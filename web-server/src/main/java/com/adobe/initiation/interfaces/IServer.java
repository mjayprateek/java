package com.adobe.initiation.interfaces;

import java.io.IOException;
import java.net.Socket;

public interface IServer {
	public void listen(Socket listeningSocket) throws IOException;
}
