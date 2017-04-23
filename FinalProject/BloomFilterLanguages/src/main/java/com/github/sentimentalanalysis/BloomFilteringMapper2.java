package com.github.sentimentalanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;

public class BloomFilteringMapper2 extends Mapper<Object, Text, Text, CompositeKeyWritable> {

private HashSet<String> set=new HashSet();
     
     
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
	

        try {
            Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration());
            if (files != null && files.length > 0) {

                for (Path file : files) {

                    try {
                        File myFile = new File(file.toUri());
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(myFile.toString()));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                        	set.add(line.toUpperCase());


                        }
                    } catch (IOException ex) {
                        System.err.println("Exception while reading  file: " + ex.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Exception in mapper setup: " + ex.getMessage());
        }
	}

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		String[] tokens = value.toString().split("\t");
		String language = tokens[0].trim();
		int year = Integer.parseInt(tokens[1].trim());
		int count = Integer.parseInt(tokens[2].trim());
		
		if (language.length() > 0 && set.contains(language)) {

			CompositeKeyWritable compositeKeyWritable = new CompositeKeyWritable(count, year);

			context.write(new Text(language), compositeKeyWritable);
		}

	}
}
