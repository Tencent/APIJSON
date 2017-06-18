package apijson.demo.server.model;

import static zuo.biao.apijson.RequestRole.ADMIN;
import static zuo.biao.apijson.RequestRole.OWNER;
import static zuo.biao.apijson.RequestRole.UNKNOWN;

import zuo.biao.apijson.MethodAccess;

/**会员隐私信息表
 * @author Lemon
 */
@MethodAccess(
		GET = {},
		POST_GET = {OWNER, ADMIN},
		POST = {UNKNOWN, ADMIN},
		DELETE = {ADMIN}
		)
public class UserPrivacy extends BaseModel {
	private static final long serialVersionUID = 1L;

	private String phone; //手机
	private String password; //登录密码，隐藏字段
	private String payPassword; //支付密码，隐藏字段
	private Double balance;	//余额

	public UserPrivacy() {
		super();
	}

	public UserPrivacy(long id) {
		this();
		setId(id);
	}

	public UserPrivacy(String phone, String password) {
		this();
		setPhone(phone);
		setPassword(password);
	}



	public String getPhone() {
		return phone;
	}
	public UserPrivacy setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	/**get_password会转为password
	 * @return
	 */
	public String get__password() {
		return password;
	}
	public UserPrivacy setPassword(String password) {
		this.password = password;
		return this;
	}

	/**get_PayPassword会转为PayPassword
	 * @return
	 */
	public String get__payPassword() {
		return payPassword;
	}
	public UserPrivacy setPayPassword(String payPassword) {
		this.payPassword = payPassword;
		return this;
	}

	public Double getBalance() {
		return balance;
	}
	public UserPrivacy setBalance(Double balance) {
		this.balance = balance;
		return this;
	}

}
