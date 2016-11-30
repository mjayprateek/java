package com.adobe.initiation.server.configuration;

public class ServerConfig {
	
	private static ServerConfig config;
	private int numberOfThreads;
	
	private ServerConfig() {}
	
	public int numOfThreads() {
		return numberOfThreads;
	}
	
	/**
	 * This is non-thread safe implementation
	 * of singleton pattern. 
	 * @return
	 */
	public static ServerConfig instance() {
		if(config==null) {
			//read values and set them in the
			//newly created config object
			config = new ServerConfig();
			config.setNumberOfThreads(10);
		}
		
		return config;
	}

	private void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}
}
