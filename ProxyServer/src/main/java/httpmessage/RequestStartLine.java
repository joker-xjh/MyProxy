package httpmessage;


public class RequestStartLine extends StartLine{
	
	private String version;
	
	private String url;
	
	private String method;
	
	public RequestStartLine(String line) {
		String[] array = line.split(" ");
		method = array[0];
		url = array[1];
		version = array[2];
	}
	
	
	@Override
	public String toString() {
		return String.format("%s %s %s", this.method, this.url,version);
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	

}
