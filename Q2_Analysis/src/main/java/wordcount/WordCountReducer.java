package wordcount;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCountReducer extends Reducer<Text, Score, Text, Score> {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(WordCountReducer.class);

    private Score totalScore = new Score();

    @Override
    protected void reduce(Text key, Iterable<Score> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        int votes = 0;
        for (Score score : values) {
            sum += score.getValue();
            votes += score.getVote();
        }
        totalScore.setValue(sum);
        totalScore.setVote(votes);
        context.write(key, totalScore);
    }
}