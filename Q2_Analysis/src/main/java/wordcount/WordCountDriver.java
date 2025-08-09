package wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCountDriver {
    private static final Logger logger = LoggerFactory.getLogger(WordCountDriver.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            logger.error(String.join(", ", args));
            logger.error("Usage: WordCountDriver <input path> <output path> [top-n]");
            System.exit(-1);
        }
        String top = "10";
        if (args.length >= 3) {
            try {
                Integer.parseInt(args[2], 10);
                top = args[2];
            }
            catch (NumberFormatException ex) {
                logger.error("Invalid number: " + args[2]);
                System.exit(-2);
            }
        }

        Configuration conf = new Configuration();
        conf.set("negative_word.top_n", top);

        /*
         * Job 1: count total score and vote
         */
        Job wordCountJob = Job.getInstance(conf, "Complaint Word Count");

        wordCountJob.setJarByClass(WordCountDriver.class);
        wordCountJob.setMapperClass(WordCountMapper.class);
        wordCountJob.setCombinerClass(WordCountReducer.class);
        wordCountJob.setReducerClass(WordCountReducer.class);

        wordCountJob.setOutputKeyClass(Text.class);
        wordCountJob.setOutputValueClass(Score.class);

        Path intermediatePath = new Path("/tmp/output/q2");
        FileSystem fs = intermediatePath.getFileSystem(conf);
        if (fs.exists(intermediatePath)) {
            fs.delete(intermediatePath, true);
        }
        FileInputFormat.addInputPath(wordCountJob, new Path(args[0]));
        FileOutputFormat.setOutputPath(wordCountJob, intermediatePath);

        if (!wordCountJob.waitForCompletion(true)) {
            System.exit(1);
        }

        /*
         * Job 2: Top-n most negative words
         */

        if (!wordCountJob.waitForCompletion(true)) {
            System.exit(1);
        }

        Job negativeWordJob = Job.getInstance(conf, "Top Negative Words");

        negativeWordJob.setJarByClass(WordCountDriver.class);
        negativeWordJob.setMapperClass(NegativeWordMapper.class);
        negativeWordJob.setCombinerClass(NegativeWordReducer.class);
        negativeWordJob.setReducerClass(NegativeWordReducer.class);

        negativeWordJob.setOutputKeyClass(IntWritable.class);
        negativeWordJob.setOutputValueClass(Text.class);

        // Write output to 1 file
        negativeWordJob.setNumReduceTasks(1);

        Path outputPath = new Path(args[1]);
        fs = outputPath.getFileSystem(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }
        FileInputFormat.addInputPath(negativeWordJob, intermediatePath);
        FileOutputFormat.setOutputPath(negativeWordJob, outputPath);

        if (!negativeWordJob.waitForCompletion(true)) {
            System.exit(1);
        }

        System.exit(0);
    }
}
