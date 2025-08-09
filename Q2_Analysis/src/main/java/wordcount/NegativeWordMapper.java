package wordcount;

import java.io.IOException;
import java.io.StringReader;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvMalformedLineException;

public class NegativeWordMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(NegativeWordMapper.class);

    private Text word = new Text();
    private IntWritable writable = new IntWritable();
    private int wordColumn = 0;
    private int scoreColumn = 1;
    private int voteColumn = 2;

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator('\t')
                .build();
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(value.toString()))
                .withCSVParser(csvParser)
                .build();
        String[] fields;

        try {
            fields = csvReader.readNext();
        } catch (CsvMalformedLineException | CsvException ex) {
            // Quitely skip the malformed line
            // logger.error(ex.getMessage(), ex);
            return;
        }

        // Skip header line
        if (key.get() == 0) {
            logger.error("No key");
            return;
        }

        if (fields.length < 3) {
            logger.error("field length < 3");
            return;
        }
        // Columns: word,score,vote
        try {
            int score = Integer.parseInt(fields[scoreColumn].trim(), 10);
            int vote = Integer.parseInt(fields[voteColumn].trim(), 10);

            if (vote > 0) {
                word.set(fields[wordColumn]);
                writable.set(score);
                context.write(writable, word);
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            // Skip malformed lines
        }
    }
}