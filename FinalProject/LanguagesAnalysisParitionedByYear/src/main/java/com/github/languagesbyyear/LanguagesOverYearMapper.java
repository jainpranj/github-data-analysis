package com.github.languagesbyyear;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class LanguagesOverYearMapper extends Mapper<LongWritable, Text, CompositeKeyWritable, IntWritable> {
	IntWritable one = new IntWritable(1);

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// Parse the input string into a nice map
		if (key.get() > 0) {
			String[] tokens = value.toString().split(",");
			int year = Integer.parseInt(tokens[1].trim());
			String language = tokens[2].trim().toUpperCase();

			CompositeKeyWritable compositeKeyWritable = new CompositeKeyWritable(language, year);

			context.write(compositeKeyWritable, one);

		}
	}
}
