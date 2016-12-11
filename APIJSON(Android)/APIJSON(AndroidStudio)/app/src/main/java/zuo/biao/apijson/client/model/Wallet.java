package zuo.biao.apijson.client.model;

public class Wallet extends BaseModel {
	private static final long serialVersionUID = 4298571449155754300L;
	
	public Double balance;
	
	public Long userId;
	
	/**默认构造方法，JSON等解析时必须要有
	 */
	public Wallet() {
		super();
	}
	public Wallet(Long id) {
		this();
		this.id = id;
	}
	
	public Wallet setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	public Long getUserId() {
		return userId;
	}
	
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
