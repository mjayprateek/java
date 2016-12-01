package com.adobe.initiation.http.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.http.exception.InvalidRequestException;
import com.adobe.initiation.http.request.HttpRequest;

public class HttpRequestParser {
	
	Logger LOG = LoggerFactory.getLogger(HttpRequestParser.class);
	
	private BufferedReader br;
	
	public HttpRequestParser(BufferedReader br) {
		this.br = br;
	}
	
	public HttpRequest process() throws IOException, InvalidRequestException {
		
		Map<String, String> headers = new HashMap<String, String>();
		String line = null;
		int crlfCount = 0;
		int msgBodyLength = 0;
		int contentLength = HttpConstants.DEFAULT_CONTENT_LENGTH;
		boolean readBody = true;
		
		while(HttpParserUtil.isCRLF((line = br.readLine()))) {;}
		
		String[] requestLine = line.split("\\s");
		String method = requestLine[0];
		String requestURI = requestLine[1];
		String httpVersion = requestLine[2];
		StringBuilder sb = new StringBuilder();
		
		if(HttpParserUtil.methodDoesNotRequireBody(method))
			readBody = false;
		
		while((line=br.readLine())!=null) {
			LOG.info(line);
			
			if(HttpParserUtil.isCRLF(line)) {
				crlfCount+=1;
				
				if(crlfCount==1)
					continue;
			}
			
			if(crlfCount==0) {
				
				int indexOfColon = line.indexOf(':');
				
				if(indexOfColon==-1)
					throw new InvalidRequestException("No header found in the http request");
				
				String headerKey = line.substring(0, indexOfColon).trim();
				String headerValue = line.substring(indexOfColon+1).trim();
				headers.put(headerKey, headerValue);
				if(HttpHeaders.contentLength.equals(headerKey)) {
					contentLength = Integer.parseInt(headers.get(HttpHeaders.contentLength)); 
				}
				
			} else {
				if(readBody && contentLength>0) {
					if(msgBodyLength < contentLength) {
						//Assuming message body uses \r\n as the line terminating character
						sb.append("".equals(line) ? "\r\n" : line+"\r\n");
						msgBodyLength += "".equals(line) ? 2 : line.length()+2;
					} else {
						break;
					}
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
