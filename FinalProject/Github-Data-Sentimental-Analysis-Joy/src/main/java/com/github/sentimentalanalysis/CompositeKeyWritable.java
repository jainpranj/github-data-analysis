package com.github.sentimentalanalysis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class CompositeKeyWritable implements Writable {

	public float getEmotionCount() {
		return emotionCount;
	}

	public void setEmotionCount(float emotionCount) {
		this.emotionCount = emotionCount;
	}

	public float getLanguageCount() {
		return languageCount;
	}

	public void setLanguageCount(float languageCount) {
		this.languageCount = languageCount;
	}

	private float emotionCount;
	private float languageCount;

	public CompositeKeyWritable() {
	}

	public CompositeKeyWritable(float emotionCount, float languageCount) {
		this.emotionCount = emotionCount;
		this.languageCount = languageCount;
	}

	@Override
	public String toString() {
		return (new StringBuilder().append(emotionCount).append("\t").append(languageCount)).toString();
	}

	public void readFields(DataInput dataInput) throws IOException {
		emotionCount = dataInput.readFloat();
		languageCount =dataInput.readFloat();
	}

	public void write(DataOutput dataOutput) throws IOException {
		dataOutput.writeFloat(emotionCount);
		dataOutput.writeFloat(languageCount);
	}







}
