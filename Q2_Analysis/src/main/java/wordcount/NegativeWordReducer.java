package wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NegativeWordReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(NegativeWordReducer.class);

    private int count = 10;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        count = Integer.parseInt(conf.get("negative_word.top_n"), 10);
        logger.info("Counting top {} negative words", count);
    }

    @Override
    protected void reduce(IntWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        for (Text text : values) {
            if (count > 0) {
                context.write(key, text);
                count--;
            }
        }
    }
}