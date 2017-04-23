package com.github.languagesbyyear;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class YearGroupingComparator extends WritableComparator {
	  protected YearGroupingComparator() {
			super( CompositeKeyWritable.class, true);
		}

	public int compare(WritableComparable tp1, WritableComparable tp2) {
		CompositeKeyWritable temperaturePair = (CompositeKeyWritable) tp1;
		CompositeKeyWritable temperaturePair2 = (CompositeKeyWritable) tp2;

		int result = temperaturePair.getYear() > temperaturePair2.getYear() ? +1
				: temperaturePair.getYear() < temperaturePair2.getYear() ? -1 : 0;
		return result;
	}
}
