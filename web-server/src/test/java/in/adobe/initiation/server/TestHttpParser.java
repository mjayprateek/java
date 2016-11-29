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

public class TestHttpParser {
	
	private static final String CRLF = "\r\n";

	@Test
	public void parserReturnsCorrectHttpMethod() throws IOException {
		String httpRequest = constructHttpGetRequest();

		InputStream in = new ByteArrayInputStream(httpRequest.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		HttpRequestParser hr = new HttpRequestParser(br);
		HttpRequest req = hr.process();
		
		assertEquals(HttpMethods.GET, req.getMethod());
	}

	private String constructHttpGetRequest() {
		StringBuilder sb = new StringBuilder();
		sb.append("GET /hello HTTP/1.1");
		sb.append(CRLF);
		sb.append("Host: localhost:9000");
		sb.append(CRLF);
		sb.append("User-Agent: prateek-test");
		sb.append(CRLF);
		sb.append("Accept: */*");
		sb.append(CRLF);
		String httpRequest = sb.toString();
		return httpRequest;
	}
	
}
