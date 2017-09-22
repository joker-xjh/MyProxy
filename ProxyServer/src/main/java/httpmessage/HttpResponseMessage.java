package httpmessage;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import exception.BuildHttpMessageException;
import util.PasswordUtils;

public class HttpResponseMessage extends HttpMessage{

	public HttpResponseMessage(InputStream in) throws BuildHttpMessageException {
		super(in);
	}

	@Override
	public boolean isSupportBody() {
		return true;
	}

	@Override
	public HttpMessage encryptHttpMessage() {
		Map<String, String> header = getHeaders();
		for(Entry<String, String> entry : header.entrySet()) {
			entry.setValue(PasswordUtils.base64Encrypt(entry.getKey()));
		}
		return this;
	}

	@Override
	public HttpMessage decryptHttpMessage() {
		Map<String, String> header = getHeaders();
		for(Entry<String, String> entry : header.entrySet()) {
			entry.setValue(PasswordUtils.base64Decrypt(entry.getKey()));
		}
		return this;
	}

	@Override
	public StartLine buildStartLine(String line) {
		return new ResponseStartLine(line);
	}

}
