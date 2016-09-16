package wordcount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class TestWordCount {

	/*
	 * Declare harnesses that let you test a mapper, a reducer, and
	 * a mapper and a reducer working together.
	 */
	MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

	/*
	 * Set up the test. This method will be called before every test.
	 */
	@Before
	public void setUp() {

		/*
		 * Set up the mapper test harness.
		 */
		WordMapper mapper = new WordMapper();
		mapDriver = new MapDriver<LongWritable, Text, Text, IntWritable>();
		mapDriver.setMapper(mapper);

		/*
		 * Set up the reducer test harness.
		 */
		SumReducer reducer = new SumReducer();
		reduceDriver = new ReduceDriver<Text, IntWritable, Text, IntWritable>();
		reduceDriver.setReducer(reducer);

		/*
		 * Set up the mapper/reducer test harness.
		 */
		mapReduceDriver = new MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable>();
		mapReduceDriver.setMapper(mapper);
		mapReduceDriver.setReducer(reducer);
	}

	/*
	 * Test the mapper.
	 */
	@Test
	public void testMapper() throws IOException {

		mapDriver.withInput(new LongWritable(1), new Text("hello hello hadoop"));

		/*
		 * The expected output is "hello 1", "hello 1", and "hadoop 1".
		 */
		mapDriver.withOutput(new Text("hello"), new IntWritable(1));
		mapDriver.withOutput(new Text("hello"), new IntWritable(1));
		mapDriver.withOutput(new Text("hadoop"), new IntWritable(1));

		/*
		 * Run the test.
		 */
		mapDriver.runTest();
	}

	/*
	 * Test the reducer.
	 */
	@Test
	public void testReducer() throws IOException {

		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(1));
		values.add(new IntWritable(1));

		/*
		 * For this test, the reducer's input will be "hello 1 1".
		 */
		reduceDriver.withInput(new Text("hello"), values);

		/*
		 * The expected output is "hello 2"
		 */
		reduceDriver.withOutput(new Text("hello"), new IntWritable(2));

		/*
		 * Run the test.
		 */
		reduceDriver.runTest();
	}

	/*
	 * Test the mapper and reducer working together.
	 */
	@Test
	public void testMapReduce() throws IOException {

		/*
		 * For this test, the mapper's input will be "1 hello hello hadoop" 
		 */
		mapReduceDriver.withInput(new LongWritable(1), new Text("hello hello hadoop"));

		/*
		 * The expected output (from the reducer) is "hello 2", "hadoop 1". 
		 */
		mapReduceDriver.addOutput(new Text("hadoop"), new IntWritable(1));
		mapReduceDriver.addOutput(new Text("hello"), new IntWritable(2));

		/*
		 * Run the test.
		 */
		mapReduceDriver.runTest();
	}
}
