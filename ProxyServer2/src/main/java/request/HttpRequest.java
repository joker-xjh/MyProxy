package request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Map;

import exception.ProxyServerException;
import response.HttpResponse;
import util.SocketUtil;

import static common.Constants.*;
import static common.Messages.*;
public class HttpRequest {

	
	private Map<String, String> header;
	private String method;
	private String version;
	private String uri;
	
	public HttpRequest(InputStream in) throws ProxyServerException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, UTF_8));
			String line = reader.readLine();
			if(blankLine(line))
				throw new ProxyServerException(ILLEGAL_REQUEST_ERR);
			String[] array = line.split(" ");
			if(array.length != 3)
				throw new ProxyServerException(ILLEGAL_REQUEST_ERR);
			method = array[0];
			uri = array[1];
			version = array[2];
			for(line = reader.readLine();!blankLine(line);line = reader.readLine()) {
				array = line.split(" ");
				if(array.length != 2)
					continue;
				String key = array[0].toLowerCase().trim();
				String val = array[1].trim();
				header.put(key, val);
			}
			if(blankLine(header.get(HOST)))
				throw new ProxyServerException(ILLEGAL_REQUEST_ERR);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new ProxyServerException(BUILD_REQUEST_ERR, e);
		}
	}
	
	
	public HttpResponse execute() throws ProxyServerException {
		Socket server = SocketUtil.createSocket(header.get(HOST), HTTP_PORT);
		OutputStream outputStream = SocketUtil.getOutputStream(server);
		String send = this.toString();
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
			writer.write(send);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new ProxyServerException(SEND_TO_HOST_ERR, e);
		}
		HttpResponse response = new HttpResponse(SocketUtil.getInputStream(server));
		SocketUtil.close(server);
		return response;
	}

	
	private boolean blankLine(String line) {
		return line == null || line.equals("");
	}
	
	
	public Map<String, String> getHeader() {
		return header;
	}
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(method).append(' ').append(uri).append(' ').append(version).append(CRLF);
		for(String key: header.keySet()) {
			String val = header.get(key);
			sb.append(key).append(": ").append(val).append(CRLF);
		}
		sb.append(CRLF);
		return sb.toString();
	}
	
	
	

}
