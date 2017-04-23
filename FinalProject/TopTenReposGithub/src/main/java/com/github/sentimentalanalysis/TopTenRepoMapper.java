package com.github.sentimentalanalysis;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TopTenRepoMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
	// Our output key and value Writables
	private TreeMap<Integer, Text> repToRecordMap = new TreeMap<Integer, Text>();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// Parse the input string into a nice map
		if (key.get() > 0) {
			String[] tokens = value.toString().split(",");
			String stars = tokens[0].trim();
			

			repToRecordMap.put(Integer.parseInt(stars), new Text(value));

			if (repToRecordMap.size() > 10) {
				repToRecordMap.remove(repToRecordMap.firstKey());
			}
		}
	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		for (Text t : repToRecordMap.values()) {
			context.write(NullWritable.get(), t);
		}
	}
}