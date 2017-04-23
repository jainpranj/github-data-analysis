package com.github.sentimentalanalysis;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.bloom.BloomFilter;
import org.apache.hadoop.util.bloom.Key;



public class BloomFilteringDriver {



	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 3) {
			System.err.println("Usage: BloomFiltering <in> <cachefile> <out>");
			System.exit(1);
		}

		FileSystem.get(conf).delete(new Path(otherArgs[2]), true);

		Job job = new Job(conf, "Github Bloom Filtering");
		job.setJarByClass(BloomFilteringDriver.class);
		job.setMapperClass(BloomFilteringMapper.class);
		job.setReducerClass(BloomFilterReducer.class);
		job.setNumReduceTasks(3);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(CompositeKeyWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));

		DistributedCache.addCacheFile(
				FileSystem.get(conf).makeQualified(new Path(otherArgs[1]))
						.toUri(), job.getConfiguration());

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
