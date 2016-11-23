/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson.client;

import java.util.List;
import java.util.Set;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONObject;

import com.alibaba.fastjson.JSONArray;

/**
 * TODO 格式化json，去除标记array内object位置的数字，转为[]形式，比如
 * "Comment[]":{"0":{"Comment":{...}}, ...}
 * 转为
 * "Comment[]":[{...}, ...]
 */
public class JSONResponse extends JSONObject {
	private static final long serialVersionUID = -6707531287941223427L;

	public JSONResponse() {
		super();
	}
	public JSONResponse(String json) {
		super(json);
	}
	public JSONResponse(com.alibaba.fastjson.JSONObject object) {
		super(object);
	}

	@Override
	public Object get(Object key) {
		return key instanceof String ? super.getWithDecode((String) key) : super.get(key);
	}
	public Object get(String key) {
		return getWithDecode(key);
	}



	/**
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(com.alibaba.fastjson.JSONObject object, Class<T> clazz) {
		return JSON.parseObject(JSON.toJSONString(clazz == null ? object : object.getJSONObject(clazz.getSimpleName())), clazz);
	}
//	/**
//	 * @param json
//	 * @param clazz
//	 * @return
//	 */
//	public static <T> T getObject(String json, Class<T> clazz) {
//		return getObject(JSON.parseObject(json, clazz), clazz);
//	}

	
	/**
	 * arrayObject = this
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getList(Class<T> clazz) {
		return getList(this, clazz);
	}
	/**
	 * @param arrayObject
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getList(com.alibaba.fastjson.JSONObject arrayObject, Class<T> clazz) {
		return JSON.parseArray(JSON.toJSONString(getJSONArray(arrayObject
				, clazz == null ? null : clazz.getSimpleName())), clazz);
	}

	/**
	 * @param arrayKey
	 * @param className
	 * @return
	 */
	public JSONArray getJSONArray(String arrayKey, String className) {
		if (StringUtil.isNotEmpty(arrayKey, true) == false) {
			arrayKey = StringUtil.getString(className) + "[]";
		}
		return isArrayKey(arrayKey) ? getJSONArray(getJSONObject(arrayKey), className) : super.getJSONArray(arrayKey);
	}

	/**
	 * arrayObject = this
	 * @return
	 */
	public JSONArray getJSONArray() {
		return getJSONArray(this, null);
	}
	/**
	 * arrayObject = this
	 * @return
	 */
	public JSONArray getJSONArray(String className) {
		return getJSONArray(this, className);
	}
	/**
	 * @param <T>
	 * @param arrayObject
	 * @param clazz 
	 * @return
	 */
	public static JSONArray getJSONArray(com.alibaba.fastjson.JSONObject arrayObject) {
		return getJSONArray(arrayObject, null);
	}
	/**
	 * @param <T>
	 * @param arrayObject
	 * @param clazz 
	 * @return
	 */
	public static JSONArray getJSONArray(com.alibaba.fastjson.JSONObject arrayObject, String className) {
		Set<String> set = arrayObject == null ? null : arrayObject.keySet();
		if (set == null || set.isEmpty()) {
			return null;
		}

		String parentString = StringUtil.getTrimedString(com.alibaba.fastjson.JSON.toJSONString(arrayObject));
		if (parentString.isEmpty()) {
			return null;
		}
		if (parentString.startsWith("[")) {
			if (parentString.endsWith("]") == false) {
				parentString += "]";
			}
			return JSON.parseArray(parentString);
		}

		//{"0":{...}, "1":{...}...}

		className = StringUtil.getTrimedString(className);

		JSONArray array = new JSONArray(set.size());
		com.alibaba.fastjson.JSONObject value;
		int index;
		for (String key : set) {//0, 1, 2,...
			value = StringUtil.isNumer(key) == false ? null : arrayObject.getJSONObject(key);
			if (value != null) {
				try {
					index = Integer.valueOf(0 + key);
					array.set(index, className.isEmpty() ? value : value.getJSONObject(className));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return array;
	}


}
