/*Copyright ©2020 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package unitauto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**方法/函数的工具类
 * @author Lemon
 */
public class MethodUtil {


	/**非null注解
	 * javax.validation.constraints.NotNull不在JDK里面，为了减少第三方库引用就在这里实现了一个替代品
	 * @author Lemon
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	public @interface NotNull {
	}

	public interface Listener<T> {
		void complete(T data, Method method, InterfaceProxy proxy, Object... extras) throws Exception;
		default void complete(T data) throws Exception {
			complete(data, null, null);
		}
	}


	public interface Callback {
		JSONObject newSuccessResult();
		JSONObject newErrorResult(Exception e);
	}


	public interface ClassLoaderCallback {
		List<Class<?>> loadClassList(String packageOrFileName, String className, boolean ignoreError) throws ClassNotFoundException, IOException;
		Class<?> loadClass(String className) throws ClassNotFoundException;
	}

	public static String KEY_CODE = "code";
	public static String KEY_MSG = "msg";

	public static int CODE_SUCCESS = 200;
	public static int CODE_SERVER_ERROR = 500;
	public static String MSG_SUCCESS = "success";


	public static String KEY_TIME = "time";
	public static String KEY_PACKAGE = "package";
	public static String KEY_CLASS = "class";
	public static String KEY_TYPE = "type";
	public static String KEY_VALUE = "value";
	public static String KEY_STATIC = "static";
	public static String KEY_NAME = "name";
	public static String KEY_METHOD = "method";
	public static String KEY_RETURN = "return";
	public static String KEY_CLASS_ARGS = "classArgs";
	public static String KEY_METHOD_ARGS = "methodArgs";
	public static String KEY_CALLBACK = "callback";

	public static String KEY_CALL_LIST = "call()[]";
	public static String KEY_CALL_MAP = "call(){}";


	public static ClassLoaderCallback CLASS_LOADER_CALLBACK = null;  //不能在 static 代码块赋值，否则 MethodUtil 子类中 static 代码块对它赋值的代码不会执行！
	public static Callback CALLBACK = new Callback() {  //不能在 static 代码块赋值，否则 MethodUtil 子类中 static 代码块对它赋值的代码不会执行！

		@Override
		public JSONObject newSuccessResult() {
			JSONObject result = new JSONObject(true);
			result.put(KEY_CODE, CODE_SUCCESS);
			result.put(KEY_MSG, MSG_SUCCESS);
			return result;
		}

		@Override
		public JSONObject newErrorResult(Exception e) {
			JSONObject result = new JSONObject(true);
			result.put(KEY_CODE, CODE_SERVER_ERROR);
			result.put(KEY_MSG, e.getMessage());
			return result;
		}
	};

	//  Map<class, <constructorArgs, instance>>
	public static final Map<Class<?>, Map<Object, Object>> INSTANCE_MAP;
	public static final Map<String, Class<?>> PRIMITIVE_CLASS_MAP;
	public static final Map<String, Class<?>> BASE_CLASS_MAP;
	public static final Map<String, Class<?>> CLASS_MAP;
	static {
		INSTANCE_MAP = new HashMap<>();

		PRIMITIVE_CLASS_MAP = new HashMap<String, Class<?>>();
		BASE_CLASS_MAP = new HashMap<String, Class<?>>();
		CLASS_MAP = new HashMap<String, Class<?>>();

		PRIMITIVE_CLASS_MAP.put(boolean.class.getSimpleName(), boolean.class);
		PRIMITIVE_CLASS_MAP.put(int.class.getSimpleName(), int.class);
		PRIMITIVE_CLASS_MAP.put(long.class.getSimpleName(), long.class);
		PRIMITIVE_CLASS_MAP.put(float.class.getSimpleName(), float.class);
		PRIMITIVE_CLASS_MAP.put(double.class.getSimpleName(), double.class);
		BASE_CLASS_MAP.putAll(PRIMITIVE_CLASS_MAP);

		BASE_CLASS_MAP.put(Boolean.class.getSimpleName(), Boolean.class);
		BASE_CLASS_MAP.put(Integer.class.getSimpleName(), Integer.class);
		BASE_CLASS_MAP.put(Long.class.getSimpleName(), Long.class);
		BASE_CLASS_MAP.put(Float.class.getSimpleName(), Float.class);
		BASE_CLASS_MAP.put(Double.class.getSimpleName(), Double.class);
		BASE_CLASS_MAP.put(Number.class.getSimpleName(), Number.class);
		BASE_CLASS_MAP.put(String.class.getSimpleName(), String.class);
		BASE_CLASS_MAP.put(Object.class.getSimpleName(), Object.class);
		CLASS_MAP.putAll(BASE_CLASS_MAP);

		CLASS_MAP.put(boolean[].class.getSimpleName(), boolean[].class);
		CLASS_MAP.put(int[].class.getSimpleName(), int[].class);
		CLASS_MAP.put(long[].class.getSimpleName(), long[].class);
		CLASS_MAP.put(float[].class.getSimpleName(), float[].class);
		CLASS_MAP.put(double[].class.getSimpleName(), double[].class);
		CLASS_MAP.put(Array.class.getSimpleName(), Array.class);
		CLASS_MAP.put(Boolean[].class.getSimpleName(), Boolean[].class);
		CLASS_MAP.put(Integer[].class.getSimpleName(), Integer[].class);
		CLASS_MAP.put(Long[].class.getSimpleName(), Long[].class);
		CLASS_MAP.put(Float[].class.getSimpleName(), Float[].class);
		CLASS_MAP.put(Double[].class.getSimpleName(), Double[].class);
		CLASS_MAP.put(Number[].class.getSimpleName(), Number[].class);
		CLASS_MAP.put(String[].class.getSimpleName(), String[].class);
		CLASS_MAP.put(Object[].class.getSimpleName(), Object[].class);
		CLASS_MAP.put(Array[].class.getSimpleName(), Array[].class);

		CLASS_MAP.put(Collection.class.getSimpleName(), Collection.class);//不允许指定<T>
		CLASS_MAP.put(List.class.getSimpleName(), List.class);//不允许指定<T>
		CLASS_MAP.put(ArrayList.class.getSimpleName(), ArrayList.class);//不允许指定<T>
		CLASS_MAP.put(Map.class.getSimpleName(), Map.class);//不允许指定<T>
		CLASS_MAP.put(HashMap.class.getSimpleName(), HashMap.class);//不允许指定<T>
		CLASS_MAP.put(Set.class.getSimpleName(), Set.class);//不允许指定<T>
		CLASS_MAP.put(HashSet.class.getSimpleName(), HashSet.class);//不允许指定<T>

		CLASS_MAP.put(JSON.class.getSimpleName(), JSON.class);//必须有，Map中没有getLongValue等方法
		CLASS_MAP.put(JSONObject.class.getSimpleName(), JSONObject.class);//必须有，Map中没有getLongValue等方法
		CLASS_MAP.put(JSONArray.class.getSimpleName(), JSONArray.class);//必须有，Collection中没有getJSONObject等方法
	}



	/**获取方法列表
	 * @param request :
	 {
		"package": "apijson.demo.server",
		"class": "DemoFunction",
		"method": "plus",
		"types": ["Integer", "String", "com.alibaba.fastjson.JSONObject"]
		//不返回的话，这个接口没意义		    "return": true,  //返回 class list，方便调试
	 }
	 * @return
	 */
	public static JSONObject listMethod(String request) {
		JSONObject result;

		try {
			JSONObject req = JSON.parseObject(request);
			if (req  == null) {
				req = new JSONObject(true);
			}
			String pkgName = req.getString(KEY_PACKAGE);
			String clsName = req.getString(KEY_CLASS);
			String methodName = req.getString(KEY_METHOD);
			JSONArray methodArgTypes = null;

			boolean allMethod = isEmpty(methodName, true);

			Class<?>[] argTypes = null;
			if (allMethod == false) {
				methodArgTypes = req.getJSONArray("types");
				if (methodArgTypes != null && methodArgTypes.isEmpty() == false) {
					argTypes = new Class<?>[methodArgTypes.size()];

					for (int i = 0; i < methodArgTypes.size(); i++) {
						argTypes[i] = getType(methodArgTypes.getString(i), null, true);
					}
				}
			}

			JSONArray list = getMethodListGroupByClass(pkgName, clsName, methodName, argTypes);
			result = CALLBACK.newSuccessResult();
			result.put("classList", list);  //序列化 Class	只能拿到 name		result.put("Class[]", JSON.parseArray(JSON.toJSONString(classlist)));
		} catch (Exception e) {
			e.printStackTrace();
			result = CALLBACK.newErrorResult(e);
		}

		return result;
	}



	/**执行方法
	 * @param request
	 * @param instance
	 * @return {@link #invokeMethod(JSONObject, Object, Listener<JSONObject>)}
	 * @throws Exception
	 */
	public static void invokeMethod(String request, Object instance, Listener<JSONObject> listener) throws Exception {
		invokeMethod(JSON.parseObject(request), instance, listener);
	}
	/**执行方法
	 * @param req :
	 {
		"package": "apijson.demo.server",
		"class": "DemoFunction",
		"classArgs": [
			null,
			null,
			0,
			null
		],
		"method": "plus",
		"methodArgs": [
			{
				"type": "Integer",  //可缺省，自动根据 value 来判断
				"value": 1
			},
			{
				"type": "String",
				"value": "APIJSON"
			},
			{
				"type": "JSONObject",  //可缺省，JSONObject 已缓存到 CLASS_MAP
				"value": {}
			},
			{
				"type": "apijson.demo.server.model.User",  //不可缺省，且必须全称
				"value": {
					"id": 1,
					"name": "Tommy"
				}
			}
		]
	 }
	 * @param instance 默认自动 new，传非 null 值一般是因为 Spring 自动注入的 Service, Component, Mapper 等不能自己 new
	 * @return
	 * @throws Exception
	 */
	public static void invokeMethod(JSONObject req, Object instance, @NotNull Listener<JSONObject> listener) throws Exception {
		if (req == null) {
			req = new JSONObject(true);
		}
		String pkgName = req.getString(KEY_PACKAGE);
		String clsName = req.getString(KEY_CLASS);
		String methodName = req.getString(KEY_METHOD);

		try {
			Class<?> clazz = getInvokeClass(pkgName, clsName);
			if (clazz == null) {
				throw new ClassNotFoundException("找不到 " + dot2Separator(pkgName) + "/" + clsName + " 对应的类！");
			}

			if (instance == null && req.getBooleanValue(KEY_STATIC) == false) {
				instance = getInvokeInstance(clazz, getArgList(req, KEY_CLASS_ARGS));
			}

			Object finalInstance = instance;

			getInvokeResult(clazz, instance, methodName, getArgList(req, KEY_METHOD_ARGS), new Listener<JSONObject>() {

				@Override
				public void complete(JSONObject data, Method method, InterfaceProxy proxy, Object... extras) throws Exception {
					JSONObject result = CALLBACK.newSuccessResult();
					if (data != null) {
						result.putAll(data);
					}
					result.put("instance", finalInstance); //TODO InterfaceProxy proxy 改成泛型 I instance ？

					listener.complete(result);
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
			if (e instanceof NoSuchMethodException) {
				e = new IllegalArgumentException("字符 " + methodName + " 对应的方法不在 " + pkgName +  "/" + clsName + " 内！"
						+ "\n请检查函数名和参数数量是否与已定义的函数一致！\n" + e.getMessage());
			}
			if (e instanceof InvocationTargetException) {
				Throwable te = ((InvocationTargetException) e).getTargetException();
				if (isEmpty(te.getMessage(), true) == false) { //到处把函数声明throws Exception改成throws Throwable挺麻烦
					e = te instanceof Exception ? (Exception) te : new Exception(te.getMessage());
				}
				e = new IllegalArgumentException("字符 " + methodName + " 对应的方法传参类型错误！"
						+ "\n请检查 key:value 中value的类型是否满足已定义的函数的要求！\n" + e.getMessage());
			}
			JSONObject result = CALLBACK.newErrorResult(e);
			result.put("throw", e.getClass().getTypeName());
			result.put("cause", e.getCause());
			result.put("trace", e.getStackTrace());
			listener.complete(result);
		}

	}


	public static List<Argument> getArgList(JSONObject req, String arrKey) {
		JSONArray arr = req == null ? null : JSON.parseArray(req.getString(arrKey));

		List<Argument> list = null;
		if (arr != null && arr.isEmpty() == false) {
			list = new ArrayList<>();
			for (Object item : arr) {
				if (item instanceof Boolean || item instanceof Number || item instanceof Collection) {
					list.add(new Argument(null, item));
				}
				else if (item instanceof String) {
					String str = (String) item;
					int index = str.indexOf(":");
					String type = index < 0 ? null : str.substring(0, index);
					String value = index < 0 ? str : str.substring(index + 1);
					list.add(new Argument(type, value));
				}
				else { //null 合法，也要加，按顺序调用的
					list.add(item == null ? null : JSON.parseObject(JSON.toJSONString(item), Argument.class));
				}
			}
		}
		return list;
	}



	/**获取类
	 * @param pkgName
	 * @param clsName
	 * @return
	 * @throws Exception
	 */
	public static Class<?> getInvokeClass(String pkgName, String clsName) throws Exception {
		return findClass(pkgName, clsName, false);
	}

	/**获取示例
	 * @param clazz
	 * @param classArgs
	 * @return
	 * @throws Exception
	 */
	public static Object getInvokeInstance(@NotNull Class<?> clazz, List<Argument> classArgs) throws Exception {
		Objects.requireNonNull(clazz);

		//new 出实例
		Map<Object, Object> clsMap = INSTANCE_MAP.get(clazz);
		if (clsMap == null) {
			clsMap = new HashMap<>();
			INSTANCE_MAP.put(clazz, clsMap);
		}

		String key = classArgs == null || classArgs.isEmpty() ? "" : JSON.toJSONString(classArgs);
		Object instance = clsMap.get(key);  //必须精确对应值，否则去除缓存的和需要的很可能不符

		if (instance == null) {
			if (classArgs == null || classArgs.isEmpty()) {
				instance = clazz.newInstance();
			}
			else { //通过构造方法
				boolean exactContructor = false;  //指定某个构造方法，只要某一项 type 不为空就是
				for (int i = 0; i < classArgs.size(); i++) {
					Argument obj = classArgs.get(i);
					if (obj != null && isEmpty(obj.getType(), true) == false) {
						exactContructor = true;
						break;
					}
				}

				Class<?>[] classArgTypes = new Class<?>[classArgs.size()];
				Object[] classArgValues = new Object[classArgs.size()];
				initTypesAndValues(classArgs, classArgTypes, classArgValues, exactContructor);

				if (exactContructor) {  //指定某个构造方法
					Constructor<?> constructor = clazz.getConstructor(classArgTypes);
					instance = constructor.newInstance(classArgValues);
				}
				else {  //尝试参数数量一致的构造方法
					Constructor<?>[] constructors = clazz.getConstructors();
					if (constructors != null) {
						for (int i = 0; i < constructors.length; i++) {
							if (constructors[i] != null && constructors[i].getParameterCount() == classArgValues.length) {
								try {
									instance = constructors[i].newInstance(classArgValues);
									break;
								}
								catch (Exception e) {}
							}
						}
					}
				}

			}

			if (instance == null) { //通过默认方法
				throw new NullPointerException("找不到 " + dot2Separator(clazz.getName()) + " 以及 classArgs 对应的构造方法！");
			}

			clsMap.put(key, instance);
		}

		return instance;
	}

	/**获取方法
	 * @param clazz
	 * @param methodName
	 * @param methodArgs
	 * @return
	 * @throws Exception
	 */
	public static Method getInvokeMethod(@NotNull Class<?> clazz, @NotNull String methodName, List<Argument> methodArgs) throws Exception {
		Objects.requireNonNull(clazz);
		Objects.requireNonNull(methodName);

		//method argument, types and values
		Class<?>[] types = null;
		Object[] args = null;

		if (methodArgs != null && methodArgs.isEmpty() == false) {
			types = new Class<?>[methodArgs.size()];
			args = new Object[methodArgs.size()];
			initTypesAndValues(methodArgs, types, args, true);
		}

		return clazz.getMethod(methodName, types);
	}

	/**执行方法并返回结果
	 * @param instance
	 * @param methodName
	 * @param methodArgs
	 * @param listener
	 * @return
	 * @throws Exception
	 */
	public static void getInvokeResult(@NotNull Class<?> clazz, Object instance, @NotNull String methodName
			, List<Argument> methodArgs, @NotNull Listener<JSONObject> listener) throws Exception {

		Objects.requireNonNull(clazz);
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(listener);

		int size = methodArgs == null ? 0 : methodArgs.size();
		boolean isEmpty = size <= 0;

		//method argument, types and values
		Class<?>[] types = isEmpty ? null : new Class<?>[size];
		Object[] args = isEmpty ? null : new Object[size];

		if (isEmpty == false) {
			initTypesAndValues(methodArgs, types, args, true, false);
		}

		Method method = clazz.getMethod(methodName, types);

		Listener<Object> itemListener = new Listener<Object>() {

			@Override
			public void complete(Object data, Method m, InterfaceProxy proxy, Object... extra) throws Exception {
				JSONObject result = new JSONObject(true);
				result.put(KEY_TYPE, method.getReturnType());  //给 UnitAuto 共享用的 trimType(val.getClass()));
				result.put(KEY_RETURN, data);

				List<JSONObject> finalMethodArgs = null;
				if (types != null) {
					finalMethodArgs = new ArrayList<>();

					for (int i = 0; i < types.length; i++) {
						Class<?> t = types[i];
						Object v = args[i];
						//无效	if (v != null && v.getClass() == InterfaceProxy.class) { // v instanceof InterfaceProxy) { // v.getClass().isInterface()) {

						try {  //解决只有 interface getter 方法才有对应字段返回
							if (t.isArray() || Collection.class.isAssignableFrom(t) || GenericArrayType.class.isAssignableFrom(t)) {
								if (t.getComponentType() != null && t.getComponentType().isInterface()) {
									v = JSON.parseArray(v.toString());
								}
							}
							else if (t.isInterface()) {
								v = parseObject(v.toString());
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}

						JSONObject o = new JSONObject(true);
						o.put(KEY_TYPE, t.toGenericString());
						o.put(KEY_VALUE, v);

						finalMethodArgs.add(o);
					}
				}

				result.put(KEY_METHOD_ARGS, finalMethodArgs);

				listener.complete(result);
			}
		};

		boolean isSync = true;
		for (int i = 0; i < types.length; i++) {  //当其中有 interface 且用 KEY_CALLBACK 标记了内部至少一个方法，则认为是触发异步回调的方法
			Class<?> type = types[i];
			Object value = args[i];

			if (value instanceof InterfaceProxy || (type != null && type.isInterface())) {  //如果这里不行，就 initTypesAndValues 给个回调
				try {
					InterfaceProxy proxy = value instanceof InterfaceProxy ? ((InterfaceProxy) value) : TypeUtils.cast(value, InterfaceProxy.class, new ParserConfig());
					Set<Entry<String, Object>> set = proxy.entrySet();
					if (set != null)  {
						for (Entry<String, Object> e : set) {
							//判断是否符合 "fun(arg0,arg1...)": { "callback": true } 格式
							String key = e == null ? null : e.getKey();
							JSONObject val = key != null && e.getValue() instanceof JSONObject ? ((JSONObject) e.getValue()) : null;

							int index = val == null || key.endsWith(")") == false ? -1 : key.indexOf("(");

							if (index > 0 && StringUtil.isName(key.substring(0, index))) {

								if (val.getBooleanValue(KEY_CALLBACK)) {
									proxy.$_putCallback(key, itemListener);
								}
							}
						}
					}

					args[i] = TypeUtils.cast(proxy, type, new ParserConfig());
					isSync = proxy.$_getCallbackMap().isEmpty();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		Object val = method.invoke(instance, args);

		if (isSync) {
			itemListener.complete(val);
		}

	}


	/**获取用 Class 分组的 Method 二级嵌套列表
	 * @param pkgName
	 * @param clsName
	 * @param methodName
	 * @param argTypes
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getMethodListGroupByClass(String pkgName, String clsName
			, String methodName, Class<?>[] argTypes) throws Exception {

		boolean allMethod = isEmpty(methodName, true);

		List<Class<?>> classList = findClassList(pkgName, clsName, true);
		JSONArray list = null;
		if (classList != null) {
			list = new JSONArray(classList.size());

			for (Class<?> cls : classList) {
				if (cls == null) {
					continue;
				}

				JSONObject clsObj = new JSONObject(true);

				clsObj.put(KEY_NAME, cls.getSimpleName());
				clsObj.put(KEY_TYPE, trimType(cls.getGenericSuperclass()));
				clsObj.put(KEY_PACKAGE, dot2Separator(cls.getPackage().getName()));

				JSONArray methodList = null;
				if (allMethod == false && argTypes != null && argTypes.length > 0) {
					Object mObj = parseMethodObject(cls.getMethod(methodName, argTypes));
					if (mObj != null) {
						methodList = new JSONArray(1);
						methodList.add(mObj);
					}
				}
				else {
					Method[] methods = cls.getDeclaredMethods(); //父类的就用父类去获取 cls.getMethods();
					if (methods != null && methods.length > 0) {
						methodList = new JSONArray(methods.length);

						for (Method m : methods) {
							if (m == null) {
								continue;
							}
							if (allMethod || methodName.equals(m.getName())) {
								methodList.add(parseMethodObject(m));
							}
						}
					}
				}
				clsObj.put("methodList", methodList);  //太多不需要的信息，导致后端返回慢、前端卡 UI	clsObj.put("Method[]", JSON.parseArray(methods));

				list.add(clsObj);
			}

		}

		return list;
	}



	public static String dot2Separator(String name) {
		return name == null ? null : name.replaceAll("\\.", File.separator);
	}

	public static String separator2dot(String name) {
		return name == null ? null : name.replaceAll(File.separator, ".");
	}

	//	private void initTypesAndValues(JSONArray methodArgs, Class<?>[] types, Object[] args)
	//			throws IllegalArgumentException, ClassNotFoundException, IOException {
	//		initTypesAndValues(methodArgs, types, args, false);
	//	}

	public static void initTypesAndValues(List<Argument> methodArgs, Class<?>[] types, Object[] args, boolean defaultType)
			throws IllegalArgumentException, ClassNotFoundException {
		initTypesAndValues(methodArgs, types, args, defaultType, true);
	}
	public static void initTypesAndValues(List<Argument> methodArgs, Class<?>[] types, Object[] args, boolean defaultType, boolean castValue2Type)
			throws IllegalArgumentException, ClassNotFoundException {
		initTypesAndValues(methodArgs, types, args, defaultType, castValue2Type, null);
	}
	public static void initTypesAndValues(List<Argument> methodArgs, Class<?>[] types, Object[] args, boolean defaultType, boolean castValue2Type, Listener<Object> listener)
			throws IllegalArgumentException, ClassNotFoundException {
		if (methodArgs == null || methodArgs.isEmpty()) {
			return;
		}
		if (types == null || args == null) {
			throw new IllegalArgumentException("types == null || args == null !");
		}
		if (types.length != methodArgs.size() || args.length != methodArgs.size()) {
			throw new IllegalArgumentException("methodArgs.isEmpty() || types.length != methodArgs.size() || args.length != methodArgs.size() !");
		}

		Argument argObj;

		String typeName;
		Class<?> type;
		Object value;
		for (int i = 0; i < methodArgs.size(); i++) {
			argObj = methodArgs.get(i);

			typeName = argObj == null ? null : argObj.getType();
			value = argObj == null ? null : argObj.getValue();

			//			if (typeName != null && value != null && value.getClass().equals(CLASS_MAP.get(typeName)) == false) {
			////				if ("double".equals(typeName)) {
			//				value = TypeUtils.cast(value, CLASS_MAP.get(typeName), new ParserConfig());
			////				}
			////				else if (PRIMITIVE_CLASS_MAP.containsKey(typeName)) {
			////					value = JSON.parse(JSON.toJSONString(value));
			////				} else {
			////					value = JSON.parseObject(JSON.toJSONString(value), Class.forName(typeName));
			////				}
			//			}

			type = getType(typeName, value, defaultType);

			if (value != null && type != null && value.getClass().equals(type) == false) {
				try {  //解决只有 interface getter 方法才有对应字段返回
					if (type.isArray() || Collection.class.isAssignableFrom(type) || GenericArrayType.class.isAssignableFrom(type)) {
						if (type.getComponentType() != null && type.getComponentType().isInterface()) {
							List<InterfaceProxy> implList = JSON.parseArray(JSON.toJSONString(value), InterfaceProxy.class);
							value = implList;
						}
					}
					else if (type.isInterface()) {
						InterfaceProxy proxy = JSON.parseObject(JSON.toJSONString(value), InterfaceProxy.class);
						proxy.$_setType(type);
						value = proxy;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (castValue2Type) {
					try {
						value = TypeUtils.cast(value, type, new ParserConfig());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			types[i] = type;
			args[i] = value;
		}
	}

	public static JSONObject parseMethodObject(Method m) {
		if (m == null) {
			return null;
		}
		//排除 private 和 protected 等访问不到的方法，以后可以通过 IDE 插件为这些方法新增代理方法
		/*
		  public Type $_delegate_$method(Type0 arg0, Type1 arg1...) {
		    Type returnVal = method(arg0, arg1...)
		    if (returnVal instanceof Void) {
		      return new Object[]{ watchVar0, watchVar1... }  //FIXME void 方法需要指定要观察的变量
		    }
		    return returnVal;
		  }
		 */
		int mod = m.getModifiers();
		if (Modifier.isPrivate(mod) || Modifier.isProtected(mod)) {
			return null;
		}

		JSONObject obj = new JSONObject(true);
		obj.put("name", m.getName());
		obj.put("parameterTypeList", trimTypes(m.getParameterTypes()));  //不能用泛型，会导致解析崩溃 m.getGenericParameterTypes()));
		obj.put("genericParameterTypeList", trimTypes(m.getGenericParameterTypes()));  //不能用泛型，会导致解析崩溃 m.getGenericParameterTypes()));
		obj.put("returnType", trimType(m.getReturnType()));  //不能用泛型，会导致解析崩溃m.getGenericReturnType()));
		obj.put("genericReturnType", trimType(m.getGenericReturnType()));  //不能用泛型，会导致解析崩溃m.getGenericReturnType()));
		obj.put("static", Modifier.isStatic(m.getModifiers()));
		obj.put("exceptionTypeList", trimTypes(m.getExceptionTypes()));  //不能用泛型，会导致解析崩溃m.getGenericExceptionTypes()));
		obj.put("genericExceptionTypeList", trimTypes(m.getGenericExceptionTypes()));  //不能用泛型，会导致解析崩溃m.getGenericExceptionTypes()));
		return obj;
	}

	public static String[] trimTypes(Type[] types) {
		if (types != null && types.length > 0) {
			String[] names = new String[types.length];
			for (int i = 0; i < types.length; i++) {
				names[i] = trimType(types[i]);
			}
			return names;
		}
		return null;
	}
	public static String trimType(Type type) {
		return trimType(type == null ? null : type.getTypeName());
	}
	public static String trimType(String name) {
		if (name == null || "void".equals(name)) {
			return null;
		}

		Collection<Entry<String, Class<?>>> set = CLASS_MAP.entrySet();
		for (Entry<String, Class<?>> e : set) {
			if (name.equals(e.getValue().getTypeName())) {
				return e.getKey();
			}
		}

		String child = "";
		int index;
		do {
			index = name.indexOf("<");
			if (index < 0) {
				break;
			}
			child += "<" + trimType(name.substring(index + 1, name.lastIndexOf(">"))) + ">";
			name = name.substring(0, index);
		}
		while (index >= 0);

		if (name.startsWith("java.lang.")) {
			name = name.substring("java.lang.".length());
		}
		if (name.startsWith("java.util.")) {
			name = name.substring("java.util.".length());
		}
		if (name.startsWith("com.alibaba.fastjson.")) {
			name = name.substring("com.alibaba.fastjson.".length());
		}

		return dot2Separator(name) + child;
	}


	//	private Class<?> getType(String name) throws ClassNotFoundException, IOException {
	//		return getType(name, null);
	//	}
	//	private Class<?> getType(String name, Object value) throws ClassNotFoundException, IOException {
	//		return getType(name, value, false);
	//	}
	public static Class<?> getType(String name, Object value, boolean defaultType) throws ClassNotFoundException {
		Class<?> type = null;
		if (isEmpty(name, true)) {  //根据值来自动判断
			if (value == null || defaultType == false) {
				//nothing
			}
			else {
				type = value.getClass();
			}
		}
		else {
			int index = name.indexOf("<");
			if (index >= 0) {
				name = name.substring(0, index);
			}

			type = CLASS_MAP.get(name);
			if (type == null) {
				name = dot2Separator(name);
				index = name.lastIndexOf(File.separator);
				type = findClass(index < 0 ? "" : name.substring(0, index), index < 0 ? name : name.substring(index + 1), defaultType);

				if (type != null) {
					CLASS_MAP.put(name, type);
				}
			}
		}

		if (type == null && defaultType) {
			type = Object.class;
		}

		return type;
	}

	/**
	 * 提供直接调用的方法
	 * @param packageOrFileName
	 * @param className
	 * @param ignoreError
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> findClass(String packageOrFileName, String className, boolean ignoreError) throws ClassNotFoundException {
		//根目录 Objects.requireNonNull(packageName);
		Objects.requireNonNull(className);

		//FIXME 这个方法在 jar 包里获取不到 class，主要是 ClassLoader.getResource(packageOrFileName) 取出来为 null，试了多种方法都没解决
		try {
			List<Class<?>> list = findClassList(packageOrFileName, className, ignoreError);
			Class<?> cls = list == null || list.isEmpty() ? null : list.get(0);
			if (cls != null) {
				return cls;
			}
		} catch(Exception e) { }

		int index = className.indexOf("<");
		if (index >= 0) {
			className = className.substring(0, index);
		}
		//这个方法保证在 jar 包里能正常执行
		return Class.forName(isEmpty(packageOrFileName, true) ? className : packageOrFileName.replaceAll("/", ".") + "." + className);
	}

	/**
	 * @param packageOrFileName
	 * @param className
	 * @param ignoreError
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> findClassList(String packageOrFileName, String className, boolean ignoreError) throws ClassNotFoundException, IOException {
		if (CLASS_LOADER_CALLBACK != null) {
			return CLASS_LOADER_CALLBACK.loadClassList(packageOrFileName, className, ignoreError);
		}

		List<Class<?>> list = new ArrayList<>();

		int index = className.indexOf("<");
		if (index >= 0) {
			className = className.substring(0, index);
		}

		boolean allPackage = isEmpty(packageOrFileName, true);
		boolean allName = isEmpty(className, true);

		//将包名替换成目录  TODO 应该一层层查找进去，实时判断是 package 还是 class，如果已经是 class 还有下一级，应该用 $ 隔开内部类。简单点也可以认为大驼峰是类
		String fileName = allPackage ? File.separator : dot2Separator(packageOrFileName);

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		//通过 ClassLoader 来获取文件列表
		File file;
		try {
			file = new File(loader.getResource(fileName).getFile());
		} catch (Exception e) {
			if (ignoreError) {
				return null;
			}
			throw e;
		}

		File[] files;
		//		if (allPackage) {  //getResource 已经过滤了
		files = file.listFiles();
		//		}
		//		else {
		//			files = file.listFiles(new FilenameFilter() {
		//
		//				@Override
		//				public boolean accept(File dir, String name) {
		//					if (fileName.equals(dir.getAbsolutePath())) {
		//
		//					}
		//					return false;
		//				}
		//			});
		//		}

		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {  //如果是目录，这进一个寻找
					if (allPackage) {
						//进一步寻找
						List<Class<?>> childList = findClassList(f.getAbsolutePath(), className, ignoreError);
						if (childList != null && childList.isEmpty() == false) {
							list.addAll(childList);
						}
					}
				}
				else {  //如果是class文件
					String name = trim(f.getName());
					if (name != null && name.endsWith(".class")) {
						name = name.substring(0, name.length() - ".class".length());
						if (name.isEmpty() || name.equals("package-info") || name.contains("$")) {
							continue;
						}

						if (allName || className.equals(name)) {
							//反射出实例
							try {
								Class<?> clazz = loader.loadClass(packageOrFileName.replaceAll(File.separator, "\\.") + "." + name);
								list.add(clazz);

								if (allName == false) {
									break;
								}
							} catch (Exception e) {
								if (ignoreError == false) {
									throw e;
								}
								e.printStackTrace();
							}

						}
					}
				}
			}
		}

		return list;
	}


	/**判断字符是否为空
	 * @param s
	 * @param trim
	 * @return
	 */
	public static boolean isEmpty(String s, boolean trim) {
		//		Log.i(TAG, "isEmpty   s = " + s);
		if (s == null) {
			return true;
		}

		if (trim) {
			s = s.trim();
		}

		return s.isEmpty();
	}

	/**判断字符是否为空
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		//		Log.i(TAG, "trim   s = " + s);
		return s == null ? null : s.trim();
	}

	/**对象转有序 JSONObject
	 * @param obj
	 * @return
	 */
	public static JSONObject parseObject(Object obj) {
		return parseObject(obj instanceof String ? ((String) obj) : JSON.toJSONString(obj));
	}
	/**JSON 字符串转有序 JSONObject
	 * @param json
	 * @return
	 */
	public static JSONObject parseObject(String json) {
		return JSON.parseObject(json, JSONObject.class, JSON.DEFAULT_PARSER_FEATURE | Feature.OrderedField.getMask());
	}


	/**参数，包括类型和值
	 */
	public static class Argument {
		private String type;
		private Object value;

		public Argument() {
		}
		public Argument(String type, Object value) {
			setType(type);
			setValue(value);
		}

		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}

	/**
	 * 将 interface 转成 JSONObject，便于返回时查看
	 * TODO 应该在 JSON.parseObject(json, clazz) 时代理 clazz 内所有的 interface
	 */
	public static class InterfaceProxy extends JSONObject {
		private static final long serialVersionUID = 1L;

		public InterfaceProxy() {
			super(true);
		}
		public InterfaceProxy(int initialCapacity){
			super(initialCapacity, true);
		}

		//奇葩命名加忽略注解可以避免被 fastjson 序列化或反序列化，奇葩命名避免和代理的 interface 中的方法冲突
		private Class<?> type;
		@JSONField(serialize = false, deserialize = false)
		public Class<?> $_getType() {
			return type;
		}
		@JSONField(serialize = false, deserialize = false)
		public InterfaceProxy $_setType(Class<?> type) {
			this.type = type;
			return this;
		}


		private Map<String, Listener<?>> callbackMap = new HashMap<>();
		@NotNull
		@JSONField(serialize = false, deserialize = false)
		public Map<String, Listener<?>> $_getCallbackMap() {
			return callbackMap;
		}
		@JSONField(serialize = false, deserialize = false)
		public InterfaceProxy $_setCallbackMap(Map<String, Listener<?>> callbackMap) {
			this.callbackMap = callbackMap != null ? callbackMap : new HashMap<>();
			return this;
		}

		@JSONField(serialize = false, deserialize = false)
		public Listener<?> $_getCallback(String method) {
			return callbackMap.get(method);
		}
		@JSONField(serialize = false, deserialize = false)
		public InterfaceProxy $_putCallback(String method, Listener<?> callback) {
			callbackMap.put(method, callback);
			return this;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1) {
				Class<?> returnType = method.getReturnType();
				if (returnType != void.class) {
					//	               return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
					return onInvoke(proxy, method, args);
				}

				String name = null;
				JSONField annotation = method.getAnnotation(JSONField.class);
				if (annotation != null) {
					if (annotation.name().length() != 0) {
						name = annotation.name();
					}
				}

				if (name == null) {
					name = method.getName();

					if (!name.startsWith("set")) {
						//	 	               return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
						return onInvoke(proxy, method, args);
					}

					name = name.substring(3);
					if (name.length() == 0) {
						//	 	               return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
						return onInvoke(proxy, method, args);
					}
				}

				return onInvoke(proxy, method, args, true);
			}

			if (parameterTypes.length == 0) {
				Class<?> returnType = method.getReturnType();
				if (returnType == void.class) {
					//		               return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
					return onInvoke(proxy, method, args);
				}

				String name = null;
				JSONField annotation = method.getAnnotation(JSONField.class);
				if (annotation != null) {
					if (annotation.name().length() != 0) {
						name = annotation.name();
					}
				}

				if (name == null) {
					name = method.getName();
					if (name.startsWith("get")) {
						name = name.substring(3);
						if (name.length() == 0) {
							//	     	               return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
							return onInvoke(proxy, method, args);
						}
					} else if (name.startsWith("is")) {
						name = name.substring(2);
						if (name.length() == 0) {
							//	     	               return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
							return onInvoke(proxy, method, args);
						}
					} else if (name.startsWith("hashCode")) {
					} else if (name.startsWith("toString")) {
					} else {
						//	 	               return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
						return onInvoke(proxy, method, args);
					}
				}

				return onInvoke(proxy, method, args, true);
			}

			//            return method.invoke(this, args);  //java.lang.IllegalArgumentException: object is not an instance of declaring clas
			return onInvoke(proxy, method, args);
		}

		private Object onInvoke(Object proxy, Method method, Object[] args) throws Throwable {
			return onInvoke(proxy, method, args, false);
		}
		private Object onInvoke(Object proxy, Method method, Object[] args, boolean callSuper) throws Throwable {
			String name = method == null ? null : method.getName();
			if (name == null) {
				return null;
			}
			String key = name + "(" + StringUtil.getString(method.getGenericParameterTypes()) + ")";
			Object handlerValue = get(key);

			Object value = callSuper ? super.invoke(proxy, method, args) : null;
			if (callSuper == false) {  //TODO default 方法如何执行里面的代码块？可能需要参考热更新，把方法动态加进去
			    if (Modifier.isStatic(method.getModifiers())) {  //正常情况不会进这个分支，因为 interface 中 static 方法不允许用实例来调用
                    value = method.invoke(null, args);
			    }
				else if (handlerValue instanceof JSONObject) {
					JSONObject handler = (JSONObject) handlerValue;
					value = handler.get(KEY_RETURN);  //TODO 可能根据传参而返回不同值
				}
				else {
					value = handlerValue;
				}
			}

			JSONObject methodObj = new JSONObject(true);  //只需要简要信息	JSONObject methodObj = parseMethodObject(method);
			methodObj.put(KEY_TIME, System.currentTimeMillis());
			methodObj.put(KEY_RETURN, value);

			List<JSONObject> finalMethodArgs = null;
			if (args != null) {
				finalMethodArgs = new ArrayList<>();

				for (int i = 0; i < args.length; i++) {
					Object v = args[i];
					String t = v == null ? "Object" : args[i].getClass().toGenericString();

					JSONObject o = new JSONObject(true);
					o.put(KEY_TYPE, t);
					o.put(KEY_VALUE, v);

					finalMethodArgs.add(o);
				}
			}
			methodObj.put(KEY_METHOD_ARGS, finalMethodArgs);



			//方法调用记录列表分组对象，按方法分组，且每组是按调用顺序排列的数组，同一个方法可能被调用多次
			JSONObject map = getJSONObject(KEY_CALL_MAP);
			if (map == null) {
				map = new JSONObject(true);
			}
			JSONArray cList = map.getJSONArray(key);
			if (cList == null) {
				cList = new JSONArray();
			}
			cList.add(0, methodObj);  //倒序，因为要最上方显示最终状态
			map.put(key, cList);
			put(KEY_CALL_MAP, map);


			//方法调用记录列表，按调用顺序排列的数组，同一个方法可能被调用多次
			JSONObject methodObj2 = new JSONObject(true);
			methodObj2.put(KEY_METHOD, key);
			methodObj2.putAll(methodObj);

			JSONArray list = getJSONArray(KEY_CALL_LIST);
			if (list == null) {
				list = new JSONArray();
			}
			list.add(methodObj2);  //顺序，因为要直观看到调用过程
			put(KEY_CALL_LIST, list);


			//是否被设置为 HTTP 回调方法
			Listener<?> listener = $_getCallback(key);
			if (listener != null) { //提前判断 && handler.getBooleanValue(KEY_CALLBACK)) {
				listener.complete(null);
			}

			return value; //实例是这个代理类，而不是原本的 interface，所以不行，除非能动态 implements。 return Modifier.isAbstract(method.getModifiers()) ? value : 执行非抽放方法(default 和 static);
		}
	}


}
