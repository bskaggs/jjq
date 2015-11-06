package com.github.bskaggs.jjq;

import java.util.ArrayList;
import java.util.List;

import com.github.bskaggs.jjq.jna.JqLibrary;
import com.github.bskaggs.jjq.jna.jv.ByValue;
import com.sun.jna.Pointer;

public class ErrorStore implements JqLibrary.jq_msg_cb {
	final private List<String> errors = new ArrayList<String>();

	@Override
	public void apply(Pointer store, ByValue jv) {
		if (JJQ.INSTANCE.jv_get_kind(jv) == JqLibrary.jv_kind.JV_KIND_STRING) {
			String str = JJQ.INSTANCE.jv_string_value(jv).getString(0);
			errors.add(str);
		}
	}
	public List<String> getErrors() {
		return errors;
	}
}