package com.adobe.initiation.interfaces;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface IServer {
	public void listen(ServerSocket s) throws IOException;
}
