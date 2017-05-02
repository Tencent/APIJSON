/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson;

import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.Parser;
import zuo.biao.apijson.server.sql.QueryConfig;
import zuo.biao.apijson.server.sql.QueryHelper;

/**结构验证类
 * @author Lemon
 */
public class StructureVerifier {
	private static final String TAG = "StructureVerifier";
	
	public static final int TYPE_COMMON = 0;
	public static final int TYPE_REQUEST = 1;
	public static final int TYPE_RESPONSE = 2;
	
	private int type;//TODO request,response

	public StructureVerifier(int type) {
		this.type = type;
	}
	
	
	
	
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getCorrectRequest(RequestMethod method, JSONObject request) throws Exception {
		return getCorrectRequest(method, request, null);
	}
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @param queryHelper
	 * @return
	 */
	public static JSONObject getCorrectRequest(RequestMethod method, JSONObject request, QueryHelper queryHelper)
			throws Exception {
		if (Parser.isPublicMethod(method)) {
			return request;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		String tag = request.getString(JSONRequest.KEY_TAG);
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new IllegalArgumentException("请指定tag！一般是table名称");
		}

		//获取指定的JSON结构 <<<<<<<<<<<<<<
		QueryConfig config = new QueryConfig(GET, "Request");
		config.setColumn("structure");

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("method", method.name());
		where.put(JSONRequest.KEY_TAG, tag);
		config.setWhere(where);

		JSONObject object = null;
		String error = "";
		if (queryHelper == null) {
			queryHelper = new QueryHelper();
		}
		try {
			object = queryHelper.select(config);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
		}
		queryHelper.close();

		if (object == null) {//empty表示随意操作  || object.isEmpty()) {
			throw new UnsupportedOperationException("非GET请求必须是服务端允许的操作！ \n " + error);
		}
		object = Parser.getJSONObject(object, "structure");//解决返回值套了一层 "structure":{}

		JSONObject target = null;
		if (Parser.isTableKey(tag) && object.containsKey(tag) == false) {//tag是table名
			target = new JSONObject(true);
			target.put(tag, object);
		} else {
			target = object;
		}
		//获取指定的JSON结构 >>>>>>>>>>>>>>

		request.remove(JSONRequest.KEY_TAG);
		return fillTarget(method, target, request, "");
	}


	public static final String NECESSARY_COLUMNS = "necessaryColumns";
	public static final String DISALLOW_COLUMNS = "disallowColumns";

	/**从request提取target指定的内容
	 * @param target
	 * @param request
	 * @return
	 */
	public static JSONObject fillTarget(RequestMethod method
			, JSONObject target, final JSONObject request, String requestName) throws Exception {
		Log.i(TAG, "filterTarget  requestName = " + requestName
				+ " target = \n" + JSON.toJSONString(target)
				+ "\n request = \n" + JSON.toJSONString(request) + "\n >> return null;");
		if (target == null || request == null) {// || request.isEmpty()) {
			Log.i(TAG, "filterTarget  target == null || request == null >> return null;");
			return null;
		}

		/**方法三：遍历request，transferredRequest只添加target所包含的object
		 *  ，且移除target中DISALLOW_COLUMNS，期间判断NECESSARY_COLUMNS是否都有
		 */
		String necessarys = StringUtil.getNoBlankString(target.getString(NECESSARY_COLUMNS));
		String[] necessaryColumns = StringUtil.split(necessarys);

		//判断必要字段是否都有
		if (necessaryColumns != null) {
			for (String s : necessaryColumns) {
				if (request.containsKey(s) == false) {
					throw new IllegalArgumentException(requestName
							+ "不能缺少 " + s + " 等[" + necessarys + "]内的任何字段！");
				}
			}
		}

		String disallows = StringUtil.getNoBlankString(target.getString(DISALLOW_COLUMNS));
		String[] disallowColumns = null;

		Set<String> set = request.keySet();
		if ("!".equals(disallows)) {//所有非necessaryColumns，改成 !necessary 更好
			if (set != null) {
				List<String> disallowList = new ArrayList<String>();
				for (String key : set) {
					if (isContainKeyInArray(key, necessaryColumns) == false) {
						disallowList.add(key);
					}
				}
				disallowColumns = disallowList.toArray(new String[]{});
			}
		} else {
			disallowColumns = StringUtil.split(disallows);
		}


		//填充target
		JSONObject transferredRequest = new JSONObject(true);
		if (set != null) {
			Object value;
			JSONObject result;
			for (String key : set) {
				value = request.get(key);
				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					if (target.containsKey(key)) {//只填充target有的object
						result = fillTarget(method, Parser.getJSONObject(target, key), (JSONObject) value, key);//往下一级提取
						Log.i(TAG, "fillTarget  key = " + key + "; result = " + result);
						if (result == null || result.isEmpty()) {//只添加!=null的值，可能数据库返回数据不够count
							throw new IllegalArgumentException(requestName
									+ "不能缺少 " + key + " 等[" + necessarys + "]内的任何JSONObject！");
						}
						if (method == POST && result.containsKey(QueryConfig.ID) == false) {//为注册用户返回id
							result.put(QueryConfig.ID, System.currentTimeMillis());
						}
						transferredRequest.put(key, result);
					}
				} else {//JSONArray或其它Object
					if (isContainKeyInArray(key, disallowColumns)) {
						throw new IllegalArgumentException(requestName
								+ "不允许传 " + key + " 等[" + disallows + "]内的任何字段！");
					}
					transferredRequest.put(key, value);
				}
			}
		}

		Log.i(TAG, "filterTarget  return transferredRequest = " + JSON.toJSONString(transferredRequest));
		return transferredRequest;
	}

	/**array至少有一个值在request的key中
	 * @param key
	 * @param array
	 * @return
	 */
	public static boolean isContainKeyInArray(String key, String[] array) {
		if (array == null || array.length <= 0 || key == null) {
			Log.i(TAG, "isContainKeyInArray"
					+ "  array == null || array.length <= 0 || key == null >> return false;");
			return false;
		}

		for (String s : array) {
			if (key.equals(s)) {
				return true;
			}
		}

		return false;
	}




	
}
