# Java Sentiment Analysis Project

This project performs sentiment analysis on reviews using Stanford CoreNLP and extracts the most frequently used words in positive and negative reviews.

## Project Structure

```
bigdata/
├── build.gradle
├── src/
│   └── main/
│       └── java/
│           └── wordcount/
│               ├── NegativeWordMapper.java
│               ├── NegativeWordReducer.java
│               ├── Score.java
│               ├── WordCountDriver.java
│               ├── WordCountMapper.java
│               └── WordCountReducer.java
└── README.md
```

1. `build.gralde` - Build script
2. `Score.java` - Output model containing the sum of the review score and the frequency of votes
3. `WordCountDriver.java` - Main program
4. `WordCountMapper.java` - Mapper
5. `WordCountReducer.java` - Reducer
6. `NegativeWordMapper.java` - Mapper to generate top negative words
7. `NegativeWordMapper.java` - Reducer to generate top negative words

## Setup Instructions

### Prerequisites
- Java 8 or higher
- Gradle 8.4.3 or higher
- Hadoop 3.4.1 or higher

### 1. Clone the GitHub Repository
```bash
git clone https://github.com/chloetai123/IST3134-Group-Assignment.git
cd IST3134-Group-Assignment
git checkout q2
cd Q2_Analysis
```

### 2. Create Hadoop File System
```bash
hadoop fs -mkdir -p /input/q2
hadoop fs -mkdir -p /output/q2
```

### 3. Add the Data Files

Copy the Steam Review dataset to `/input/q2`:
```bash
hadoop fs -put /path/to/dataset.csv /input/q2/dataset.csv
```

### 4. Build the Project
```bash
./gradlew build
```

## Running the Application

```bash
hadoop jar build/libs/sentiment-analyzer.jar /input/q2/dataset.csv /output/q2 10
```

## Input Data Format (Comma-delimited)

The application accepts Steam Review dataset, which is a comma-separated file.
The columns are:

* app_id - game's ID
* app_name - game's name
* review_text - review text
* review_score - the review score (-1 for negative review, 1 for positive review)
* review_votes - the number of votes that agree with the review


## Output Example (Tab-delimited)

```
-24374	waste
-21350	terrible
-21312	worst
-20282	refund
-15981	unplayable
-14006	horrible
-13547	boring
-11307	awful
-11149	garbage
-9110	broken
```

## Jobs

### Word Count

* Input: CSV file
* Output: word,score,vote (intermediate, tab-delimited format)

### Negative Word

* Input: word,score,vote (intermediate file)
* Output: score,word (tab-delimited format)


## Dependencies

- **OpenCSV**: Read/write CSV files
- **SLF4J + Logback**: Logging framework
- **Shadow Plugin**: Creates fat JAR with all dependencies
