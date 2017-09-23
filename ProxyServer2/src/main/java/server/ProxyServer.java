package server;

import java.net.ServerSocket;
import java.net.Socket;

import cache.Cache;
import cache.LRUCache;
import exception.ProxyServerException;
import request.HttpRequest;
import request.RequestHandler;
import response.HttpResponse;
import util.SocketUtil;

public class ProxyServer {
	
	private ServerSocket serverSocket;
	private Cache cache;
	
	
	public synchronized boolean isCache(HttpRequest request) {
		return cache.isCached(request);
	}
	
	public synchronized HttpResponse getCache(HttpRequest request) {
		return cache.get(request);
	}
	
	public synchronized void putCache(HttpRequest request, HttpResponse response) {
		cache.put(request, response);
	}
	
	private void handle(Socket client) {
		RequestHandler handler = new RequestHandler(client, this);
		handler.start();
	}
	
	
	public ProxyServer(int port, Cache cache) throws ProxyServerException {
		serverSocket =SocketUtil.createServerSocket(port);
		this.cache = cache;
	}
	
	public void run() {
		while(true) {
			try {
				Socket client = SocketUtil.accpetClient(serverSocket);
				handle(client);
			} catch (ProxyServerException e) {
				e.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args) {
		Cache cache = new LRUCache();
		try {
			ProxyServer server = new ProxyServer(6666, cache);
			server.run();
		} catch (ProxyServerException e) {
			e.printStackTrace();
		}
		
	}

}
