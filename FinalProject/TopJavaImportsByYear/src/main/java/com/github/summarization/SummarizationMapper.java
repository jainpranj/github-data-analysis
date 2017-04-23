package com.github.summarization;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SummarizationMapper extends Mapper<LongWritable, Text, Text, SortedMapWritable> {
	// Our output key and value Writables

	private IntWritable bytesMap = new IntWritable();
	private static final LongWritable ONE = new LongWritable(1);
	private Text language_name = new Text();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// Parse the input string into a nice map
		if (key.get() > 0) {
			String[] tokens = value.toString().split(",");
			if (tokens.length>0) {
				String language_name = tokens[0].trim();
				if (null != language_name && !language_name.isEmpty()) {
					int language_bytes = Integer.parseInt(tokens[1].trim());

					SortedMapWritable bytesMap = new SortedMapWritable();
					bytesMap.put(new IntWritable(language_bytes), ONE);

					// write out the user ID with min max dates and count
					context.write(new Text(language_name), bytesMap);

				}
			}
		}
	}
}