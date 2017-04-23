package com.github.summarization;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Reducer;

public class SummarizationCombiner extends Reducer<Text, SortedMapWritable, Text, SortedMapWritable> {

	@SuppressWarnings("rawtypes")
	protected void reduce(Text key, Iterable<SortedMapWritable> values, Context context)
			throws IOException, InterruptedException {

		SortedMapWritable outValue = new SortedMapWritable();

		for (SortedMapWritable v : values) {
			for (Entry<WritableComparable, Writable> entry : v.entrySet()) {
				LongWritable count = (LongWritable) outValue.get(entry.getKey());

				if (count != null) {
					count.set(count.get() + ((LongWritable) entry.getValue()).get());
				} else {
					outValue.put(entry.getKey(), new LongWritable(((LongWritable) entry.getValue()).get()));
				}
			}
		}

		context.write(key, outValue);
	}
}