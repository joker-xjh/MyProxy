package httpmessage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import exception.BuildHttpMessageException;

//Http报文
public abstract class HttpMessage {
	
	//起始行
	private StartLine startLine;
	
	//首部(key全部转化为小写)
	private Map<String, String> header = new HashMap<String, String>();

	//实体
	private byte[] body;
	
	//获取起始行
	public StartLine getStartLine() {
		return startLine;
	}
	
	//设置起始行
	public void setStartLine(StartLine startLine) {
		this.startLine = startLine;
	}
	
	//添加首部
	public void addHeader(String key, String value) {
		if(key != null && key.length() > 0 && value != null && value.length() >0) {
			header.put(key.toLowerCase().trim(), value.trim());
		}
	}
	
	
	//获取首部
	public String getHeader(String key) {
		return header.get(key.toLowerCase().trim());
	}
	
	
	//获取所有首部
	public Map<String, String> getHeaders(){
		return header;
	}
	
	
	//删除首部
	public void removeHeader(String key) {
		header.remove(key.toLowerCase().trim());
	}
	
	
	//是否有首部
	public boolean headerIsEmpty() {
		return header.isEmpty();
	}
	
	
	//获取实体
	public byte[] getBody() {
		return body;
	}
	
	
	public HttpMessage(InputStream in) throws BuildHttpMessageException{
		buildHttpMessage(in);
	}
	
	
	
	
	public abstract boolean isSupportBody();
	
	
	private String buildStartLine(InputStream inputStream) throws BuildHttpMessageException {
		return readLine(inputStream);
	}
	
	
	private String readLine(InputStream in) throws BuildHttpMessageException{
		StringBuffer sb = new StringBuffer();
		boolean EOF = false;
		int read = 0;
		try {
			for(read = in.read(); read != -1; read = in.read()) {
				if(read == '\r') {
					EOF = true;
					continue;
				}
				else if(read == '\n' && EOF) {
					return sb.toString();
				}
				sb.append((char)read);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildHttpMessageException();
		}
		if(sb.length() > 0)
			throw new BuildHttpMessageException();
		return null;
	}
	
	
	
	
	
	private void buildHeader(InputStream in) throws BuildHttpMessageException{
		try {
			for(String line = readLine(in); line.length() != 0; line = readLine(in)) {
				String[] array = line.split(":");
				if(array.length != 2)
					continue;
				String key = array[0];
				String value = array[1];
				addHeader(key, value);
			}
		} catch (BuildHttpMessageException e) {
			e.printStackTrace();
			throw new BuildHttpMessageException();
		}
	}
	
	
	private void buildBody(InputStream in) {
		in = new BufferedInputStream(in);
		int length = Integer.parseInt(header.get("content-length"));
		body = new byte[length];
		try {
			in.read(body);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public HttpMessage buildHttpMessage(InputStream in) throws BuildHttpMessageException {
		if(in == null)
			throw new BuildHttpMessageException("inputstream is null");
		in = new BufferedInputStream(in);
		String line = buildStartLine(in);
		startLine = buildStartLine(line);
		buildHeader(in);
		if(isSupportBody())
			buildBody(in);
		return this;
	}
	
	
	public abstract HttpMessage encryptHttpMessage();
	
	public abstract HttpMessage decryptHttpMessage();
	
	public abstract StartLine buildStartLine(String line);
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(startLine.toString());
		sb.append("\r\n");
		for(Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key).append(": ").append(value).append("\r\n");
		}
		sb.append("\r\n");
		if(isSupportBody())
			try {
				sb.append(new String(body, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return sb.toString();
	}
	
	
	
}
