package apijson.demo.server;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import apijson.demo.server.config.ConfigYml;
import com.alibaba.fastjson.JSONObject;
import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import org.springframework.stereotype.Component;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.AbstractSQLConfig;
import zuo.biao.apijson.server.Join;
import zuo.biao.apijson.server.SQLConfig;


/**SQL配置
 * @author Lemon
 */
@Component
public class DemoSQLConfig extends AbstractSQLConfig {
	//表名映射，隐藏真实表名，对安全要求很高的表可以这么做
	static {
		TABLE_KEY_MAP.put(User.class.getSimpleName(), "apijson_user");
		TABLE_KEY_MAP.put(Privacy.class.getSimpleName(), "apijson_privacy");
	}

	ConfigYml dataSourceConfig = new ConfigYml();
	Map dataSource = dataSourceConfig.read();

	@Override
	public String getDBUri() {
		//TODO 改成你自己的
		return dataSource.get("url")+"";
	}
	@Override
	public String getDBAccount() {
		return dataSource.get("username")+"";
	}
	@Override
	public String getDBPassword() {
		return dataSource.get("password")+"";
	}
	@Override
	public String getSchema() {
		String s = super.getSchema();
		return StringUtil.isEmpty(s, true) ? dataSource.get("schema")+"" : s; //TODO 改成你自己的
	}
	
	@Override
	public String getAlias() { //getTable 不能小写，因为Verifier用大小写敏感的名称判断权限
		return super.getAlias();
	}
	

	public DemoSQLConfig() throws FileNotFoundException {
		this(RequestMethod.GET);
	}
	public DemoSQLConfig(RequestMethod method) throws FileNotFoundException {
		super(method);
	}
	public DemoSQLConfig(RequestMethod method, String table) throws FileNotFoundException {
		super(method, table);
	}
	public DemoSQLConfig(RequestMethod method, int count, int page) throws FileNotFoundException {
		super(method, count, page);
	}


	/**获取SQL配置
	 * @param table
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static SQLConfig newSQLConfig(RequestMethod method, String table, JSONObject request, List<Join> joinList) throws Exception {
		return newSQLConfig(method, table, request, joinList, new Callback() {

			@Override
			public DemoSQLConfig getSQLConfig(RequestMethod method, String table) {
				try {
					return new DemoSQLConfig(method, table);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}


}
