package proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import exception.BuildHttpMessageException;
import httpmessage.HttpRequestMessage;
import httpmessage.HttpResponseMessage;
import util.socketUtil;

public class Proxy {
	
	//是否支持压缩（默认：支持）
	protected boolean enabledAcceptEncoding = true;
	
	//是否支持长连接（默认：不支持）
	protected boolean enabledKeepAlive = false;
	
	// 转发时是否加密请求(默认: 不加密)
	private boolean encryptRequest = false;
	
	//转发时是否加密响应(默认: 不加密)
	private boolean encryptResponse = false;
	
	//接收响应时是否解密响应(默认: 不解密)
	private boolean decryptResponse = false;
	
	//客户端Socket
	protected Socket clientSocket;
	
	//客户端输出流
	protected OutputStream clientOutputStream;
	
	//服务端Socket
	protected Socket serverSocket;
	
	//服务端输出流
	protected OutputStream serverOutputStream;
	
	//服务端输入流
	protected InputStream serverInputStream;
	
	//客户端请求
	protected HttpRequestMessage requestMessage;
	
	public Proxy(Socket client, Socket server, HttpRequestMessage requestMessage) {
		this.clientSocket = client;
		this.serverSocket = server;
		this.requestMessage = requestMessage;
		try {
			this.clientOutputStream = client.getOutputStream();
			this.serverInputStream = server.getInputStream();
			this.serverOutputStream = server.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initHTTP();
	}
	
	//对客户端的请求header进行修改
	private void initHTTP() {
		if(enabledKeepAlive)
			requestMessage.addHeader("Connection", requestMessage.getHeader("Proxy-Connection"));
		else
			requestMessage.addHeader("Connection", "close");
		
		requestMessage.removeHeader("Proxy-Connection");
		
		if(!enabledAcceptEncoding)
			requestMessage.removeHeader("Accept-Encoding");
		
		System.out.println("[修改后请求: ]");
		System.out.print(requestMessage);
		System.out.println("----------");
	}
	
	//代理方法
	public void proxyHTTP() {
		
		if(!socketUtil.writeSocket(serverOutputStream, requestMessage, encryptRequest)) {
			socketUtil.close(this.clientSocket, this.serverSocket);
			return;
		}
		
		HttpResponseMessage responseMessage = null;
		
		try {
			responseMessage = new HttpResponseMessage(serverInputStream);
		} catch (BuildHttpMessageException e) {
			e.printStackTrace();
			socketUtil.close(this.clientSocket, this.serverSocket);
			return;
		}
		
		if(this.decryptResponse)
			responseMessage.decryptHttpMessage();
		
		if(!socketUtil.writeSocket(clientOutputStream, responseMessage, this.encryptResponse)) {
			socketUtil.close(this.clientSocket, this.serverSocket);
			return;
		}
		
		socketUtil.close(this.clientSocket, this.serverSocket);
	}
	
	
	
	

}
