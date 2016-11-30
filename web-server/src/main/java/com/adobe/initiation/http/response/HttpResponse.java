package com.adobe.initiation.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	
	private String method;
	private String requestURI;
	private String version;
	private Map<String, String> headers = new HashMap<String, String>();
	private String content;
	private int contentLength;
	
	public HttpResponse(String method, String requestURI, Map<String, String> headers, String content) {
		this.method = method;
		this.requestURI = requestURI;
		this.headers = headers;
		this.content = content;
	}

	public HttpResponse() {
		// TODO Auto-generated constructor stub
	}

	public String getMethod() {
		return method;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getVersion() {
		return version;
	}

	public String getContent() {
		return content;
	}

	public int getContentLength() {
		return content==null ? 0 : content.length();
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void setHeader(String key, String value) {
		this.headers.put(key, value);
	}
	
	public String getHeaderValue(String key) {
		return this.headers.get(key);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
}
