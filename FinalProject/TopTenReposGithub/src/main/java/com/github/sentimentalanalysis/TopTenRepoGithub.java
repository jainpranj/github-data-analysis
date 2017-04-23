package com.github.sentimentalanalysis;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TopTenRepoGithub  {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Path inputPath = new Path(args[0]);
		
		Path outputDir = new Path(args[1]);

		// Create configuration
		Configuration conf = new Configuration(true);


		Job job = new Job(conf, "Top Ten Users by Reputation");
		job.setJarByClass(TopTenRepoGithub.class);
		job.setMapperClass(TopTenRepoMapper.class);
		job.setReducerClass(TopTenRepoReducer.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, inputPath);
		FileOutputFormat.setOutputPath(job,outputDir);
		System.exit(job.waitForCompletion(true) ? 0 : 1);


	}



}