package apijson.demo.server;

import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.HEADS;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;

import com.jfinal.kit.HttpKit;

import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.server.JSONRequest;

public class Controller extends com.jfinal.core.Controller {

	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**获取
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	public void get() {
		renderJson(new DemoParser(GET).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**计数
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	public void head() {
		renderJson(new DemoParser(HEAD).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**限制性GET，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	public void gets() {
		renderJson(new DemoParser(GETS).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**限制性HEAD，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	public void heads() {
		renderJson(new DemoParser(HEADS).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**新增
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	public void post() {
		renderJson(new DemoParser(POST).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**修改
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	public void put() {
		renderJson(new DemoParser(PUT).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	public void delete() {
		renderJson(new DemoParser(DELETE).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}


	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	

	public static final String USER_;
	public static final String PRIVACY_;
	public static final String VERIFY_; //加下划线后缀是为了避免 Verify 和 verify 都叫VERIFY，分不清
	static {
		USER_ = User.class.getSimpleName();
		PRIVACY_ = Privacy.class.getSimpleName();
		VERIFY_ = Verify.class.getSimpleName();
	}

	public static final String VERSION = JSONRequest.KEY_VERSION;
	public static final String FORMAT = JSONRequest.KEY_FORMAT;
	public static final String COUNT = JSONResponse.KEY_COUNT;
	public static final String TOTAL = JSONResponse.KEY_TOTAL;

	public static final String ID = "id";
	public static final String USER_ID = "userId";
	public static final String CURRENT_USER_ID = "currentUserId";

	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String PASSWORD = "password";
	public static final String _PASSWORD = "_password";
	public static final String _PAY_PASSWORD = "_payPassword";
	public static final String OLD_PASSWORD = "oldPassword";
	public static final String VERIFY = "verify";

	public static final String TYPE = "type";

}
