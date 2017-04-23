package com.github.sentimentalanalysis;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class BloomFilterReducer
		extends Reducer<Text, CompositeKeyWritable, Text, IntWritable> {

	@Override
	public void reduce(Text key, Iterable<CompositeKeyWritable> values, Context context)
			throws IOException, InterruptedException {
		int languagePopularityCount = 0;
		for (CompositeKeyWritable value : values) {
			languagePopularityCount = languagePopularityCount + value.getCount();
		}
		context.write(key, new IntWritable(languagePopularityCount));

	}
}