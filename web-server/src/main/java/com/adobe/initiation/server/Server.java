package com.adobe.initiation.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.interfaces.IServer;

/**
 * This server class implements the IServer interface and 
 * 
 * @author pkhatri
 *
 */
public class Server implements IServer {
	
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
	public void listen(Socket s) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String line = null;
		
		while((line=br.readLine())!=null) {
			LOG.info(line);
		}
	}

}
