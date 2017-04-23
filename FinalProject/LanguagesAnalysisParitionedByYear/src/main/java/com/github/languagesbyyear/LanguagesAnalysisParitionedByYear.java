package com.github.languagesbyyear;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LanguagesAnalysisParitionedByYear {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Path inputPath = new Path(args[0]);

		Path outputDir = new Path(args[1]);

		// Create configuration
		Configuration conf = new Configuration(true);

		Job job = new Job(conf, "LanguagesOverYear");
		job.setJarByClass(LanguagesAnalysisParitionedByYear.class);
		job.setMapperClass(LanguagesOverYearMapper.class);
		job.setReducerClass(LanguagesOverYearReducer.class);
		job.setPartitionerClass(LangaugesOverYearParitioner.class);
		//job.setGroupingComparatorClass(YearGroupingComparator.class);
		job.setNumReduceTasks(3);
		job.setMapOutputKeyClass(CompositeKeyWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(CompositeKeyWritable.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputDir);
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}