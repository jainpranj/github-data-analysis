package com.github.sentimentalanalysis;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public  class TopTenRepoReducer extends
		Reducer<NullWritable, Text, NullWritable, Text> {

	private TreeMap<Integer, Text> repToRecordMap = new TreeMap<Integer, Text>();

	@Override
	public void reduce(NullWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {
		for (Text value : values) {
			String[] tokens = value.toString().split(",");
			String stars = tokens[0].trim();

			repToRecordMap.put(Integer.parseInt(stars),
					new Text(value));

			if (repToRecordMap.size() > 10) {
				repToRecordMap.remove(repToRecordMap.firstKey());
			}
		}

		for (Text t : repToRecordMap.descendingMap().values()) {
			context.write(NullWritable.get(), t);
		}
	}
}