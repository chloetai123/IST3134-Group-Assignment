package wordcount;

import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvMalformedLineException;
import com.opencsv.CSVReader;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, Score> {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(WordCountMapper.class);

    private Text word = new Text();
    private Score writable = new Score();
    private int reviewColumn = 2;
    private int scoreColumn = 3;
    private int voteColumn = 4;

    // Minimal stopword list (exclude common neutral words)
    private static final HashSet<String> stopWords = new HashSet<>(Arrays.asList(
            "the", "and", "you", "this", "that", "for", "with", "are", "have", "can", "not",
            "your", "all", "there", "was", "more", "one", "get", "like", "good", "fun", "great",
            "games", "play", "out", "very", "only", "when", "well", "even", "which", "also",
            "will", "would", "what", "from", "just", "they", "been", "has", "had", "but", "on",
            "it", "of", "is", "in", "to", "a", "an", "i", "my", "we", "me", "do", "did", "does"));

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(value.toString())).build();
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
            return;
        }

        if (fields.length < 5) {
            return;
        }
        // Columns: app_id,app_name,review_text,review_score,review_votes
        try {
            int score = Integer.parseInt(fields[scoreColumn].trim(), 10);
            int vote = Integer.parseInt(fields[voteColumn].trim(), 10);

            String review = fields[reviewColumn].toLowerCase()
                    .replaceAll("[^a-z\\s]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();
            StringTokenizer itr = new StringTokenizer(review);

            while (itr.hasMoreTokens()) {
                String token = itr.nextToken().trim();

                if (token.length() >= 3 && !stopWords.contains(token) && token.length() > 2) {
                    word.set(token);
                    if (score < 0) {
                        writable.setValue(-1);
                        writable.setVote(vote);
                    } else {
                        writable.setValue(1);
                        writable.setVote(vote);
                    }
                    context.write(word, writable);
                }
            }
        } catch (NumberFormatException e) {
            // Skip malformed lines
        }
    }
}