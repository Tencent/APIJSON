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

import static zuo.biao.apijson.StringUtil.bigAlphaPattern;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**parser for response JSON String
 * @author Lemon
 * @see #getList
 * @see #toArray
 * @use JSONResponse response = new JSONResponse(...);
 * <br> JSONArray array = JSONResponse.toArray(response.getJSONObject(KEY_ARRAY));//not a must
 * <br> User user = JSONResponse.getObject(response, User.class);//not a must
 * <br> List<Comment> list = JSONResponse.getList(response.getJSONObject("Comment[]"), Comment.class);//not a must
 */
@SuppressWarnings("serial")
public class JSONResponse extends zuo.biao.apijson.JSONObject {
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

	public static final int STATUS_SUCCEED = 200;


	public static final String KEY_ID = "id";
	public static final String KEY_STATUS = "status";
	public static final String KEY_COUNT = "count";
	public static final String KEY_TOTAL = "total";
	public static final String KEY_MESSAGE = "message";

	/**获取id
	 * @return
	 */
	public long getId() {
		return getLongValue(KEY_ID);
	}
	/**获取状态
	 * @return
	 */
	public int getStatus() {
		return getIntValue(KEY_STATUS);
	}
	/**获取数量
	 * @return
	 */
	public int getCount() {
		return getIntValue(KEY_COUNT);
	}
	/**获取数量
	 * @return
	 */
	public int getTotal() {
		try {
			return getIntValue(KEY_TOTAL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	/**获取信息
	 * @return
	 */
	public String getMessage() {
		return getString(KEY_MESSAGE);
	}

	/**是否成功
	 * @return
	 */
	public boolean isSucceed() {
		return isSucceed(getStatus());
	}
	/**是否成功
	 * @param status
	 * @return
	 */
	public static boolean isSucceed(int status) {
		return status == STATUS_SUCCEED;
	}
	/**是否成功
	 * @param response
	 * @return
	 */
	public static boolean isSucceed(JSONResponse response) {
		return response != null && response.isSucceed();
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
		Object obj = object == null ? null : object.get(replaceArray(key));
		if (obj == null) {
			return null;
		}
		return obj instanceof JSONArray ? JSON.parseArray((JSONArray) obj, clazz) : toList((JSONObject) obj, clazz);
	}
	/**
	 * @param clazz
	 * @return
	 */
	public <T> List<T> toList(Class<T> clazz) {
		return toList(this, clazz);
	}
	/**
	 * @param arrayObject
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> toList(JSONObject arrayObject, Class<T> clazz) {
		return clazz == null ? null : JSON.parseArray(JSON.toJSONString(
				toArray(arrayObject, clazz.getSimpleName())), clazz);
	}

	/**
	 * key = KEY_ARRAY
	 * @param className
	 * @return
	 */
	public JSONArray getArray(String className) {
		return getArray(KEY_ARRAY, className);
	}
	/**
	 * @param key
	 * @param className
	 * @return
	 */
	public JSONArray getArray(String key, String className) {
		return getArray(this, key, className);
	}
	/**
	 * @param object
	 * @param key
	 * @param className
	 * @return
	 */
	public static JSONArray getArray(JSONObject object, String className) {
		return getArray(object, KEY_ARRAY, className);
	}
	/**
	 * key = KEY_ARRAY
	 * @param object
	 * @param className
	 * @return
	 */
	public static JSONArray getArray(JSONObject object, String key, String className) {
		Object obj = object == null ? null : object.get(replaceArray(key));
		if (obj == null) {
			return null;
		}
		return obj instanceof JSONArray ? (JSONArray) obj : toArray((JSONObject) obj, className);
	}

	/**
	 * @param className
	 * @return
	 */
	public JSONArray toArray(String className) {
		return toArray(this, className);
	}
	/**{0:{Table:{}}, 1:{Table:{}}...} 转化为 [{Table:{}}, {Table:{}}]
	 * array.set(index, isContainer ? value : value.getJSONObject(className));
	 * @param arrayObject
	 * @param className className.equals(Table) ? {Table:{Content}} => {Content}
	 * @return
	 */
	public static JSONArray toArray(JSONObject arrayObject, String className) {
		Set<String> set = arrayObject == null ? null : arrayObject.keySet();
		if (set == null || set.isEmpty()) {
			return null;
		}

		// [{...},{...},...]
		String parentString = StringUtil.getTrimedString(JSON.toJSONString(arrayObject));
		if (parentString.isEmpty()) {
			return null;
		}
		if (parentString.startsWith("[")) {
			if (parentString.endsWith("]") == false) {
				parentString += "]";
			}
			return JSON.parseArray(parentString);
		}

		//{"0":{Table:{...}}, "1":{Table:{...}}...}

		className = StringUtil.getTrimedString(className);
		boolean isContainer = true;

		JSONArray array = new JSONArray(set.size());
		JSONObject value;
		boolean isFirst = true;
		int index;
		for (String key : set) {//0, 1, 2,...
			value = StringUtil.isNumer(key) == false ? null : arrayObject.getJSONObject(key);// Table:{}
			if (value != null) {
				try {
					index = Integer.valueOf(0 + key);
					if (isFirst && isTableKey(className) && value.containsKey(className)) {// 判断是否需要提取table
						isContainer = false;
					}
					array.set(index, isContainer ? value : value.getJSONObject(className));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			isFirst = false;
		}
		return array;
	}



	//	/**
	//	 * @return
	//	 */
	//	public JSONObject format() {
	//		return format(this);
	//	}
	/**将Item[]:[{Table:{}}, {Table:{}}...] 或 Item[]:{0:{Table:{}}, 1:{Table:{}}...}
	 *  转化为 itemList:[{Table:{}}, {Table:{}}]，如果 Item.equals(Table)，则将 {Table:{Content}} 转化为 {Content}
	 * @param target
	 * @param response
	 * @return
	 */
	public static JSONObject format(final JSONObject response) {
		//太长查看不方便，不如debug	 Log.i(TAG, "format  response = \n" + JSON.toJSONString(response));
		if (response == null || response.isEmpty()) {
			Log.i(TAG, "format  response == null || response.isEmpty() >> return response;");
			return response;
		}
		JSONObject transferredObject = new JSONObject(true);

		Set<String> set = response.keySet();
		if (set != null) {

			Object value;
			String arrayKey;
			for (String key : set) {
				value = response.get(key);

				if (value instanceof JSONArray) {//转化JSONArray内部的APIJSON Array
					transferredObject.put(replaceArray(key), format(key, (JSONArray) value));
				} else if (value instanceof JSONObject) {//APIJSON Array转为常规JSONArray
					if (isArrayKey(key)) {//APIJSON Array转为常规JSONArray
						arrayKey = key.substring(0, key.lastIndexOf(KEY_ARRAY));
						transferredObject.put(getArrayKey(getSimpleName(arrayKey))
								, format(key, toArray((JSONObject) value, arrayKey)));//需要将name:alias传至toArray
					} else {//常规JSONObject，往下一级提取
						transferredObject.put(getSimpleName(key), format((JSONObject) value));
					}
				} else {//其它Object，直接填充
					transferredObject.put(getSimpleName(key), value);
				}
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return transferredObject = " + JSON.toJSONString(transferredObject));
		return transferredObject;
	}

	/**
	 * @param responseArray
	 * @return
	 */
	public static JSONArray format(String name, final JSONArray responseArray) {
		//太长查看不方便，不如debug	 Log.i(TAG, "format  responseArray = \n" + JSON.toJSONString(responseArray));
		if (responseArray == null || responseArray.isEmpty()) {
			Log.i(TAG, "format  responseArray == null || responseArray.isEmpty() >> return response;");
			return responseArray;
		}
		int index = name == null ? -1 : name.lastIndexOf(KEY_ARRAY);
		String className = index < 0 ? "" : name.substring(0, index);

		JSONArray transferredArray = new JSONArray();

		Object value;
		boolean isContainer = true;
		boolean isFirst = true;
		for (int i = 0; i < responseArray.size(); i++) {
			value = responseArray.get(i);
			if (value instanceof JSONArray) {//转化JSONArray内部的APIJSON Array
				transferredArray.add(format(null, (JSONArray) value));
			} else if (value instanceof JSONObject) {//JSONObject，往下一级提取
				//判断是否需要提取child
				if (isFirst && isTableKey(className) && ((JSONObject) value).containsKey(className)) {
					isContainer = false;
				}
				//直接添加child 或 添加提取出的child
				transferredArray.add(format(isContainer ? (JSONObject)value : ((JSONObject)value).getJSONObject(className) ));
				isFirst = false;
			} else {//其它Object，直接填充
				transferredArray.add(responseArray.get(i));
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return transferredArray = " + JSON.toJSONString(transferredArray));
		return transferredArray;
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
		key = StringUtil.getNoBlankString(key);
		if (key.isEmpty()) {
			return "list";
		}

		String first = key.substring(0, 1);
		if (bigAlphaPattern.matcher(first).matches()) {
			key = first.toLowerCase() + key.substring(1, key.length());
		}
		return key + "List";
	}

	/**获取简单名称
	 * @param fullName name 或 name:alias
	 * @return name > name; name:alias > alias
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
