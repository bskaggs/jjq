package com.github.bskaggs.jjq;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
			assertEquals(false, jq.isFinished());
		}
		jq.finish();
		assertEquals(true, jq.isFinished());
		assertEquals(Arrays.asList(expectedOutput), collector);
	}

	private String loadText(String path) {
		Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream(path), "UTF-8");
		StringBuilder builder = new StringBuilder();
		while(scanner.hasNext()) {
			builder.append(scanner.nextLine());
		}

		scanner.close();
		return builder.toString();
	}
	
	@Test
	public void testValidCompilation() throws JJQException {
		newWithPrinter(".");
		newWithPrinter(".text");
		newWithPrinter(".text | .[] | {foo: .bar}");
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
	
	@Test
	public void testAddition() throws JJQException {
		String[] values = new String[] { "4 5 6" };
		assertProgram(".+1", values, new String[] { "5", "6", "7" });
	}

	@Test
	public void testMultipleParts() throws JJQException {
		String[] values = new String[] { "4 5 6", "7" };
		assertProgram(".+1", values, new String[] { "5", "6", "68" });
	}
	
	@Test
	public void testObject() throws JJQException {
		String person = loadText("person.json");
		assertProgram(".isAlive", new String[] { person }, new String[] { "true" } );
		assertProgram(".age", new String[] { person }, new String[] { "25" } );
		assertProgram(".firstName", new String[] { person }, new String[] { "\"John\"" } );
		assertProgram(".firstName + \" \" + .lastName", new String[] { person }, new String[] { "\"John Smith\"" } );

	}
}
