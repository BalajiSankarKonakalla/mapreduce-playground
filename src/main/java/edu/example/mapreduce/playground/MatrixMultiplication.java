package edu.example.mapreduce.playground;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatrixMultiplication {

    public static class MatrixMapper extends Mapper<Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            String matrixName = parts[0]; // A or B
            int i = Integer.parseInt(parts[1]);
            int j = Integer.parseInt(parts[2]);
            int val = Integer.parseInt(parts[3]);

            Configuration conf = context.getConfiguration();
            int n = Integer.parseInt(conf.get("n")); // Number of columns in A / rows in B

            if (matrixName.equals("A")) {
                for (int k = 0; k < n; k++) {
                    context.write(new Text(i + "," + k), new Text("A," + j + "," + val));
                }
            } else if (matrixName.equals("B")) {
                for (int k = 0; k < n; k++) {
                    context.write(new Text(k + "," + j), new Text("B," + i + "," + val));
                }
            }
        }
    }

    public static class MatrixReducer extends Reducer<Text, Text, Text, IntWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            List<String> aValues = new ArrayList<>();
            List<String> bValues = new ArrayList<>();

            for (Text val : values) {
                String[] parts = val.toString().split(",");
                if (parts[0].equals("A")) {
                    aValues.add(parts[1] + "," + parts[2]);
                } else if (parts[0].equals("B")) {
                    bValues.add(parts[1] + "," + parts[2]);
                }
            }

            int sum = 0;
            for (String a : aValues) {
                String[] aParts = a.split(",");
                int aColumn = Integer.parseInt(aParts[0]);
                int aValue = Integer.parseInt(aParts[1]);

                for (String b : bValues) {
                    String[] bParts = b.split(",");
                    int bRow = Integer.parseInt(bParts[0]);
                    int bValue = Integer.parseInt(bParts[1]);

                    if (aColumn == bRow) {
                        sum += aValue * bValue;
                    }
                }
            }

            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("n", "2"); // Set matrix dimensions (change as needed)

        Job job = Job.getInstance(conf, "Matrix Multiplication");
        job.setJarByClass(MatrixMultiplication.class);
        job.setMapperClass(MatrixMapper.class);
        job.setReducerClass(MatrixReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
