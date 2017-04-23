package com.github.languagesbyyear;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class CompositeKeyWritable implements Writable, WritableComparable<CompositeKeyWritable> {

	private String language;
	private int year;

	public CompositeKeyWritable() {
	}

	public CompositeKeyWritable(String language, int year) {
		this.language = language;
		this.year = year;
	}

	@Override
	public String toString() {
		return (new StringBuilder().append(language).append("\t").append(year)).toString();
	}

	public void readFields(DataInput dataInput) throws IOException {
		language = WritableUtils.readString(dataInput);
		year = WritableUtils.readVInt(dataInput);
	}

	public void write(DataOutput dataOutput) throws IOException {
		WritableUtils.writeString(dataOutput, language);
		WritableUtils.writeVInt(dataOutput, year);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int compareTo(CompositeKeyWritable objKeyPair) {

		int result = this.language.compareTo(objKeyPair.language);
//		if (result == 0) {
//			 result = this.year > objKeyPair.year ? +1 : this.year < objKeyPair.year ? -1 : 0;
//		}
		return result;
	}

}
