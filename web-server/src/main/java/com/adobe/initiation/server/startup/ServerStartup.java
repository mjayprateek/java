package com.adobe.initiation.server.startup;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.server.Server;


public class ServerStartup {
	
	private static Logger LOG = LoggerFactory.getLogger(ServerStartup.class);

	public static void main(String[] args) {
		String hostname = getHostName(args);
		int port = getPort(args);
		int backlog = getBacklogConnections(args);
		
		ServerSocket listeningSocket = null;
		try {
			listeningSocket = new ServerSocket(port, backlog);
			Server.instance().listen(listeningSocket);
		} catch (UnknownHostException e) {
			LOG.error("Invalid hostname: " + hostname + ". Exiting ...", e);
			System.exit(1);
		} catch (IOException e) {
			LOG.error(String.format("Error while communicating with the socket with host: %s and port %s. Exiting ...", hostname, port), e);
			System.exit(1);
		}

	}


	private static int getBacklogConnections(String[] args) {
		return 1;
	}


	private static String getHostName(String[] args) {
		return "localhost";
	}
	
	private static int getPort(String[] args) {
		return 9000;
	}
	

}
