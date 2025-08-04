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

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Gradle 8.4.3 or higher
- Hadoop 3.4.1 or higher

### 1. Clone the GitHub Repository
```bash
git clone https://github.com/chloetai/bigdata.git
cd bigdata
```

### 2. Create Hadoop File System
```bash
hadoop fs -mkdir -p hdfs/input/q2
```

### 3. Add the Data Files

Copy the Steam Review dataset to `hdfs/input/q2`:
```bash
hadoop fs -put dataset.csv hdfs/input/q2
```

### 4. Build the Project
```bash
./gradlew build
```

### 5. Create the Executable JAR
```bash
./gradlew shadowJar
```

This creates an executable JAR file at: `build/libs/sentiment-analyzer.jar`

## Running the Application

### Option 1: Run with Gradle
```bash
./gradlew runWithSample
```

### Option 2: Run the JAR directly
```bash
java -jar build/libs/sentiment-analyzer.jar hdfs/input/q2 hdfs/output_q2
```

### Option 3: Run with Hadoop
```bash
hadoop jar build/libs/sentiment-analyzer.jar hdfs/input/q2 hdfs/output_q2
```

## Input Data Format

The application accepts Steam Review dataset, which is a comma-separated file.
The columns are:

* app_id - game's ID
* app_name - game's name
* review_text - review text
* review_score - the review score (-1 for negative review, 1 for positive review)
* review_votes - the number of votes that agree with the review


## Output Example

```
best	1	1
forever	1	1
game	1	1
hype	-1	1
life	1	0
money	-1	0
ruined	0	1
waste	-1	0
weekend	-1	1
```

## Dependencies

- **OpenCSV**: Read/write CSV files
- **SLF4J + Logback**: Logging framework
- **Shadow Plugin**: Creates fat JAR with all dependencies
