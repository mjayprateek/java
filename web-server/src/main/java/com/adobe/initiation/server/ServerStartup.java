package com.adobe.initiation.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.initiation.interfaces.IServer;


public class ServerStartup {
	
	private static Logger LOG = LoggerFactory.getLogger(ServerStartup.class);

	public static void main(String[] args) {
		String hostname = getHostName(args);
		int port = 8080;
		
		Socket listeningSocket = null;
		try {
			listeningSocket = new Socket(hostname, port);
			Server.getInstance().listen(listeningSocket);
		} catch (UnknownHostException e) {
			LOG.error("Invalid hostname: " + hostname + ". Exiting ...", e);
			System.exit(1);
		} catch (IOException e) {
			LOG.error(String.format("Error while communicating with the socket with host: {0} and port {1}. Exiting ...", hostname, port), e);
			System.exit(1);
		}

	}


	private static String getHostName(String[] args) {
		return "localhost";
	}
	
	private static int getPort(String[] args) {
		return 8080;
	}
	

}
