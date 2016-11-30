package in.adobe.initiation.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.adobe.initiation.http.parser.HttpMethods;
import com.adobe.initiation.http.parser.HttpRequestParser;
import com.adobe.initiation.http.request.HttpRequest;

import in.adobe.initiation.server.builders.HttpRequestBuilder;

public class TestHttpParser {
	
	@Test
	public void parserReturnsCorrectHttpMethod() throws IOException {
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
	public void parsesTheHttpRequestCorrectlyWithLeadingCRLFs() throws IOException {
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
	public void httpRequestDoesNotContainBodyIfTheMethodIsGet() throws IOException {
		HttpRequestBuilder hrb = httpRequestBuilderWithBasicHeaders();
		hrb.setMethod("GET");
		hrb.setRequestURI("/hello");
		String httpRequestStr = hrb.construct();

		HttpRequest req = parseHttpRequest(httpRequestStr);
		
		assertEquals("", req.getContent());
		assertEquals(0, req.getContentLength());
	}

	private HttpRequest parseHttpRequest(String httpRequestStr) throws IOException {
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
