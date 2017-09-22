package httpmessage;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import exception.BuildHttpMessageException;
import util.PasswordUtils;

public class HttpRequestMessage extends HttpMessage{
	
	
	public HttpRequestMessage(InputStream in) throws BuildHttpMessageException {
		super(in);
	}
	

	@Override
	public boolean isSupportBody() {
		String method = ((RequestStartLine)super.getStartLine()).getMethod();
		if(method.equals("POST"))
			return true;
		return false;
	}

	@Override
	public StartLine buildStartLine(String line) {
		return new RequestStartLine(line);
	}

	@Override
	public HttpMessage encryptHttpMessage() {
		StartLine startLine = getStartLine();
		if(startLine instanceof RequestStartLine) {
			RequestStartLine requestStartLine = (RequestStartLine)startLine;
			requestStartLine.setUrl(PasswordUtils.base64Encrypt(requestStartLine.getUrl()));
			setStartLine(requestStartLine);
		}
		Map<String, String> header = getHeaders();
		for(Entry<String, String> entry : header.entrySet()) {
			entry.setValue(PasswordUtils.base64Encrypt(entry.getKey()));
		}
		return this;
	}

	@Override
	public HttpMessage decryptHttpMessage() {
		StartLine startLine = getStartLine();
		if(startLine instanceof RequestStartLine) {
			RequestStartLine requestStartLine = (RequestStartLine)startLine;
			requestStartLine.setUrl(PasswordUtils.base64Decrypt(requestStartLine.getUrl()));
			setStartLine(requestStartLine);
		}
		Map<String, String> header = getHeaders();
		for(Entry<String, String> entry : header.entrySet()) {
			entry.setValue(PasswordUtils.base64Decrypt(entry.getKey()));
		}
		return this;
	}

	

}
