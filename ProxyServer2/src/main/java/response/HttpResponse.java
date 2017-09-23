package response;

import static common.Constants.CRLF;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import static common.Messages.*;
import exception.ProxyServerException;

public class HttpResponse {
	
	private String version;
	private String code;
	private String description;
	private Map<String, String> header;
	private byte[] body;
	
	public HttpResponse(InputStream in) throws ProxyServerException {
		in = new BufferedInputStream(in);
		String line = readLine(in);
		String[] array = line.split(" ");
		if(array.length != 3)
			throw new ProxyServerException(ILLEGAL_RESPONSE);
		version = array[0];
		code = array[1];
		description = array[2];
		for(line =readLine(in); !blankLine(line);line = readLine(in)) {
			array = line.split(": ");
			if(array.length !=2)
				continue;
			String key = array[0].toLowerCase().trim();
			String val = array[1].trim();
			header.put(key, val);
		}
		if(blankLine(header.get("content-length")))
			throw new ProxyServerException(ILLEGAL_RESPONSE);
		body = new byte[Integer.parseInt(header.get("content-length"))];
		try {
			in.read(body);
		} catch (IOException e) {
			throw new ProxyServerException(READ_BODY_ERR);
		}
	}
	
	private boolean blankLine(String line) {
		return line == null || line.equals("");
	}
	
	
	private String readLine(InputStream in) throws ProxyServerException {
		StringBuilder sb = new StringBuilder();
		boolean end = false;
		try {
			for(int read = in.read(); read != -1; read = in.read()) {
				if(read == '\r') {
					end = true;
					continue;
				}
				else if(read == '\n' && end)
					return sb.toString();
				else {
					end = false;
					sb.append(read);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(sb.length() !=0)
			throw new ProxyServerException(READ_HEADER_ERR);
		return null;
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(version).append(' ').append(code).append(' ').append(description).append(CRLF);
		for(String key: header.keySet()) {
			String val = header.get(key);
			sb.append(key).append(": ").append(val).append(CRLF);
		}
		sb.append(CRLF);
		return sb.toString();
	}


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	
	public byte[] getBody() {
		return body;
	}
	

}
