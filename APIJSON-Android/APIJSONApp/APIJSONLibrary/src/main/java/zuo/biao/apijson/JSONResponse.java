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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Set;

/**parser for response
 * @author Lemon
 * @see #getObject
 * @see #getList
 * @use JSONResponse response = new JSONResponse(json);
 * <br> User user = response.getObject(User.class);//not a must
 * <br> List<Comment> commenntList = response.getList("Comment[]", Comment.class);//not a must
 */
public class JSONResponse extends zuo.biao.apijson.JSONObject {
	private static final long serialVersionUID = 1L;

	private static final String TAG = "JSONResponse";

	public JSONResponse() {
		super();
	}
	public JSONResponse(String json) {
		this(parseObject(json));
	}
	public JSONResponse(JSONObject object) {
		super(format(object));
	}

	//状态信息，非GET请求获得的信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final int CODE_SUCCESS = 200; //成功
	public static final int CODE_UNSUPPORTED_ENCODING = 400; //编码错误
	public static final int CODE_ILLEGAL_ACCESS = 401; //权限错误
	public static final int CODE_UNSUPPORTED_OPERATION = 403; //禁止操作
	public static final int CODE_NOT_FOUND = 404; //未找到
	public static final int CODE_ILLEGAL_ARGUMENT = 406; //参数错误
	public static final int CODE_NOT_LOGGED_IN = 407; //未登录
	public static final int CODE_TIME_OUT = 408; //超时
	public static final int CODE_CONFLICT = 409; //重复，已存在
	public static final int CODE_CONDITION_ERROR = 412; //条件错误，如密码错误
	public static final int CODE_UNSUPPORTED_TYPE = 415; //类型错误
	public static final int CODE_OUT_OF_RANGE = 416; //超出范围
	public static final int CODE_NULL_POINTER = 417; //对象为空
	public static final int CODE_SERVER_ERROR = 500; //服务器内部错误


	public static final String MSG_SUCCEED = "success"; //成功
	public static final String MSG_SERVER_ERROR = "Internal Server Error!"; //服务器内部错误


	public static final String KEY_CODE = "code";
	public static final String KEY_MSG = "msg";
	public static final String KEY_ID = "id";
	public static final String KEY_ID_IN = KEY_ID + "{}";
	public static final String KEY_COUNT = "count";
	public static final String KEY_TOTAL = "total";

	/**获取状态
	 * @return
	 */
	public int getCode() {
		try {
			return getIntValue(KEY_CODE);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取信息
	 * @return
	 */
	public String getMsg() {
		return getString(KEY_MSG);
	}
	/**获取id
	 * @return
	 */
	public long getId() {
		try {
			return getLongValue(KEY_ID);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取数量
	 * @return
	 */
	public int getCount() {
		try {
			return getIntValue(KEY_COUNT);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取总数
	 * @return
	 */
	public int getTotal() {
		try {
			return getIntValue(KEY_TOTAL);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}


	/**是否成功
	 * @return
	 */
	public boolean isSuccess() {
		return isSuccess(getCode());
	}
	/**是否成功
	 * @param code
	 * @return
	 */
	public static boolean isSuccess(int code) {
		return code == CODE_SUCCESS;
	}
	/**是否成功
	 * @param response
	 * @return
	 */
	public static boolean isSuccess(JSONResponse response) {
		return response != null && response.isSuccess();
	}

	/**校验服务端是否存在table
	 * @return
	 */
	public boolean isExist() {
		return isExist(getCount());
	}
	/**校验服务端是否存在table
	 * @param count
	 * @return
	 */
	public static boolean isExist(int count) {
		return count > 0;
	}
	/**校验服务端是否存在table
	 * @param response
	 * @return
	 */
	public static boolean isExist(JSONResponse response) {
		return response != null && response.isExist();
	}

	/**获取内部的JSONResponse
	 * @param key
	 * @return
	 */
	public JSONResponse getJSONResponse(String key) {
		return getObject(key, JSONResponse.class);
	}
	//状态信息，非GET请求获得的信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>






	/**
	 * key = clazz.getSimpleName()
	 * @param clazz
	 * @return
	 */
	public <T> T getObject(Class<T> clazz) {
		return getObject(clazz == null ? "" : clazz.getSimpleName(), clazz);
	}
	/**
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObject(String key, Class<T> clazz) {
		return getObject(this, key, clazz);
	}
	/**
	 * @param object
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(JSONObject object, String key, Class<T> clazz) {
		return toObject(object == null ? null : object.getJSONObject(key), clazz);
	}

	/**
	 * @param clazz
	 * @return
	 */
	public <T> T toObject(Class<T> clazz) {
		return toObject(this, clazz);
	}
	/**
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(JSONObject object, Class<T> clazz) {
		return JSON.parseObject(JSON.toJSONString(object), clazz);
	}




	/**
	 * key = KEY_ARRAY
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getList(Class<T> clazz) {
		return getList(KEY_ARRAY, clazz);
	}
	/**
	 * arrayObject = this
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> clazz) {
		return getList(this, key, clazz);
	}

	/**
	 * key = KEY_ARRAY
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getList(JSONObject object, Class<T> clazz) {
		return getList(object, KEY_ARRAY, clazz);
	}
	/**
	 * @param object
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getList(JSONObject object, String key, Class<T> clazz) {
		return object == null ? null : JSON.parseArray(object.getString(replaceArray(key)), clazz);
	}

	/**
	 * key = KEY_ARRAY
	 * @return
	 */
	public JSONArray getArray() {
		return getArray(KEY_ARRAY);
	}
	/**
	 * @param key
	 * @return
	 */
	public JSONArray getArray(String key) {
		return getArray(this, key);
	}
	/**
	 * @param object
	 * @return
	 */
	public static JSONArray getArray(JSONObject object) {
		return getArray(object, KEY_ARRAY);
	}
	/**
	 * key = KEY_ARRAY
	 * @param object
	 * @param key
	 * @return
	 */
	public static JSONArray getArray(JSONObject object, String key) {
		return object == null ? null : object.getJSONArray(replaceArray(key));
	}


	//	/**
	//	 * @return
	//	 */
	//	public JSONObject format() {
	//		return format(this);
	//	}
	/**格式化key名称
	 * @param object
	 * @return
	 */
	public static JSONObject format(final JSONObject object) {
		//太长查看不方便，不如debug	 Log.i(TAG, "format  object = \n" + JSON.toJSONString(object));
		if (object == null || object.isEmpty()) {
			Log.i(TAG, "format  object == null || object.isEmpty() >> return object;");
			return object;
		}
		JSONObject formatted = new JSONObject(true);

		Set<String> set = object.keySet();
		if (set != null) {

			Object value;
			for (String key : set) {
				value = object.get(key);

				if (value instanceof JSONArray) {//JSONArray，遍历来format内部项
					formatted.put(replaceArray(key), format((JSONArray) value));
				}
				else if (value instanceof JSONObject) {//JSONObject，往下一级提取
					formatted.put(getSimpleName(key), format((JSONObject) value));
				}
				else {//其它Object，直接填充
					formatted.put(getSimpleName(key), value);
				}
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return formatted = " + JSON.toJSONString(formatted));
		return formatted;
	}

	/**格式化key名称
	 * @param array
	 * @return
	 */
	public static JSONArray format(final JSONArray array) {
		//太长查看不方便，不如debug	 Log.i(TAG, "format  array = \n" + JSON.toJSONString(array));
		if (array == null || array.isEmpty()) {
			Log.i(TAG, "format  array == null || array.isEmpty() >> return array;");
			return array;
		}
		JSONArray formatted = new JSONArray();

		Object value;
		for (int i = 0; i < array.size(); i++) {
			value = array.get(i);
			if (value instanceof JSONArray) {//JSONArray，遍历来format内部项
				formatted.add(format((JSONArray) value));
			}
			else if (value instanceof JSONObject) {//JSONObject，往下一级提取
				formatted.add(format((JSONObject) value));
			}
			else {//其它Object，直接填充
				formatted.add(value);
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return formatted = " + JSON.toJSONString(formatted));
		return formatted;
	}

	/**替换key+KEY_ARRAY为keyList
	 * @param key
	 * @return getSimpleName(isArrayKey(key) ? getArrayKey(...) : key) {@link #getSimpleName(String)}
	 */
	public static String replaceArray(String key) {
		if (isArrayKey(key)) {
			key = getArrayKey(key.substring(0, key.lastIndexOf(KEY_ARRAY)));
		}
		return getSimpleName(key);
	}
	/**获取列表变量名
	 * @param key => StringUtil.getNoBlankString(key)
	 * @return empty ? "list" : key + "List" 且首字母小写
	 */
	public static String getArrayKey(String key) {
		return StringUtil.addSuffix(key, "list");
	}

	/**获取简单名称
	 * @param fullName name 或 name:alias
	 * @return name => name; name:alias => alias
	 */
	public static String getSimpleName(String fullName) {
		//key:alias  -> alias; key:alias[] -> alias[]
		int index = fullName == null ? -1 : fullName.indexOf(":");
		if (index >= 0) {
			fullName = fullName.substring(index + 1);
		}
		return fullName;
	}


}
