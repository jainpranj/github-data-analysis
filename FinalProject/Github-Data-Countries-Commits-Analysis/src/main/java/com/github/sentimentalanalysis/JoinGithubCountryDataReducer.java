package com.github.sentimentalanalysis;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class JoinGithubCountryDataReducer extends Reducer<Text, Text, Text, Text> {
	private ArrayList<Text> countryData = new ArrayList<Text>();
	private ArrayList<Text> uniqueRatioCountryData = new ArrayList<Text>();
	private static final Text EMPTY_TEXT = new Text();
	private Text tmp = new Text();
	private String joinType = null;

	protected void setup(Context context) throws IOException, InterruptedException {
		joinType = context.getConfiguration().get("join.type");
	}

	public void reduce(Text country, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		countryData.clear();
		uniqueRatioCountryData.clear();

		// for (Text value : values) {
		while (values.iterator().hasNext()) {
			 tmp = values.iterator().next();
			if (tmp.charAt(0) == 'C') {
				countryData.add(new Text(tmp.toString().substring(1)));
			} else if (tmp.charAt(0) == 'U') {
				uniqueRatioCountryData.add(new Text(tmp.toString().substring(1)));
			}
		}
		applyJoin(context);
	}

	private void applyJoin(Context context) throws IOException, InterruptedException {

		if (joinType.equalsIgnoreCase("INNER")) {
			// inner join
			if (!uniqueRatioCountryData.isEmpty() && !countryData.isEmpty()) {
				for (Text A : countryData) {
					for (Text B : uniqueRatioCountryData) {
						context.write(A, B);
					}
				}
			}

		} else if (joinType.equalsIgnoreCase("LEFTOUTER")) {

			if (!countryData.isEmpty()) {
				for (Text A : countryData) {
					if (!uniqueRatioCountryData.isEmpty()) {
						for (Text B : uniqueRatioCountryData) {
							context.write(A, B);
						}
					} else {
						context.write(A, EMPTY_TEXT);
					}
				}
			}

		} else if (joinType.equalsIgnoreCase("RIGHTOUTER")) {

			if (!uniqueRatioCountryData.isEmpty()) {
				for (Text B : uniqueRatioCountryData) {
					if (!countryData.isEmpty()) {
						for (Text A : countryData) {
							context.write(A, B);
						}
					} else {
						context.write(EMPTY_TEXT, B);
					}
				}
			}

		} else if (joinType.equalsIgnoreCase("FULLOUTER")) {
			if (!countryData.isEmpty()) {
				for (Text A : countryData) {
					if (!uniqueRatioCountryData.isEmpty()) {
						for (Text B : uniqueRatioCountryData) {
							context.write(A, B);
						}
					} else {
						context.write(A, new Text(""));
					}
				}
			} else {
				for (Text B : uniqueRatioCountryData) {
					context.write(new Text(""), B);
				}
			}

		} else if (joinType.equalsIgnoreCase("ANTIJOIN")) {
			if (countryData.isEmpty() ^ uniqueRatioCountryData.isEmpty()) {
				for (Text A : countryData) {
					{
						context.write(A, new Text(""));
					}
					for (Text B : uniqueRatioCountryData) {
						{
							context.write(new Text(""), B);
						}
					}
				}

			} else {
				throw new RuntimeException("No matching join");
			}
		}
	}
}
