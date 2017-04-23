package com.github.sentimentalanalysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class LangaugesOverYearParitioner extends Partitioner<CompositeKeyWritable, IntWritable> {

	@Override
	public int getPartition(CompositeKeyWritable key, IntWritable value, int numPartitions) {
		
		return key.getYear()-2015;
		
	}
}