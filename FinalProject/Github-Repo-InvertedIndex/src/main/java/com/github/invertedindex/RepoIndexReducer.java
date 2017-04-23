package com.github.invertedindex;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class RepoIndexReducer extends Reducer<Text, Text, Text, Text> {
Text result=new Text();
	public void reduce(Text reporUrl, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Text lanugaes : values) {
			if (first) {
				first = false;
			} else {
				sb.append(" ");
			}
			sb.append(lanugaes);
		}
		result.set(sb.toString());
		context.write(reporUrl, result);
	}

}
