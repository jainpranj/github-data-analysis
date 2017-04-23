package com.github.sentimentalanalysis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class CompositeKeyWritable implements Writable {

	private int count;
	private int year;

	public CompositeKeyWritable() {
	}

	public CompositeKeyWritable(int count, int year) {
		this.count = count;
		this.year = year;
	}

	@Override
	public String toString() {
		return (new StringBuilder().append(count).append("\t").append(year)).toString();
	}

	public void readFields(DataInput dataInput) throws IOException {
		count = WritableUtils.readVInt(dataInput);
		year = WritableUtils.readVInt(dataInput);
	}

	public void write(DataOutput dataOutput) throws IOException {
		WritableUtils.writeVInt(dataOutput, count);
		WritableUtils.writeVInt(dataOutput, year);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
