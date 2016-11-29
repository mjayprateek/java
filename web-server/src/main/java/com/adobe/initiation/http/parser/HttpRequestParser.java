package com.adobe.initiation.http.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.http.request.HttpRequest;

public class HttpRequestParser {
	
	Logger LOG = LoggerFactory.getLogger(HttpRequestParser.class);
	
	private InputStream requestStream;
	
	public HttpRequestParser(InputStream requestStream) {
		this.requestStream = requestStream;
	}
	
	public HttpRequest process() throws IOException {
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(this.requestStream));
		
		Map<String, String> headers = new HashMap<String, String>();
		String line = null;
		int crlfCount = 0;
		int msgBodyLength = 0;
		int contentLength = HttpConstants.DEFAULT_CONTENT_LENGTH;
		StringBuilder sb = new StringBuilder();
		
		while(HttpParserUtil.isCRLF((line = br.readLine()))) {;}
		
		line = br.readLine();
		String[] requestLine = line.split("\\s");
		String method = requestLine[0];
		String requestURI = requestLine[1];
		String httpVersion = requestLine[2];
		
		while((line=br.readLine())!=null) {
			LOG.info(line);
			
			if(HttpParserUtil.isCRLF(line))
				crlfCount+=1;
			
			if(crlfCount==0) {
				
				String[] headerKeyValue = line.split(":");
				String headerKey = headerKeyValue[0].trim();
				String headerValue = headerKeyValue[1].trim();
				headers.put(headerKey, headerValue);
				if(HttpHeaders.contentLength.equals(headerKey)) {
					contentLength = Integer.parseInt(headers.get(HttpHeaders.contentLength)); 
				}
				
			} else {
				if(HttpParserUtil.methodDoesNotRequireBody(method))
					break;
				
				if(msgBodyLength < contentLength) {
					sb.append(line);
					msgBodyLength += line.length();
				} else {
					break;
				}
				
			}
		}
		
		HttpRequest req = new HttpRequest(method, requestURI, headers, sb.toString());
		req.setVersion(httpVersion);
		return req;
	}
}
