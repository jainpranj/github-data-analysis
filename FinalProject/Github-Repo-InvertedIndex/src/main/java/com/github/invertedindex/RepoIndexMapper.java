package com.github.invertedindex;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RepoIndexMapper extends Mapper<LongWritable, Text, Text, Text> {

	public String appendUrl = "https://github.com/";

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		if (key.get() > 0) {
			String[] tokens = value.toString().split(",");
			if (tokens.length > 0) {
				if(null!=tokens[0] && !tokens[0].isEmpty()){
				String repo = appendUrl.concat(tokens[0].trim());
				
				if(tokens.length>1 && null!=tokens[1] && !tokens[1].isEmpty()){
				String language_name = tokens[1].trim();
				context.write(new Text(repo), new Text(language_name));
				}
				}
				

			}
		}
	}
}