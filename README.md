# Hadoop MapReduce Project

This project contains two Hadoop MapReduce programs:

1. **WordCount** - A classic MapReduce example that counts word occurrences in a text file.
2. **MatrixMultiplication** - A MapReduce implementation of matrix multiplication.

## Prerequisites

- Java 8 or 11
- Apache Hadoop 3.3.6
- Maven (for building the project)

## Project Structure
```
mapreduce-playground/
│── src/main/java/edu/example/mapreduce/playground/
│   ├── WordCount.java
│   ├── WordCountWithCombiner.java
│   ├── MatrixMultiplication.java
│── src/main/resources/
│   ├── sample_text_file_for_wordcount.txt  # Sample input for WordCount
│   ├── input_matrix_2_2     # Sample input for MatrixMultiplication
│── pom.xml
│── README.md
```

---

## Building the Project

Navigate to the project directory and run:
```sh
mvn clean package
```
This generates a JAR file inside the `target/` directory.

---

## Running the Programs

### 1. Running **WordCount**
#### **Prepare Input**
```sh
hdfs dfs -mkdir -p input
hdfs dfs -put input/wordcount.txt input/
```
#### **Run the Job**
```sh
hadoop jar target/mapreduce-project-1.0-SNAPSHOT.jar WordCount input output_wordcount
```
#### **View the Output**
```sh
hdfs dfs -cat output_wordcount/part-r-00000
```

---

### 2. Running **MatrixMultiplication**
#### **Prepare Input**
```sh
hdfs dfs -mkdir -p input
hdfs dfs -put input/matrix.txt input/
```
#### **Run the Job**
```sh
hadoop jar target/mapreduce-project-1.0-SNAPSHOT.jar MatrixMultiplication input output_matrix
```
#### **View the Output**
```sh
hdfs dfs -cat output_matrix/part-r-00000
```

---

## Notes
- Ensure Hadoop is running before executing the commands (`start-dfs.sh` and `start-yarn.sh`).
- Modify `pom.xml` to change dependencies if needed.

**Happy Coding!** 🚀

