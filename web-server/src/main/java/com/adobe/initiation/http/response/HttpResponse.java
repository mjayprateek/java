package com.adobe.initiation.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	
	private String statusCode;
	private String reasonPhrase;
	private String version;
	private String content;
	private int contentLength;
	private Map<String, String> headers = new HashMap<String, String>();
	
	public HttpResponse(String statusCode, String reasonPhrase, Map<String, String> headers, String content) {
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
		this.headers = headers;
		this.content = content;
	}

	public HttpResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getContent() {
		return content;
	}

	public int getContentLength() {
		return content==null ? 0 : content.length();
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
