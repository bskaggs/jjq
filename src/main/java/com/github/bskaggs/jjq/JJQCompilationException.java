package com.github.bskaggs.jjq;

/**
 * Represents an error has occurred when compiling a jq program.
 * @author bskaggs
 *
 */
public class JJQCompilationException extends JJQException {
	private static final long serialVersionUID = -3209667282517177913L;

	public JJQCompilationException(String msg, String program) {
		super(msg);
		this.program = program;
	}
	
	private String program;
	
	public String getProgram() {
		return program;
	}
}
