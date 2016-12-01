package in.adobe.initiation.server.builders;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import in.adobe.initiation.server.TestHttpParser;

public class HttpRequestBuilder {
	Logger LOG = LoggerFactory.getLogger(HttpRequestBuilder.class);
	public static final String CRLF = "\r\n";
	
	String method;
	String requestURI;
	String httpVersion;
	Map<String, String> headers = new HashMap<String, String>();
	String msgbody;
	
	public HttpRequestBuilder() {}
	
	public HttpRequestBuilder(String method, String requestURI, String httpVersion) {
		super();
		this.method = method;
		this.requestURI = requestURI;
		this.httpVersion = httpVersion;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	public String getHttpVersion() {
		return httpVersion;
	}
	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}
	public void addHeader(String key, String value) {
		this.headers.put(key, value);
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getMsgbody() {
		return msgbody;
	}
	public void setMsgbody(String msgbody) {
		this.msgbody = msgbody;
	}
	
	public String construct() {
		StringBuilder sb = new StringBuilder();
		sb.append(method + " " + requestURI + " " + httpVersion);
		sb.append(CRLF);
		
		for(String key : headers.keySet()) {
			sb.append(key + ": " + headers.get(key));
			sb.append(CRLF);
		}
		
		sb.append(CRLF);
		sb.append(msgbody);
		sb.append(CRLF);
		
		String s = sb.toString();
		
		return s;
	}
}