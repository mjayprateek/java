package com.adobe.initiation.http.parser;

import java.util.HashSet;
import java.util.Set;

public class HttpParserUtil {
	
	private static Set<String> httpMethodsWithNoBody = new HashSet<String>();
	
	static {
		httpMethodsWithNoBody.add(HttpMethods.GET);
	}
	
	/**
	 * This is wrong implementation because
	 * Java BufferedReader strips all the lines
	 * off their CRLFs. 
	 * 
	 * A correct implementation would read raw bytes and would 
	 * look for the presence of \r\n (CRLF) characters
	 * @param s
	 * @return
	 * 
	 */
	public static boolean isCRLF(String s) {
		return s!=null && "".equals(s);
	}
	
	public static boolean methodDoesNotRequireBody(String httpMethod) {
		return httpMethodsWithNoBody.contains(httpMethod);
	}
}
