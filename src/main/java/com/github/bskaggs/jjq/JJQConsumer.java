package com.github.bskaggs.jjq;

import java.io.IOException;

public interface JJQConsumer {
	public void accept(String obj) throws IOException;
}