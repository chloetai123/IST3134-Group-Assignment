package wordcount;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Score implements Writable {
    int value;
    int vote;

    public Score() {
    }

    public Score(int value, int vote) {
        this.value = value;
        this.vote = vote;
    }

    public int getValue() {
        return value;
    }

    public int getVote() {
        return vote;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return value + "\t" + vote;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(value);
        out.writeUTF("\t");
        out.writeInt(vote);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.value = in.readInt();
        in.readUTF();
        this.vote = in.readInt();
    }
}
