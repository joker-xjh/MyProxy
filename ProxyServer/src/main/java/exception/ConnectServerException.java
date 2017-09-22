package exception;

import java.io.IOException;

public class ConnectServerException extends IOException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4465389130270724823L;
	
	public ConnectServerException() {
		
	}
	
	public ConnectServerException(String message) {
		super(message);
	}
	
}
