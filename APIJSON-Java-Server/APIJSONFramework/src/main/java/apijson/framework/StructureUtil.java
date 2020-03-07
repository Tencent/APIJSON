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

package apijson.framework;

import static apijson.framework.APIJSONConstant.REQUEST_;

import java.rmi.ServerException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.JSON;
import apijson.JSONResponse;
import apijson.Log;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.JSONRequest;
import apijson.orm.ParserCreator;
import apijson.orm.SQLCreator;
import apijson.orm.Structure;


/**请求结构校验
 * @author Lemon
 */
public class StructureUtil {
	public static final String TAG = "StructureUtil";

	//根据 version 动态从数据库查的  version{}:">=$currentVersion"，所以静态缓存暂时没用   public static final Map<String, JSONObject> REQUEST_MAP;
	static {
		//		REQUEST_MAP = new HashMap<>();
	}

	/**初始化，加载所有请求校验配置
	 * @return 
	 * @throws ServerException
	 */
	public static JSONObject init() throws ServerException {
		return init(false);
	}
	/**初始化，加载所有请求校验配置
	 * @param shutdownWhenServerError 
	 * @return 
	 * @throws ServerException
	 */
	public static JSONObject init(boolean shutdownWhenServerError) throws ServerException {
		return init(shutdownWhenServerError, null);
	}
	/**初始化，加载所有请求校验配置
	 * @param creator 
	 * @return 
	 * @throws ServerException
	 */
	public static JSONObject init(ParserCreator<Long> creator) throws ServerException {
		return init(false, creator);
	}
	/**初始化，加载所有请求校验配置
	 * @param shutdownWhenServerError 
	 * @param creator 
	 * @return 
	 * @throws ServerException
	 */
	public static JSONObject init(boolean shutdownWhenServerError, ParserCreator<Long> creator) throws ServerException {
		JSONRequest request = new JSONRequest();

		{   //Request[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			JSONRequest requestItem = new JSONRequest();

			{   //Request<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
				requestItem.put(REQUEST_, new JSONRequest());
			}   //Request>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			request.putAll(requestItem.toArray(0, 0, REQUEST_));

		}   //Request[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


		if (creator == null) {
			creator = APIJSONApplication.DEFAULT_APIJSON_CREATOR;
		}

		JSONObject response = creator.createParser().setMethod(RequestMethod.GET).setNoVerify(true).parseResponse(request);
		if (JSONResponse.isSuccess(response) == false) {
			Log.e(TAG, "\n\n\n\n\n !!!! 查询权限配置异常 !!!\n" + response.getString(JSONResponse.KEY_MSG) + "\n\n\n\n\n");
			onServerError("查询权限配置异常 !", shutdownWhenServerError);
		}

		JSONArray list = response.getJSONArray(REQUEST_ + "[]");
		if (list == null || list.isEmpty()) {
			Log.w(TAG, "init list == null || list.isEmpty()，没有可用的权限配置");
			throw new NullPointerException("没有可用的权限配置");
		}

		//		Log.d(TAG, "init < for REQUEST_MAP.size() = " + REQUEST_MAP.size() + " <<<<<<<<<<<<<<<<<<<<<<<<");

		//		REQUEST_MAP.clear();

		JSONObject item;
		for (int i = 0; i < list.size(); i++) {
			item = list.getJSONObject(i);
			if (item == null) {
				continue;
			}

			String version = item.getString("version");
			if (StringUtil.isEmpty(version, true)) {
				Log.e(TAG, "init  for  StringUtil.isEmpty(version, true)，Request 表中的 version 不能为空！");
				onServerError("服务器内部错误，Request 表中的 version 不能为空！", shutdownWhenServerError);
			}

			String method = item.getString("method");
			if (StringUtil.isEmpty(method, true)) {
				Log.e(TAG, "init  for  StringUtil.isEmpty(method, true)，Request 表中的 method 不能为空！");
				onServerError("服务器内部错误，Request 表中的 method 不能为空！", shutdownWhenServerError);
			}

			String tag = item.getString("tag");
			if (StringUtil.isEmpty(tag, true)) {
				Log.e(TAG, "init  for  StringUtil.isEmpty(tag, true)，Request 表中的 tag 不能为空！");
				onServerError("服务器内部错误，Request 表中的 tag 不能为空！", shutdownWhenServerError);
			}

			JSONObject structure = JSON.parseObject(item.getString("structure"));


			JSONObject target = null;

			if (structure != null) {
				if (apijson.JSONObject.isTableKey(tag) && structure.containsKey(tag) == false) {//tag是table名
					target = new JSONObject(true);
					target.put(tag, structure);
				} else {
					target = structure;
				}
			}

			if (target == null || target.isEmpty()) {
				Log.e(TAG, "init  for  target == null || target.isEmpty()");
				onServerError("服务器内部错误，Request 表中的 version = " + version + ", method = " + method + ", tag = " + tag +  " 对应的 structure 不能为空！", shutdownWhenServerError);
			}

			//			REQUEST_MAP.put(tag, target);
		}

		//		Log.d(TAG, "init  for /> REQUEST_MAP.size() = " + REQUEST_MAP.size() + " >>>>>>>>>>>>>>>>>>>>>>>");

		return response;
	}

	private static void onServerError(String msg, boolean shutdown) throws ServerException {
		Log.e(TAG, "\n请求校验配置文档测试未通过！\n请修改 Request 表里的记录！\n保证前端看到的请求校验配置文档是正确的！！！\n\n原因：\n" + msg);

		if (shutdown) {
			System.exit(1);	
		} else {
			throw new ServerException(msg);
		}
	}


	static final String requestString = "{\"Comment\":{\"DISALLOW\": \"id\", \"NECESSARY\": \"userId,momentId,content\"}, \"ADD\":{\"Comment:to\":{}}}";
	static final String responseString = "{\"User\":{\"REMOVE\": \"phone\", \"REPLACE\":{\"sex\":2}, \"ADD\":{\"name\":\"api\"}}, \"PUT\":{\"Comment:to\":{}}}";
	/**测试
	 * @throws Exception
	 */
	public static void test() throws Exception {
		test(null);
	}
	/**测试
	 * @throws Exception
	 */
	public static void test(SQLCreator creator) throws Exception {
		JSONObject request;

		if (creator == null) {
			creator = APIJSONApplication.DEFAULT_APIJSON_CREATOR;
		}

		try {
			request = JSON.parseObject("{\"Comment\":{\"userId\":0}}");
			Log.d(TAG, "test  parseRequest = " + Structure.parseRequest(RequestMethod.POST, "", JSON.parseObject(requestString), request, creator));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			request = JSON.parseObject("{\"Comment\":{\"userId\":0, \"momentId\":0, \"content\":\"apijson\"}}");
			Log.d(TAG, "test  parseRequest = " + Structure.parseRequest(RequestMethod.POST, "", JSON.parseObject(requestString), request, creator));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			request = JSON.parseObject("{\"Comment\":{\"id\":0, \"userId\":0, \"momentId\":0, \"content\":\"apijson\"}}");
			Log.d(TAG, "test  parseRequest = " + Structure.parseRequest(RequestMethod.POST, "", JSON.parseObject(requestString), request, creator));
		} catch (Exception e) {
			e.printStackTrace();
		}


		JSONObject response;
		try {
			response = JSON.parseObject("{\"User\":{\"userId\":0}}");
			Log.d(TAG, "test  parseResponse = " + Structure.parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response, creator, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			response = JSON.parseObject("{\"User\":{\"userId\":0, \"phone\":\"12345678\"}}");
			Log.d(TAG, "test  parseResponse = " + Structure.parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response, creator, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			response = JSON.parseObject("{\"User\":{\"userId\":0, \"phone\":\"12345678\", \"sex\":1}}");
			Log.d(TAG, "test  parseResponse = " + Structure.parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response, creator, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			response = JSON.parseObject("{\"User\":{\"id\":0, \"name\":\"tommy\", \"phone\":\"12345678\", \"sex\":1}}");
			Log.d(TAG, "test  parseResponse = " + Structure.parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response,creator, null));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}




}
