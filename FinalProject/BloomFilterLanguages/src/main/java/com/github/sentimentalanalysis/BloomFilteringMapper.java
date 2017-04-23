package com.github.sentimentalanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.util.bloom.Key;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Sink;

public class BloomFilteringMapper extends Mapper<Object, Text, Text, CompositeKeyWritable> {

    Funnel<Language> language = new Funnel<Language>() {

        public void funnel(Language lang, Sink into) {
            into.putString(lang.languageName, Charsets.UTF_8);
        }
    };
     BloomFilter<Language> languages = BloomFilter.create(language, 500, 0.01);
     
     
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
                        

                        	Language lang = new Language(line.toUpperCase().trim());
                            languages.put(lang);
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
		Language language2=new Language(language);
		if (language.length() > 0 && languages.mightContain(language2)) {

			CompositeKeyWritable compositeKeyWritable = new CompositeKeyWritable(count, year);

			context.write(new Text(language), compositeKeyWritable);
		}

	}
}
