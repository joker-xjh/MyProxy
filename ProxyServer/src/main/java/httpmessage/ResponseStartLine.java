package httpmessage;

public class ResponseStartLine extends StartLine{
	
	//状态码
	private String code;
	
	
	//http版本
	private String version;
	
	
	//状态描述
	private String description;
	
	
	public ResponseStartLine(String line) {
		String[] array = line.split(" ");
		version = array[0];
		code = array[1];
		description = array[2];
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", version, code, description);
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
	
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getVersion() {
		return version;
	}
	

}
