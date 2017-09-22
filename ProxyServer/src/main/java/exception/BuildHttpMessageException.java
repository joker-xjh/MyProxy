package exception;

import java.io.IOException;

public class BuildHttpMessageException extends IOException{

	
	private static final long serialVersionUID = -4289731776374780830L;

	
	
	public BuildHttpMessageException() {
		
	}
	
	public BuildHttpMessageException(String message) {
		super(message);
	}

}
