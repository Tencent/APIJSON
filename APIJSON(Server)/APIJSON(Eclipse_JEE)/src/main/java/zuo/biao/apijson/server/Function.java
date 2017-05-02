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

package zuo.biao.apijson.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.BaseModel;
import zuo.biao.apijson.FunctionList;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.StringUtil;

/**
 * @author Lemon
 */
public class Function implements FunctionList {
	private static final String TAG = "Function";


	public static void test() throws Exception {
		int i0 = 1, i1 = -2;
		Map<String, Object> jsonMap = new HashMap<String, Object>(); 
		jsonMap.put("id", 10);
		jsonMap.put("i0", i0);
		jsonMap.put("i1", i1);
		JSONArray arr = new JSONArray();
		arr.add(new JSONObject());
		jsonMap.put("arr", arr);

		JSONArray collection = new JSONArray();
		collection.add(1);//new JSONObject());
		collection.add(2);//new JSONObject());
		collection.add(4);//new JSONObject());
		collection.add(10);//new JSONObject());
		jsonMap.put("collection", collection);

		jsonMap.put("position", 1);
		jsonMap.put("@position", 0);

		jsonMap.put("key", "key");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("key", true);
		jsonMap.put("map", map);

		String function = "get(Collection:collection,int:position)";//只允许引用，不能直接传值//"plus(@i0,@i1)";

		String key = "praiseCount@function";
		if (key.endsWith("@")) {//内部引用

		} else if (key.endsWith("@function")) {//引用服务器方法
			long time0 = System.currentTimeMillis();
			Log.i(TAG, "" + time0);
			Object result = invoke(jsonMap, function);
			Log.i(TAG, "" + result);
			Log.i(TAG, "duration=" + (System.currentTimeMillis()-time0));
		}

		Log.i(TAG, "plus = " + invoke(jsonMap, "plus(long:i0,long:i1)"));
		Log.i(TAG, "count = " + invoke(jsonMap, "count(Collection:collection)"));
		Log.i(TAG, "isContain = " + invoke(jsonMap, "isContain(Collection:collection,Object:id)"));
		Log.i(TAG, "get(Map:map,key) = " + invoke(jsonMap, "get(Map:map,key)"));
		Log.i(TAG, "get(Collection:collection,int:@position) = " + invoke(jsonMap, "get(Collection:collection,int:@position)"));
		Log.i(TAG, "Integer:get(Map:map,key) = " + invoke(jsonMap, "Integer:get(Map:map,key)"));
	}

	/**反射调用
	 * @param jsonMap
	 * @param function 例如get(Map:map,key)，参数只允许引用，不能直接传值
	 * @return
	 */
	public static Object invoke(Map<String, Object> jsonMap, String function) throws Exception {

		int start = function.indexOf("(");
		int end = function.lastIndexOf(")");
		Entry<String, String> method = Pair.parseVariableEntry(function.substring(0, start));
		if (method == null || StringUtil.isNotEmpty(method.getValue(), true) == false) {
			Log.i(TAG, "invoke  method == null || StringUtil.isNotEmpty(method.getValue(), true) == false"
					+ " >> return null;");
			return null;
		}

		String[] typeValues = StringUtil.split(function.substring(start + 1, end));

		Class<?>[] types = null;
		Object[] values = null;
		if (typeValues != null && typeValues.length > 0) {
			types = new Class<?>[typeValues.length];
			values = new Object[typeValues.length];

			Entry<Class<?>, Object> entry;
			for (int i = 0; i < typeValues.length; i++) {
				entry = Pair.parseVariableEntry(typeValues[i], jsonMap);
				if (entry != null && entry.isEmpty() == false) {
					types[i] = entry.getKey();
					values[i] = entry.getValue();
				}
			}
		}
		return invoke(method.getValue(), types, values); 
	}
	/**反射调用
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static Object invoke(String methodName, Class<?>[] parameterTypes, Object[] args) throws Exception {
		Function obj = new Function();
		Class<?> clazz = obj.getClass();
		return clazz.getDeclaredMethod(methodName, parameterTypes).invoke(obj, args);
	}




	public String search(String key) {
		return null;
	}

	public long plus(long i0, long i1) {
		return i0 + i1;
	}

	public String newVerify(String phone) {
		return new Controller().postAuthCode(phone);
	}


	//判断是否为空 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**判断collection是否为空
	 * @param collection
	 * @return
	 */
	@Override
	public <T> boolean isEmpty(Collection<T> collection) {
		return BaseModel.isEmpty(collection);
	}
	/**判断map是否为空
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	@Override
	public <K, V> boolean isEmpty(Map<K, V> map) {
		return BaseModel.isEmpty(map); 
	}
	//判断是否为空 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//判断是否为包含 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**判断collection是否包含object
	 * @param collection
	 * @param object
	 * @return
	 */
	@Override
	public <T> boolean isContain(Collection<T> collection, T object) {
		return BaseModel.isContain(collection, object);
	}
	/**判断map是否包含key
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	@Override
	public <K, V> boolean isContainKey(Map<K, V> map, K key) { 
		return BaseModel.isContainKey(map, key); 
	}
	/**判断map是否包含value
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param value
	 * @return
	 */
	@Override
	public <K, V> boolean isContainValue(Map<K, V> map, V value) { 
		return BaseModel.isContainValue(map, value);
	}
	//判断是否为包含 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//获取集合长度 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取数量
	 * @param <T>
	 * @param array
	 * @return
	 */
	@Override
	public <T> int count(T[] array) {  
		return BaseModel.count(array); 
	}
	/**获取数量
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	@Override
	public <T> int count(Collection<T> collection) { 
		return BaseModel.count(collection); 
	}
	/**获取数量
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	@Override
	public <K, V> int count(Map<K, V> map) {
		return BaseModel.count(map); 
	}
	//获取集合长度 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//获取集合长度 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取
	 * @param <T>
	 * @param array
	 * @return
	 */
	@Override
	public <T> T get(T[] array, int position) { 
		return BaseModel.get(array, position);
	}
	/**获取
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	@Override
	public <T> T get(Collection<T> collection, int position) { 
		return BaseModel.get(collection, position); 
	}
	/**获取
	 * @param <K>
	 * @param <V>
	 * @param map null ? null
	 * @param key null ? null : map.get(key);
	 * @return
	 */
	@Override
	public <K, V> V get(Map<K, V> map, K key) { 
		return BaseModel.get(map, key);
	}
	//获取集合长度 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//获取非基本类型对应基本类型的非空值 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取非空值
	 * @param value
	 * @return
	 */
	@Override
	public boolean value(Boolean value) { 
		return BaseModel.value(value); 
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	@Override
	public int value(Integer value) {  
		return BaseModel.value(value); 
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	@Override
	public long value(Long value) {   
		return BaseModel.value(value); 
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	@Override
	public float value(Float value) {  
		return BaseModel.value(value); 
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	@Override
	public double value(Double value) {    
		return BaseModel.value(value);
	}
	//获取非基本类型对应基本类型的非空值 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}