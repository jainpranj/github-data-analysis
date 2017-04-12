package com.github.sentimentalanalysis;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class GithubSentimentalAnlysisMapper extends Mapper<LongWritable, Text, Text, CompositeKeyWritable> {
	public static String joyWordsRegex;


	public IntWritable one = new IntWritable(1);
	public static final String[] listOfEmotions = { "Anger", "Joy", "Amusement", "Surpise", "Issue/Bugs", "Swearing" };

	protected void setup(Context context) throws IOException, InterruptedException {

		// joy
		joyWordsRegex = "(?i).*\\b(yes|yay|hallelujah|hurray|bingo|amused|cheerful|excited|glad|proud)\\b.*";




	}

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (key.get() > 0) {
	
			String commitMessage = value.toString().substring(0, value.toString().lastIndexOf(',')).trim();
			String language = value.toString().substring(value.toString().lastIndexOf(',') + 1).trim();
			CompositeKeyWritable emotionLang=new CompositeKeyWritable();
			if (commitMessage.matches(joyWordsRegex)) {
				emotionLang.setEmotionCount(1);
				
			}
			emotionLang.setLanguageCount(1);
			context.write(new Text(language), emotionLang);
		}

	}



}