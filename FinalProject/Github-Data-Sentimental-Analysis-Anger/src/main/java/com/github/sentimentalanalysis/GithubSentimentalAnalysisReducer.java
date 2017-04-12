package com.github.sentimentalanalysis;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Reducer;

public class GithubSentimentalAnalysisReducer extends Reducer<Text, CompositeKeyWritable, Text, FloatWritable> {

	public void reduce(Text key, Iterable<CompositeKeyWritable> values, Context context)
			throws IOException, InterruptedException {
		float totalLangCount = 0;
		float totalEmotionCount = 0;
		for (CompositeKeyWritable emotionLang : values) {
			totalLangCount=totalLangCount+	emotionLang.getLanguageCount();
			totalEmotionCount=totalEmotionCount+	emotionLang.getEmotionCount();
		}
		float percentage=totalEmotionCount/totalLangCount;
		context.write(key, new FloatWritable(percentage));
	}

}
