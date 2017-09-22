package util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import exception.ConnectServerException;
import httpmessage.HttpMessage;

public class socketUtil {
	
	
	
	public static Socket connectServer(String host, int port, int timeout) throws ConnectServerException{
		Socket serverSocket = new Socket();
		SocketAddress address = new InetSocketAddress(host, port);
		try {
			serverSocket.connect(address, timeout);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectServerException("Connect Server ConnectionException");
		}
		return serverSocket;
	}
	
	
	
	
	
	
	public static boolean writeSocket(OutputStream out, HttpMessage message, boolean encrypt) {
		if(encrypt)
			message.encryptHttpMessage();
		return writeSocket(out, message.toString());
	}
	
	public static boolean writeSocket(OutputStream out, String message) {
		boolean success = false;
		try {
			out.write(message.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	
	
	
	public static void close(Socket...sockets) {
		if(sockets != null && sockets.length >0) {
			int length = sockets.length;
			for(int i=0; i<length; i++) {
				Socket socket = sockets[i];
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
