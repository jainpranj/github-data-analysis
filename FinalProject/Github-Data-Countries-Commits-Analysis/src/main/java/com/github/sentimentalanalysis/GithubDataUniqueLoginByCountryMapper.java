package com.github.sentimentalanalysis;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GithubDataUniqueLoginByCountryMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (key.get() > 0) {
			String[] tokens = value.toString().split(",");
			String id = tokens[0].trim();
			if (id == null) {
				return;
			}

			context.write(new Text(id), new Text("U" + tokens[1]));
		}
	}
}