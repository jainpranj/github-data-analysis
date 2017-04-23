package com.github.languagesbyyear;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class LanguagesOverYearReducer
		extends Reducer<CompositeKeyWritable, IntWritable, CompositeKeyWritable, IntWritable> {

	@Override
	public void reduce(CompositeKeyWritable key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int languagePopularityCount = 0;
		for (IntWritable value : values) {
			languagePopularityCount = languagePopularityCount + value.get();
		}
		context.write(key, new IntWritable(languagePopularityCount));

	}
}