package com.github.bskaggs.jjq;

import java.util.ArrayList;
import java.util.List;

public class SampleAdder {

	public static void main(String[] args) throws JJQException {
		//This program takes its input and adds one to it
		String adderProgram = ". + 1";
		                        
		//JJQConsumer objects receive results as the are emitted from jq.
		//They have exactly one function, `accept`. Let's make one that
		//just appends each result to a list.

		final List<String> collector = new ArrayList<String>();
		JJQConsumer consumer = new JJQConsumer() {
		        @Override
		        public void accept(String obj) {
		                collector.add(obj);
		        }
		};

		//When you create a JJQ object, you must specify both the program String
		//and the JJQConsumer.        
		JJQ jjq = new JJQ(adderProgram, consumer);

		//Now, let's give ten numbers to jq. Note that we have to have whitespace of
		//some kind to delimit the objects.
		for (int i = 1; i <= 10; i++) {
		        jjq.add(i + " ");
		}
		//When you are done, you must call the finish method to finalize any tokens that
		//might be left in jq's buffer.
		jjq.finish();

		System.out.println(collector);
		//Output: [2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
	}

}
