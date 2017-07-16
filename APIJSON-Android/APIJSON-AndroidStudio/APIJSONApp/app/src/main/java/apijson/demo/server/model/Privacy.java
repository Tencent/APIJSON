package apijson.demo.server.model;

import static zuo.biao.apijson.RequestRole.ADMIN;
import static zuo.biao.apijson.RequestRole.OWNER;
import static zuo.biao.apijson.RequestRole.UNKNOWN;

import zuo.biao.apijson.MethodAccess;

/**用户隐私信息类
 * @author Lemon
 */
@MethodAccess(
		GET = {},
		POST_GET = {OWNER, ADMIN},
		POST = {UNKNOWN, ADMIN},
		DELETE = {ADMIN}
		)
public class Privacy extends BaseModel {
	private static final long serialVersionUID = 1L;

	private String phone; //手机
	private String password; //登录密码，隐藏字段
	private String payPassword; //支付密码，隐藏字段
	private Double balance;	//余额

	public Privacy() {
		super();
	}

	public Privacy(long id) {
		this();
		setId(id);
	}

	public Privacy(String phone, String password) {
		this();
		setPhone(phone);
		setPassword(password);
	}



	public String getPhone() {
		return phone;
	}
	public Privacy setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	/**get_password会转为password
	 * @return
	 */
	public String get__password() {
		return password;
	}
	public Privacy setPassword(String password) {
		this.password = password;
		return this;
	}

	/**get_PayPassword会转为PayPassword
	 * @return
	 */
	public String get__payPassword() {
		return payPassword;
	}
	public Privacy setPayPassword(String payPassword) {
		this.payPassword = payPassword;
		return this;
	}

	public Double getBalance() {
		return balance;
	}
	public Privacy setBalance(Double balance) {
		this.balance = balance;
		return this;
	}

}
