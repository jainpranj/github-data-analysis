package com.github.sentimentalanalysis;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

public class JoinGithubCountryData extends Configured implements Tool {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		
		Path outputDir = new Path(args[3]);

		// Create configuration
		Configuration conf = new Configuration(true);

		// Create job
		//Job job = new Job(conf, "LogFile");
		Job job = new Job(conf, "Inner Join");
		// Configure the join type
		job.getConfiguration().set("join.type", args[2]);
		
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class,JoinGithubCountryDataMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class,GithubDataUniqueLoginByCountryMapper.class);
		job.setReducerClass(JoinGithubCountryDataReducer.class);
		job.setNumReduceTasks(1);

		// Specify key / value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);



		// Output
		FileOutputFormat.setOutputPath(job, new Path(args[3]));
		job.setOutputFormatClass(TextOutputFormat.class);

		// Delete output if exists
		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(outputDir))
			hdfs.delete(outputDir, true);

		// Execute job
		int code = job.waitForCompletion(true) ? 0 : 2;
		System.exit(code);

	}

	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}