package zuo.biao.apijson.server;

public class ConditionNotMatchException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7067141825558078593L;
	
	public ConditionNotMatchException() {
		super();
	}
	public ConditionNotMatchException(String msg) {
		super(msg);
	}
	public ConditionNotMatchException(Throwable t) {
		super(t);
	}
	public ConditionNotMatchException(String msg, Throwable t) {
		super(msg, t);
	}
}
