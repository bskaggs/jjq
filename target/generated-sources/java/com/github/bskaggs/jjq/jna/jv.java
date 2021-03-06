package com.github.bskaggs.jjq.jna;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : src/main/c/jq.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class jv extends Structure {
	public byte kind_flags;
	public byte pad_;
	/** array offsets */
	public short offset;
	public int size;
	/** C type : u_union */
	public u_union u;
	/** <i>native declaration : src/main/c/jq.h:24</i> */
	public static class u_union extends Union {
		/** C type : jv_refcnt* */
		public com.github.bskaggs.jjq.jna.jv_refcnt.ByReference ptr;
		public double number;
		public u_union() {
			super();
		}
		/** @param ptr C type : jv_refcnt* */
		public u_union(com.github.bskaggs.jjq.jna.jv_refcnt.ByReference ptr) {
			super();
			this.ptr = ptr;
			setType(com.github.bskaggs.jjq.jna.jv_refcnt.ByReference.class);
		}
		public u_union(double number) {
			super();
			this.number = number;
			setType(Double.TYPE);
		}
		public u_union(Pointer peer) {
			super(peer);
		}
		public static class ByReference extends u_union implements Structure.ByReference {
			
		};
		public static class ByValue extends u_union implements Structure.ByValue {
			
		};
	};
	public jv() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("kind_flags", "pad_", "offset", "size", "u");
	}
	/**
	 * @param offset array offsets<br>
	 * @param u C type : u_union
	 */
	public jv(byte kind_flags, byte pad_, short offset, int size, u_union u) {
		super();
		this.kind_flags = kind_flags;
		this.pad_ = pad_;
		this.offset = offset;
		this.size = size;
		this.u = u;
	}
	public jv(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends jv implements Structure.ByReference {
		
	};
	public static class ByValue extends jv implements Structure.ByValue {
		
	};
}
