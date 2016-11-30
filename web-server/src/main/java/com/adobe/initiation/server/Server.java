package com.adobe.initiation.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.http.parser.HttpRequestParser;
import com.adobe.initiation.http.request.HttpRequest;
import com.adobe.initiation.server.configuration.ServerConfig;
import com.adobe.initiation.thread.pool.task.HttpRequestProcessingTask;

/**
 * This server class implements the IServer interface and 
 * 
 * @author pkhatri
 *
 */
public class Server implements IServer {
	
	private static final int DEFAULT_CONTENT_LENGTH = 1000;

	Logger LOG = LoggerFactory.getLogger(Server.class);
	
	private static Server server;
	private ExecutorService executorService;
	
	private Server(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
	//This method is a thread-safe implementation of 
	//the singleton pattern with late initialization
	public static Server instance() {
		if(server==null) {
			synchronized(Server.class) {
				if(server==null) {
					server = new Server(Executors.newFixedThreadPool(ServerConfig.instance().numOfThreads()));
					return server;
				}
			}
		}
		
		return server;
	}

	@Override
	public void listen(ServerSocket serverSocket) throws IOException {
		
		boolean hasNotStopped = true;
		while(hasNotStopped) {
			Socket s = serverSocket.accept();
			
			//Create a new Thread and pass on the input and output streams of the socket for processing
			executorService.execute(new HttpRequestProcessingTask(s.getInputStream(), s.getOutputStream()));
			
			s.close();
		}
	}
	
	

}
