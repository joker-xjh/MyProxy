package exception;

public class ProxyServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3029023659701238568L;
	
	public ProxyServerException(String message) {
		super(message);
	}
	
	public ProxyServerException(Exception exception) {
		super(exception);
	}
	
	public ProxyServerException(String message, Exception exception) {
		super(message, exception);
	}

}
