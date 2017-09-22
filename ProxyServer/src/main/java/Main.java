import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import constant.Properties;
import pool.ThreadPoolManager;
import thread.HttpProxyThread;

public class Main {
	
	private static ServerSocket serverSocket;
	
	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(Properties.ListenerPort);
			while(true) {
				Socket client = serverSocket.accept();
				ThreadPoolManager.execute(new HttpProxyThread(client));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
