package com.adobe.initiation.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.http.parser.HttpHeaders;
import com.adobe.initiation.http.parser.HttpParserUtil;
import com.adobe.initiation.interfaces.IServer;

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
	
	private Server() {};
	
	public static Server getInstance() {
		if(server==null) {
			synchronized(Server.class) {
				if(server==null) {
					server = new Server();
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
			//Create a new Thread and pass on the new socket to the thread
			
			
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bw.write("HTTP/1.1 200 OK\n\tHello, World!\n\t");
			bw.flush();
			bw.close();
		}
	}

}
