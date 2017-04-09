package zuo.biao.apijson.server.exception;

/**不存在异常，可接受，内部吃掉
 * @author Lemon
 */
public class NotExistException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5597379135111907206L;
	
	public NotExistException() {
		super();
	}
	public NotExistException(String msg) {
		super(msg);
	}
	public NotExistException(Throwable t) {
		super(t);
	}
	public NotExistException(String msg, Throwable t) {
		super(msg, t);
	}

}

