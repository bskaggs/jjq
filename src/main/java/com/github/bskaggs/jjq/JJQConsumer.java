package com.github.bskaggs.jjq;

import java.io.IOException;

/**
 * Collects results from jq native code.
 *
 * @author bskaggs
 */
public interface JJQConsumer {
	/**
	 * Called when a new result is available from jq
	 * @param obj
	 * @throws IOException
	 */
	public void accept(String obj) throws IOException;
}