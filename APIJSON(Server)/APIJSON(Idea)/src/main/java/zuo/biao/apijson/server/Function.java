package zuo.biao.apijson.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.Entry;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.TypeValueKeyEntry;

/**
 * @author Lemon
 */
public class Function {
	private static final String TAG = "Function";


	public static void test() {
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

		jsonMap.put("key", "key");
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("key", 111);
		jsonMap.put("map", map);

		String function = "get(Collection:collection,int:position)";//只允许引用，不能直接传值//"plus(@i0,@i1)";

		String key = "praiseCount@function";
		if (key.endsWith("@")) {//内部引用

		} else if (key.endsWith("@function")) {//引用服务器方法
			long time0 = System.currentTimeMillis();
			System.out.println("" + time0);
			Object result = invoke(jsonMap, function);
			System.out.println("" + result);
			System.out.println("duration=" + (System.currentTimeMillis()-time0));
		}

		System.out.println("plus = " + invoke(jsonMap, "plus(long:i0,long:i1)"));
		System.out.println("count = " + invoke(jsonMap, "count(Collection:collection)"));
		System.out.println("contains = " + invoke(jsonMap, "contains(Collection:collection,Object:id)"));
		System.out.println("get = " + invoke(jsonMap, "get(Map:map,key)"));
		System.out.println("Integer:get = " + invoke(jsonMap, "Integer:get(Map:map,key)"));
	}

	/**反射调用
	 * @param jsonMap
	 * @param function 例如get(Map:map,key)，参数只允许引用，不能直接传值
	 * @return
	 */
	public static Object invoke(Map<String, Object> jsonMap, String function) {

		int start = function.indexOf("(");
		int end = function.lastIndexOf(")");
		Entry<String, String> method = TypeValueKeyEntry.parseKeyEntry(function.substring(0, start));
		if (method == null || StringUtil.isNotEmpty(method.getValue(), true) == false) {
			System.out.println(TAG + "invoke  method == null || StringUtil.isNotEmpty(method.getValue(), true) == false"
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
				entry = TypeValueKeyEntry.parseEntry(jsonMap, typeValues[i]);
				if (entry != null) {
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
	public static Object invoke(String methodName, Class<?>[] parameterTypes, Object[] args) {
		Function obj = new Function();
		Class<?> clazz = obj.getClass();
		try {
			return clazz.getDeclaredMethod(methodName, parameterTypes).invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	
	

	public static String search(String key) {
		return null;
	}

	public static long plus(long i0, long i1) {
		return i0 + i1;
	}
	
	public static String newVerify(String phone) {
		return new Controller().postAuthCode(phone);
	}


	/**判断collection是否包含object
	 * @param collection
	 * @param object
	 * @return
	 */
	public static <T> boolean contains(Collection<T> collection, T object) {
		return collection != null && collection.contains(object);
	}

	//获取集合长度 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取数量
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> int count(T[] array) {
		return array == null ? 0 : array.length;
	}
	/**获取数量
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	public static <T> int count(Collection<T> collection) {
		return collection == null ? 0 : collection.size();
	}
	/**获取数量
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> int count(Map<K, V> map) {
		return map == null ? 0 : map.size();
	}
	//获取集合长度 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//获取集合内的对象 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取集合内的对象
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> T get(T[] array, int position) {
		return position < 0 || position >= count(array) ? null : array[position];
	}
	/**获取集合内的对象
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Collection<T> collection, int position) {
		return (T) (collection == null ? null : get(collection.toArray(), position));
	}
	/**获取集合内的对象
	 * @param <K>
	 * @param <V>
	 * @param map null ? null
	 * @param key null ? null : map.get(key);
	 * @return
	 */
	public static <K, V> V get(Map<K, V> map, K key) {
		return key == null || map == null ? null : map.get(key);
	}
	//获取集合内的对象 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//获取非基本类型对应基本类型的非空值 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static boolean value(Boolean value) {
		return value == null ? false : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static int value(Integer value) {
		return value == null ? 0 : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static long value(Long value) {
		return value == null ? 0 : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static float value(Float value) {
		return value == null ? 0 : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static double value(Double value) {
		return value == null ? 0 : value;
	}
	//获取非基本类型对应基本类型的非空值 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
