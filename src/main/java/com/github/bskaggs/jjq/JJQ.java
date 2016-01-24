package com.github.bskaggs.jjq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.github.bskaggs.jjq.jna.JqLibrary;
import com.github.bskaggs.jjq.jna.jv;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;


public class JJQ {
	public static final NativeLibrary ONIG_NATIVE_LIB = NativeLoader.loadLibrary("onig");
	public static final NativeLibrary JQ_NATIVE_LIB = NativeLoader.loadLibrary("jq");
	public static final JqLibrary INSTANCE = (JqLibrary)Native.loadLibrary(JQ_NATIVE_LIB.getName(), JqLibrary.class);
	
	private final PointerByReference jq;
    private final PointerByReference parser;
    private final ErrorStore errorStore = new ErrorStore();
    private final int dumpFormat = 0;
    private final String program;
    private final JJQConsumer consumer;
    
	public JJQ(String program, JJQConsumer consumer) throws JJQException{
		this.program = program;
		this.consumer = consumer;
    	jq = INSTANCE.jq_init();
    	if (jq == null) {
    		throw new JJQException("Error initializing jq");
    	}
    	
    	INSTANCE.jq_set_error_cb(jq, errorStore, new Pointer(0));
        
        int compiled = INSTANCE.jq_compile(jq, program);
        
        if (!errorStore.getErrors().isEmpty()) {
        	throw new JJQCompilationException(errorStore.getErrors().toString());
        }
        if (compiled == 0) {
        	throw new JJQCompilationException("Program was not valid");
        }
        
        parser = INSTANCE.jv_parser_new(0);
    }
	
	private boolean jv_is_valid(jv.ByValue v) {
		return INSTANCE.jv_get_kind(v) != JqLibrary.jv_kind.JV_KIND_INVALID;
	}
	
	public void add(String string) throws JJQException {
		add(string.getBytes(StandardCharsets.UTF_8), false);
	}
	
	public void add(byte[] bytes, boolean finished) throws JJQException {
		Memory memory = new Memory(bytes.length);
		memory.write(0, bytes, 0, bytes.length);
		add(memory, bytes.length, finished);
	}
	
	public void add(Pointer pointer, int length, boolean finished) throws JJQException {
		INSTANCE.jv_parser_set_buf(parser, pointer, length, finished ? 0 : 1);
		jv.ByValue value;
		
		while (jv_is_valid(value = INSTANCE.jv_parser_next(parser))) {
			process(value);
		}
		
		handleInvalid(value, "Parse error: ");
		if (!errorStore.getErrors().isEmpty()) {
			throw new JJQException(errorStore.getErrors().toString());
		}
	}
	
	
	public void finish() throws JJQException {
		add(new Pointer(-1), 0, true);
		INSTANCE.jq_set_error_cb(jq, null, null);
	}
	
	private void process(jv.ByValue value) throws JJQException {
		INSTANCE.jq_start(jq, value, 0);
		jv.ByValue result;
		
		while (jv_is_valid(result = INSTANCE.jq_next(jq))) {
			jv.ByValue dumped = INSTANCE.jv_dump_string(result, dumpFormat);
			String str = INSTANCE.jv_string_value(dumped).getString(0, StandardCharsets.UTF_8.name());
			INSTANCE.jv_free(dumped);
			try {
				consumer.accept(str);
			} catch (IOException e) {
				throw new JJQException(e);
			}
		}
		handleInvalid(result, "");
	}
	
	private void handleInvalid(jv.ByValue value, String prefix) {

		int hasMessage = INSTANCE.jv_invalid_has_msg(INSTANCE.jv_copy(value));
		
		if (hasMessage != 0) {
			jv.ByValue message = INSTANCE.jv_invalid_get_msg(value);

			errorStore.getErrors().add(prefix + INSTANCE.jv_string_value(message).getString(0, StandardCharsets.UTF_8.name()));
			INSTANCE.jv_free(message);
		} else {
			INSTANCE.jv_free(value);
		}
	}
		
	@Override
    protected void finalize() throws Throwable {
		if (parser != null) {
			INSTANCE.jv_parser_free(parser);
		}
    	if (jq != null) {
    		INSTANCE.jq_set_error_cb(jq, null, null);
    		INSTANCE.jq_teardown(jq);
    	}
    	super.finalize();
    }
    
    public String getProgram() {
		return program;
	}

    @Override
    public String toString() {
    	return "JJQ<" + program + ">";
    }   
}
