package request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import exception.ProxyServerException;
import response.HttpResponse;
import server.ProxyServer;
import util.SocketUtil;

public class RequestHandler extends Thread{
	
	private Socket client;
	private ProxyServer proxyServer;
	
	public RequestHandler(Socket client, ProxyServer proxyServer) {
		this.client = client;
		this.proxyServer = proxyServer;
	}
	
	private HttpResponse getResponse(HttpRequest request) throws ProxyServerException {
		HttpResponse response = null;
		if(proxyServer.isCache(request))
			response = proxyServer.getCache(request);
		else
			response = request.execute();
		return response;
	}
	
	private void replyToClient(HttpResponse response) throws ProxyServerException {
		OutputStream out = SocketUtil.getOutputStream(client);
		out = new BufferedOutputStream(out);
		try {
			out.write(response.toString().getBytes("UTF-8"));
			out.write(response.getBody());
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new ProxyServerException("Error send Response to client");
		}
		
	}
	
	@Override
	public void run() {
		try {
			HttpRequest request = new HttpRequest(SocketUtil.getInputStream(client));
			HttpResponse response = getResponse(request);
			replyToClient(response);
		} catch (ProxyServerException e) {
			e.printStackTrace();
		}
	}
	
	
	

}
