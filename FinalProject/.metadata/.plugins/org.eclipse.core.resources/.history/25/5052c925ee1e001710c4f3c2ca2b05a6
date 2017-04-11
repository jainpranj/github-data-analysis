package com.github.sentimentalanalysis;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GithubSentimentalAnlysisMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	public static String angerWordsRegex;

	public static HashMap<String, String> emotionRegexMap = new HashMap<String, String>();
	public IntWritable one = new IntWritable(1);
	public static final String[] listOfEmotions = { "Anger", "Joy", "Amusement", "Surpise", "Issue/Bugs", "Swearing" };

	protected void setup(Context context) throws IOException, InterruptedException {
		// anger
		angerWordsRegex = "(?i).*\\b(a+rgh|angry|annoyed|annoying|appalled|bitter|cranky|hate|hating|mad)\\b.*";
		emotionRegexMap.put(listOfEmotions[0], angerWordsRegex);


	}

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (key.get() > 0) {
			// String tokens[]=value.toString().substring(0,
			// value.toString().lastIndexOf(','));
			String commitMessage = value.toString().substring(0, value.toString().lastIndexOf(',')).trim();
			String language = value.toString().substring(value.toString().lastIndexOf(',') + 1).trim();

			if (commitMessage.matches(angerWordsRegex)) {
				context.write(new Text(language), one);
			}
		}

	}



}