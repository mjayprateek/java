package com.adobe.initiation.thread.pool.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.http.parser.HttpRequestParser;
import com.adobe.initiation.http.request.HttpRequest;

public class HttpRequestProcessingTask implements Runnable {
	
	private static final String CRLF = "\r\n";

	Logger LOG = LoggerFactory.getLogger(HttpRequestProcessingTask.class);

	private InputStream httpReqInputStream;
	private OutputStream httpReqOutputStream;
	
	public HttpRequestProcessingTask(InputStream httpReqInputStream, OutputStream httpReqOutputStream) {
		this.httpReqInputStream = httpReqInputStream;
		this.httpReqOutputStream = httpReqOutputStream;
	}
	
	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(httpReqInputStream));
		
		HttpRequest hr = null;
		try {
			hr = new HttpRequestParser(br).process();
		} catch(IOException e) {
			LOG.error("Error while parsing http request.", e);
			sendResponse("HTTP/1.1 400 Bad Request"+CRLF+"Hello World!"+CRLF);
			return;
		}
		
		//Pick a controller implementation here and pass on the HttpRequest object to it
		//And, let it return the HttpResponse object which then can be written 
		//to the outputStream
		
		sendResponse("HTTP/1.1 200 OK"+CRLF+"Hello World!"+CRLF);
		
		try {
			br.close();
		} catch(IOException e) {
			LOG.error("Error while closing the BufferedReader stream of the socket ", e);
		}
	}

	private void sendResponse(String httpResponseStr) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(httpReqOutputStream));
			bw.write(httpResponseStr);
		} catch(IOException ioe) {
			LOG.error("Error while writing response to the socket", ioe);
		} finally {
			try {
				bw.flush();
				bw.close();
			} catch (IOException e) {
				LOG.error("Could not close the buffered writer for the socket properly.", e);
			}
		}
	}

}
