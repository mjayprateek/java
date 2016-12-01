package in.adobe.initiation.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.http.exception.InvalidRequestException;
import com.adobe.initiation.http.parser.HttpHeaders;
import com.adobe.initiation.http.parser.HttpMethods;
import com.adobe.initiation.http.parser.HttpRequestParser;
import com.adobe.initiation.http.request.HttpRequest;

import in.adobe.initiation.server.builders.HttpRequestBuilder;

public class TestHttpParser {
	
	Logger LOG = LoggerFactory.getLogger(TestHttpParser.class);
	
	private static final String CRLF = "\r\n";

	@Test
	public void parserReturnsCorrectHttpMethod() throws IOException, InvalidRequestException {
		HttpRequestBuilder hrb = httpRequestBuilderWithBasicHeaders();
		hrb.setMethod("GET");
		hrb.setRequestURI("/hello");
		String httpRequestStr = hrb.construct();

		HttpRequest req = parseHttpRequest(httpRequestStr);
		
		assertEquals(HttpMethods.GET, req.getMethod());
		assertEquals("HTTP/1.1", req.getVersion());
		assertEquals("localhost:9000", req.getHeaderValue("Host"));
		assertEquals("*/*", req.getHeaderValue("Accept"));
		assertEquals("prateek-test", req.getHeaderValue("User-Agent"));
	}
	
	@Test
	public void parsesTheHttpRequestCorrectlyWithLeadingCRLFs() throws IOException, InvalidRequestException {
		HttpRequestBuilder hrb = httpRequestBuilderWithBasicHeaders();
		hrb.setMethod("GET");
		hrb.setRequestURI("/hello");
		String httpRequestWithLeadingCRLFs = HttpRequestBuilder.CRLF
				+ HttpRequestBuilder.CRLF
				+ HttpRequestBuilder.CRLF
				+ HttpRequestBuilder.CRLF
				+ hrb.construct();

		HttpRequest req = parseHttpRequest(httpRequestWithLeadingCRLFs);
		
		assertEquals(HttpMethods.GET, req.getMethod());
		assertEquals(3, req.getHeaders().size());
	}
	
	@Test
	public void httpRequestDoesNotContainBodyIfTheMethodIsGet() throws IOException, InvalidRequestException {
		HttpRequestBuilder hrb = httpRequestBuilderWithBasicHeaders();
		hrb.setMethod("GET");
		hrb.setRequestURI("/hello");
		String httpRequestStr = hrb.construct();

		HttpRequest req = parseHttpRequest(httpRequestStr);
		
		assertEquals("", req.getContent());
		assertEquals(0, req.getContentLength());
	}
	
	@Test
	public void readsTheHttpRequestBodyProperlyIfContentLengthHeaderIsPresent() throws IOException, InvalidRequestException {
		HttpRequestBuilder hrb = httpRequestBuilderWithBasicHeaders();
		hrb.setMethod("POST");
		hrb.setRequestURI("/hello");
		String msgbody = "This is a post request";
		hrb.setMsgbody(msgbody);
		hrb.addHeader(HttpHeaders.contentLength, String.valueOf(msgbody.length()));
		String httpRequestStr = hrb.construct();

		HttpRequest req = parseHttpRequest(httpRequestStr);
		
		//This is wrong. \r\n should not have been appended.
		assertEquals(msgbody+"\r\n", req.getContent());
		assertEquals(msgbody.length(), req.getContentLength());
	}
	
	//This is based on the assumption that message body uses
	//\r\n as the line terminating character
	@Test
	public void readsTheHttpRequestBodyWithLineBreaksProperlyIfContentLengthHeaderIsPresent() throws IOException, InvalidRequestException {
		HttpRequestBuilder hrb = httpRequestBuilderWithBasicHeaders();
		hrb.setMethod("POST");
		hrb.setRequestURI("/hello");
		String msgbody = "This is a post request\r\nAnother Line\r\nSome other Line\r\n\r\n\r\n\r\n\r\n";
		hrb.setMsgbody(msgbody);
		hrb.addHeader(HttpHeaders.contentLength, String.valueOf(msgbody.length()));
		String httpRequestStr = hrb.construct();

		HttpRequest req = parseHttpRequest(httpRequestStr);
		
		assertEquals(msgbody, req.getContent());
		assertEquals(msgbody.length(), req.getContentLength());
	}
	
	@Test
	public void doesNotReadTheHttpRequestBodyIfContentLengthHeaderIsNotPresent() throws IOException, InvalidRequestException {
		HttpRequestBuilder hrb = httpRequestBuilderWithBasicHeaders();
		hrb.setMethod("POST");
		hrb.setRequestURI("/hello");
		String msgbody = "This is a post request";
		hrb.setMsgbody(msgbody);
		String httpRequestStr = hrb.construct();

		HttpRequest req = parseHttpRequest(httpRequestStr);
		
		assertEquals("", req.getContent());
		assertEquals(0, req.getContentLength());
	}

	private HttpRequest parseHttpRequest(String httpRequestStr) throws IOException, InvalidRequestException {
		InputStream in = new ByteArrayInputStream(httpRequestStr.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		HttpRequestParser hr = new HttpRequestParser(br);
		HttpRequest req = hr.process();
		return req;
	}

	private HttpRequestBuilder httpRequestBuilderWithBasicHeaders() {
		HttpRequestBuilder hrb = new HttpRequestBuilder();
		hrb.setHttpVersion("HTTP/1.1");
		hrb.addHeader("Host", "localhost:9000");
		hrb.addHeader("User-Agent", "prateek-test");
		hrb.addHeader("Accept", "*/*");
		return hrb;
	}
}
