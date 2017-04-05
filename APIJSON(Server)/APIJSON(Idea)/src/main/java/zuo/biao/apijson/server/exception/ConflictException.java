package zuo.biao.apijson.server.exception;


public class ConflictException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2237022658440709153L;
	
	public ConflictException() {
		super();
	}
	public ConflictException(String msg) {
		super(msg);
	}
	public ConflictException(Throwable t) {
		super(t);
	}
	public ConflictException(String msg, Throwable t) {
		super(msg, t);
	}

}
