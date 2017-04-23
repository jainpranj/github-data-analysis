package com.github.summarization;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Summarization  {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Path inputPath = new Path(args[0]);
		
		Path outputDir = new Path(args[1]);

		// Create configuration
		Configuration conf = new Configuration(true);


		Job job = new Job(conf, "Summarization File Size And Langugae");
		job.setJarByClass(Summarization.class);
		job.setMapperClass(SummarizationMapper.class);
		
		job.setReducerClass(SummarizationReducer.class);
		job.setNumReduceTasks(1);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(SortedMapWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MedianStdDevTuple.class);
		FileInputFormat.addInputPath(job, inputPath);
		job.setInputFormatClass(CustomFileInputFormat.class);

		FileOutputFormat.setOutputPath(job,outputDir);
		System.exit(job.waitForCompletion(true) ? 0 : 1);


	}



}