package com.github.bskaggs.jjq;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestJJQ {
	@Before
	public void setUp() throws Exception {
		
	}
	
	public JJQ newWithPrinter(String program) throws JJQException {
		JJQConsumer printer = new JJQConsumer() {
			
			@Override
			public void accept(String obj) {
				System.out.println(obj);
			}
		};
		return new JJQ(program, printer);
	}

	private void assertProgram(String program, String[] input, String[] expectedOutput) throws JJQException {
		final List<String> collector = new ArrayList<String>();
		JJQ jq = new JJQ(program, new JJQConsumer() {
			@Override
			public void accept(String obj) {
				collector.add(obj);
			}
		});
		for (String value : input) {
			jq.add(value);
		}
		jq.finish();
		assertEquals(Arrays.asList(expectedOutput), collector);
	}
	@SuppressWarnings("unused")
	@Test
	public void testValidCompilation() throws JJQException {
		JJQ jq1 = newWithPrinter(".");
		JJQ jq2 = newWithPrinter(".text");
		JJQ jq3 = newWithPrinter(".text | .[] | {foo: .bar}");
	}

	
	@Test (expected = JJQCompilationException.class)
	public void testInvalidProgram() throws JJQException {
		assertProgram("{", null, null);
	}
	
	@Test
	public void testIdentity() throws JJQException {
		String[] values = new String[] { "[4]", "5", "{\"k\":\"v\"}" };
		assertProgram(".", values, values);
	}
	
	@Test (expected = JJQException.class)
	public void testIncomplete() throws JJQException {
		assertProgram(".", new String[] { "[4" }, null);
	}
	
	@Test (expected = JJQException.class)
	public void testInvalidInput() throws JJQException {
		assertProgram(".", new String[] { "}" }, null);
	}
}
