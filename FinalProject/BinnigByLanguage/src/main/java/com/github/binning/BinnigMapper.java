package com.github.binning;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class BinnigMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

	private MultipleOutputs<Text, NullWritable> mos = null;


	protected void setup(Context context) {
		// Create a new MultipleOutputs using the context object
		mos = new MultipleOutputs(context);
	}

	
	protected void map(LongWritable key, Text value, Context context) {

		if (key.get() > 0) {
			String[] tokens = value.toString().split(",");



		// For each tag
		for (String tag : tagTokens) {
			// Remove any > or < from the token
			String groomed = tag.replaceAll(">|<", "").toLowerCase();

			// If this tag is one of the following, write to the named bin
			if (groomed.equalsIgnoreCase("hadoop")) {
				mos.write("bins", value, NullWritable.get(), "hadoop-tag");
			}

			if (groomed.equalsIgnoreCase("pig")) {
				mos.write("bins", value, NullWritable.get(), "pig-tag");
			}

			if (groomed.equalsIgnoreCase("hive")) {
				mos.write("bins", value, NullWritable.get(), "hive-tag");
			}

			if (groomed.equalsIgnoreCase("hbase")) {
				mos.write("bins", value, NullWritable.get(), "hbase-tag");
			}
		}

		// Get the body of the post
		String post = parsed.get("Body");

		if (post == null) {
			return;
		}

		// If the post contains the word "hadoop", write it to its own bin
		if (post.toLowerCase().contains("hadoop")) {
			mos.write("bins", value, NullWritable.get(), "hadoop-post");
		}
	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		// Close multiple outputs!
		mos.close();
	}
}