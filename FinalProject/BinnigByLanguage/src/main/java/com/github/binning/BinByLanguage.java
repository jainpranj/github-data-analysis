package com.github.binning;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class BinByLanguage {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Path inputPath = new Path(args[0]);

		Path outputDir = new Path(args[1]);

		// Create configuration
		Configuration conf = new Configuration(true);

		Job job = new Job(conf, "Binning");
		job.setJarByClass(BinByLanguage.class);
		job.setMapperClass(BinnigMapper.class);
		job.setNumReduceTasks(0);

		TextInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputDir);

		// Configure the MultipleOutputs by adding an output called "bins"
		// With the proper output format and mapper key/value pairs
		MultipleOutputs.addNamedOutput(job, "bins", TextOutputFormat.class, Text.class, NullWritable.class);

		// Enable the counters for the job
		// If there is a significant number of different named outputs, this
		// should be disabled
		MultipleOutputs.setCountersEnabled(job, true);
		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(outputDir))
			hdfs.delete(outputDir, true);

		System.exit(job.waitForCompletion(true) ? 0 : 2);
		

	}

}