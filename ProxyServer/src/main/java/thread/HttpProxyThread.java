package thread;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import constant.Properties;
import exception.ConnectServerException;
import httpmessage.HttpRequestMessage;
import proxy.Proxy;
import util.socketUtil;

public class HttpProxyThread extends Thread{
	
	//客户端Socket
	private Socket clientSocket;
	
	//服务端Socket
	private Socket serverSocket;
	
	public HttpProxyThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		
	}
	
	
	@Override
	public void run() {
		HttpRequestMessage requestMessage = null;
		try {
			requestMessage = new HttpRequestMessage(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			socketUtil.close(clientSocket, serverSocket);
			return;
		}
		//解密
		requestMessage = (HttpRequestMessage) requestMessage.decryptHttpMessage();
		
		System.out.println("[原始请求:]");
		System.out.print(requestMessage);
		System.out.println("----------");
		
		String host = requestMessage.getHeader("host");
		if(host == null || host.length() == 0) {
			socketUtil.close(clientSocket);
			return;
		}
		int port = 80;
		
		try {
			this.serverSocket = socketUtil.connectServer(host, port, Properties.serverSocketConnectTimeout);
		} catch (ConnectServerException e) {
			e.printStackTrace();
			socketUtil.close(clientSocket, serverSocket);
			return;
		}
		
		try {
			clientSocket.setSoTimeout(Properties.clientSocketReadTimeout);
			serverSocket.setSoTimeout(Properties.clientSocketReadTimeout);
		} catch (SocketException e) {
			e.printStackTrace();
			socketUtil.close(clientSocket, serverSocket);
		}
		Proxy proxy = new Proxy(clientSocket, serverSocket, requestMessage);
		proxy.proxyHTTP();
	}
	
	
	
	
	

}
