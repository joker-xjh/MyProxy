package cache;

import request.HttpRequest;
import response.HttpResponse;

public interface Cache {
	
	boolean isCached(HttpRequest request);
	
	void put(HttpRequest request, HttpResponse response);
	
	HttpResponse get(HttpRequest request);

}
