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

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
	/**获取状态
	 * @return
	 */
	public static int getCode(JSONObject reponse) {
		try {
			return reponse.getIntValue(KEY_CODE);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取状态描述
	 * @return
	 */
	public String getMsg() {
		return getString(KEY_MSG);
	}
	/**获取状态描述
	 * @param reponse
	 * @return
	 */
	public static String getMsg(JSONObject reponse) {
		return reponse == null ? null : reponse.getString(KEY_MSG);
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
	/**是否成功
	 * @param response
	 * @return
	 */
	public static boolean isSuccess(JSONObject response) {
		return response != null && isSuccess(response.getIntValue(KEY_CODE));
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
	//cannot get javaBeanDeserizer
	//	/**获取内部的JSONResponse
	//	 * @param response
	//	 * @param key
	//	 * @return
	//	 */
	//	public static JSONResponse getJSONResponse(JSONObject response, String key) {
	//		return response == null ? null : response.getObject(key, JSONResponse.class);
	//	}
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
		return toObject(object == null ? null : object.getJSONObject(formatObjectKey(key)), clazz);
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
		return object == null ? null : JSON.parseArray(object.getString(formatArrayKey(key)), clazz);
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
		return object == null ? null : object.getJSONArray(formatArrayKey(key));
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
		JSONObject formatedObject = new JSONObject(true);

		Set<String> set = object.keySet();
		if (set != null) {

			Object value;
			for (String key : set) {
				value = object.get(key);

				if (value instanceof JSONArray) {//JSONArray，遍历来format内部项
					formatedObject.put(formatArrayKey(key), format((JSONArray) value));
				}
				else if (value instanceof JSONObject) {//JSONObject，往下一级提取
					formatedObject.put(formatObjectKey(key), format((JSONObject) value));
				}
				else {//其它Object，直接填充
					formatedObject.put(formatOtherKey(key), value);
				}
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return formatedObject = " + JSON.toJSONString(formatedObject));
		return formatedObject;
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
		JSONArray formatedArray = new JSONArray();

		Object value;
		for (int i = 0; i < array.size(); i++) {
			value = array.get(i);
			if (value instanceof JSONArray) {//JSONArray，遍历来format内部项
				formatedArray.add(format((JSONArray) value));
			}
			else if (value instanceof JSONObject) {//JSONObject，往下一级提取
				formatedArray.add(format((JSONObject) value));
			}
			else {//其它Object，直接填充
				formatedArray.add(value);
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return formatedArray = " + JSON.toJSONString(formatedArray));
		return formatedArray;
	}


	/**获取表名称
	 * @param fullName name 或 name:alias
	 * @return name => name; name:alias => alias
	 */
	public static String getTableName(String fullName) {
		//key:alias  -> alias; key:alias[] -> alias[]
		int index = fullName == null ? -1 : fullName.indexOf(":");
		return index < 0 ? fullName : fullName.substring(0, index);
	}

	/**获取变量名
	 * @param fullName
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean)} formatColon = true, formatAt = true, formatHyphen = true, firstCase = true
	 */
	public static String getVariableName(String fullName) {
		if (isArrayKey(fullName)) {
			fullName = StringUtil.addSuffix(fullName.substring(0, fullName.length() - 2), "list");
		}
		return formatKey(fullName, true, true, true, true);
	}

	/**格式化数组的名称 key[] => keyList; key:alias[] => aliasList; Table-column[] => tableColumnList
	 * @param key empty ? "list" : key + "List" 且首字母小写
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean)} formatColon = false, formatAt = true, formatHyphen = true, firstCase = true
	 */
	public static String formatArrayKey(String key) {
		if (isArrayKey(key)) {
			key = StringUtil.addSuffix(key.substring(0, key.length() - 2), "list");
		}
		int index = key == null ? -1 : key.indexOf(":");
		if (index >= 0) {
			return key.substring(index + 1); //不处理自定义的
		}

		return formatKey(key, false, true, true, true); //节约性能，除了数组对象 Table-column:alias[] ，一般都符合变量命名规范
	}

	/**格式化对象的名称 name => name; name:alias => alias
	 * @param key name 或 name:alias
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean)} formatColon = false, formatAt = true, formatHyphen = false, firstCase = true
	 */
	public static String formatObjectKey(String key) {
		int index = key == null ? -1 : key.indexOf(":");
		if (index >= 0) {
			return key.substring(index + 1); //不处理自定义的
		}

		return formatKey(key, false, true, false, true); //节约性能，除了表对象 Table:alias ，一般都符合变量命名规范
	}

	/**格式化普通值的名称 name => name; name:alias => alias
	 * @param fullName name 或 name:alias
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean)} formatColon = false, formatAt = true, formatHyphen = false, firstCase = false
	 */
	public static String formatOtherKey(String fullName) {
		return formatKey(fullName, false, true, false, false); //节约性能，除了关键词 @key ，一般都符合变量命名规范，不符合也原样返回便于调试
	}


	/**格式化名称
	 * @param fullName name 或 name:alias
	 * @param formatAt 去除前缀 @ ， @a => a
	 * @param formatColon 去除分隔符 : ， A:b => b
	 * @param formatHyphen 去除分隔符 - ， A-b-cd-Efg => aBCdEfg
	 * @param firstCase 第一个单词首字母小写，后面的首字母大写， Ab => ab ; A-b-Cd => aBCd
	 * @return name => name; name:alias => alias
	 */
	public static String formatKey(String fullName, boolean formatColon, boolean formatAt, boolean formatHyphen, boolean firstCase) {
		if (fullName == null) {
			Log.w(TAG, "formatKey  fullName == null >> return null;");
			return null;
		}

		if (formatColon) {
			fullName = formatColon(fullName);
		}
		if (formatAt) { //关键词只去掉前缀，不格式化单词，例如 @a-b 返回 a-b ，最后不会调用 setter
			fullName = formatAt(fullName);
		}
		if (formatHyphen) {
			fullName = formatHyphen(fullName, firstCase);
		}

		return firstCase ? StringUtil.firstCase(fullName) : fullName; //不格式化普通 key:value (value 不为 [], {}) 的 key
	}

	/**"@key" => "key"
	 * @param key
	 * @return
	 */
	public static String formatAt(@NotNull String key) {
		return key.startsWith("@") ? key.substring(1) : key;
	}
	/**key:alias => alias
	 * @param key
	 * @return
	 */
	public static String formatColon(@NotNull String key) {
		int index = key.indexOf(":");
		return index < 0 ? key : key.substring(index + 1);
	}

	/**A-b-cd-Efg => ABCdEfg
	 * @param key
	 * @return
	 */
	public static String formatHyphen(@NotNull String key, boolean firstCase) {
		boolean first = true;
		int index;

		String name = "";
		String part;
		do {
			index = key.indexOf("-");
			part = index < 0 ? key : key.substring(0, index);

			name += firstCase && first == false ? StringUtil.firstCase(part, true) : part;
			key = key.substring(index + 1);

			first = false;
		}
		while (index >= 0);

		return name;
	}


}
