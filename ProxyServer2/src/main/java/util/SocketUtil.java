package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import static common.Messages.*;
import static common.Constants.*;
import exception.ProxyServerException;

public class SocketUtil {
	
	public static InputStream getInputStream(Socket socket) throws ProxyServerException {
		try {
			return socket.getInputStream();
		} catch (IOException e) {
			throw new ProxyServerException(GET_INPUT_STREAM_ERR, e);
		}
	}
	
	public static OutputStream getOutputStream(Socket socket) throws ProxyServerException {
		try {
			return socket.getOutputStream();
		} catch (IOException e) {
			throw new ProxyServerException(GET_OUTPUT_STEAM_ERR, e);
		}
	}
	
	public static void close(Socket socket) throws ProxyServerException {
		try {
			socket.close();
		} catch (IOException e) {
			throw new ProxyServerException(CLOSE_SOCK_ERR, e);
		}
	}
	
	public static Socket createSocket(String host, int port) throws ProxyServerException {
		Socket socket = new Socket();
		SocketAddress address = null;
		try {
			address = new InetSocketAddress(host, port);
			socket.setSoTimeout(SOCKETTIMEOUT);
			socket.connect(address);
			return socket;
		} catch (UnknownHostException e) {
			throw new ProxyServerException(UNKNOWN_HOST_ERR, e);
		} catch (IOException e) {
			throw new ProxyServerException(e);
		}
	}
	
	public static ServerSocket createServerSocket(int port) throws ProxyServerException {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			return serverSocket;
		} catch (IOException e) {
			throw new ProxyServerException(CREATE_SERVER_SOCK_ERR, e);
		}
	}
	
	public static Socket accpetClient(ServerSocket serverSocket) throws ProxyServerException {
		try {
			Socket client = serverSocket.accept();
			return client;
		} catch (IOException e) {
			throw new ProxyServerException(ACCEPT_SOCK_ERR, e);
		}
	}
	

}
