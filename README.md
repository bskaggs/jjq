jjq: jq for Java
================

jjq let's you use [jq](https://stedolan.github.io/jq/) to slice and dice JSON in Java.  It's not a re-implementation of jq in Java; instead, it embeds the necessary jq and Oniguruma native libraries in a jar file, and then uses [Java Native Access](https://github.com/java-native-access/jna) (JNA) to call the embedded libraries in a Java-friendly way. This library primarily exists to let you use jq with Hadoop MapReduce as part of [hjq](https://github.com/bskaggs/hjq).

jjq is heavily inspired by [jq.py](https://github.com/mwilliamson/jq.py) and [ruby-jq](https://github.com/winebarrel/ruby-jq), but any imperfections are my own.

Building
========

jjq requires Apache Maven to build, as well as standard C build tools for building the jq and Oniguruma dependencies.  It has only been tested on Linux x86-64.

To build, simply run
```
make
```

The build process will download jq and oniguruma from source, build them in the deps directory, and symlink the libraries into the right place in the source tree.  Then, Apache Maven fetches the Java dependencies, and builds the platform-specific jar.  If you want, `mvn install` will place the resulting jar file in your local Maven repository.

Usage
=====

Let's make a program that adds one to each element in a sequence of numbers.
```java
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

//Now, let's give ten numbers to jjq. Note that we have to have whitespace of
//some kind to delimit the objects.
for (int i = 1; i <= 10; i++) {
	jjq.add(i + " ");
}
//When you are done, you must call the finish method to finalize any tokens that
//might be left in jq's buffer.
jjq.finish();

System.out.println(collector);
//Output: [2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
```
