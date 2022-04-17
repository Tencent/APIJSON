/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;

/**可远程调用的函数类
 * @author Lemon
 */
public class AbstractFunctionParser implements FunctionParser {
	//	private static final String TAG = "AbstractFunctionParser";

	// <methodName, JSONObject>
	// <isContain, <arguments:"array,key", tag:null, methods:null>>
	public static Map<String, JSONObject> FUNCTION_MAP;
	static {
		FUNCTION_MAP = new HashMap<>();
	}

	private RequestMethod method;
	private String tag;
	private int version;
	private JSONObject request;
	public AbstractFunctionParser() {
		this(null, null, 0, null);
	}
	public AbstractFunctionParser(RequestMethod method, String tag, int version, @NotNull JSONObject request) {
		setMethod(method == null ? RequestMethod.GET : method);
		setTag(tag);
		setVersion(version);
		setRequest(request);
	}

	@Override
	public RequestMethod getMethod() {
		return method;
	}
	@Override
	public AbstractFunctionParser setMethod(RequestMethod method) {
		this.method = method;
		return this;
	}
	@Override
	public String getTag() {
		return tag;
	}
	@Override
	public AbstractFunctionParser setTag(String tag) {
		this.tag = tag;
		return this;
	}
	@Override
	public int getVersion() {
		return version;
	}
	@Override
	public AbstractFunctionParser setVersion(int version) {
		this.version = version;
		return this;
	}
	
	private String key;
	@Override
	public String getKey() {
		return key;
	}
	@Override
	public AbstractFunctionParser setKey(String key) {
		this.key = key;
		return this;
	}
	
	private String parentPath;
	@Override
	public String getParentPath() {
		return parentPath;
	}
	@Override
	public AbstractFunctionParser setParentPath(String parentPath) {
		this.parentPath = parentPath;
		return this;
	}
	private String currentName;
	@Override
	public String getCurrentName() {
		return currentName;
	}
	@Override
	public AbstractFunctionParser setCurrentName(String currentName) {
		this.currentName = currentName;
		return this;
	}
	
	@NotNull
	@Override
	public JSONObject getRequest() {
		return request;
	}
	@Override
	public AbstractFunctionParser setRequest(@NotNull JSONObject request) {
		this.request = request;
		return this;
	}
	
	private JSONObject currentObject;
	@NotNull 
	@Override
	public JSONObject getCurrentObject() {
		return currentObject;
	}
	@Override
	public AbstractFunctionParser setCurrentObject(@NotNull JSONObject currentObject) {
		this.currentObject = currentObject;
		return this;
	}


	/**反射调用
	 * @param function 例如get(object,key)，参数只允许引用，不能直接传值
	 * @param currentObject 不作为第一个参数，就不能远程调用invoke，避免死循环
	 * @return {@link #invoke(AbstractFunctionParser, String, JSONObject)}
	 */
	@Override
	public Object invoke(@NotNull String function, @NotNull JSONObject currentObject) throws Exception {
		return invoke(this, function, currentObject);
	}
	
	/**反射调用
	 * @param parser
	 * @param request
	 * @param function 例如get(Map:map,key)，参数只允许引用，不能直接传值
	 * @return {@link #invoke(AbstractFunctionParser, String, Class[], Object[])}
	 */
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String function, @NotNull JSONObject currentObject) throws Exception {

		FunctionBean fb = parseFunction(function, currentObject, false);

		JSONObject row = FUNCTION_MAP.get(fb.getMethod());
		if (row == null) {
			throw new UnsupportedOperationException("不允许调用远程函数 " + fb.getMethod() + " !");
		}

		int v = row.getIntValue("version");
		if (parser.getVersion() < v) {
			throw new UnsupportedOperationException("不允许 version = " + parser.getVersion() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 version >= " + v + " !");
		}
		String t = row.getString("tag");
		if (t != null && t.equals(parser.getTag()) == false) {
			throw new UnsupportedOperationException("不允许 tag = " + parser.getTag() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 tag = " + t + " !");
		}
		String[] methods = StringUtil.split(row.getString("methods"));
		List<String> ml = methods == null || methods.length <= 0 ? null : Arrays.asList(methods);
		if (ml != null && ml.contains(parser.getMethod().toString()) == false) {
			throw new UnsupportedOperationException("不允许 method = " + parser.getMethod() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 method 在 " + Arrays.toString(methods) + "内 !");
		}

		try {
			return invoke(parser, fb.getMethod(), fb.getTypes(), fb.getValues()); 
		} catch (Exception e) {
			if (e instanceof NoSuchMethodException) {
				throw new IllegalArgumentException("字符 " + function + " 对应的远程函数 " + getFunction(fb.getMethod(), fb.getKeys()) + " 不在后端工程的DemoFunction内！"
						+ "\n请检查函数名和参数数量是否与已定义的函数一致！"
						+ "\n且必须为 function(key0,key1,...) 这种单函数格式！"
						+ "\nfunction必须符合Java函数命名，key是用于在request内取值的键！"
						+ "\n调用时不要有空格！");
			}
			if (e instanceof InvocationTargetException) {
				Throwable te = ((InvocationTargetException) e).getTargetException();
				if (StringUtil.isEmpty(te.getMessage(), true) == false) { //到处把函数声明throws Exception改成throws Throwable挺麻烦
					throw te instanceof Exception ? (Exception) te : new Exception(te.getMessage());
				}
				throw new IllegalArgumentException("字符 " + function + " 对应的远程函数传参类型错误！"
						+ "\n请检查 key:value 中value的类型是否满足已定义的函数 " + getFunction(fb.getMethod(), fb.getKeys()) + " 的要求！");
			}
			throw e;
		}

	}
	
	/**反射调用
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String methodName, @NotNull Class<?>[] parameterTypes, @NotNull Object[] args) throws Exception {
		return parser.getClass().getMethod(methodName, parameterTypes).invoke(parser, args);
	}

	/**解析函数
	 * @param function
	 * @param request
	 * @param isSQLFunction
	 * @return
	 * @throws Exception
	 */
	@NotNull
	public static FunctionBean parseFunction(@NotNull String function, @NotNull JSONObject request, boolean isSQLFunction) throws Exception {

		int start = function.indexOf("(");
		int end = function.lastIndexOf(")");
		String method = (start <= 0 || end != function.length() - 1) ? null : function.substring(0, start);
		if (StringUtil.isEmpty(method, true)) {
			throw new IllegalArgumentException("字符 " + function + " 不合法！函数的名称 function 不能为空，"
					+ "且必须为 function(key0,key1,...) 这种单函数格式！"
					+ "\nfunction必须符合 " + (isSQLFunction ? "SQL 函数/SQL 存储过程" : "Java 函数") + " 命名，key 是用于在 request 内取值的键！");
		}

		String[] keys = StringUtil.split(function.substring(start + 1, end));

		int length = keys == null ? 0 : keys.length;

		Class<?>[] types;
		Object[] values;

		if (isSQLFunction) {
			types = new Class<?>[length];
			values = new Object[length];

			//碰到null就挂了！！！Number还得各种转换不灵活！不如直接传request和对应的key到函数里，函数内实现时自己 getLongValue,getJSONObject ...
			Object v;
			for (int i = 0; i < length; i++) {
				v = values[i] = request.get(keys[i]);
				if (v == null) {
					types[i] = Object.class;
					values[i] = null;
					break;
				}

				if (v instanceof Boolean) {
					types[i] = Boolean.class; //只支持JSON的几种类型 
				}
				else if (v instanceof Number) {
					types[i] = Number.class;
				}
				else if (v instanceof String) {
					types[i] = String.class;
				}
				else if (v instanceof JSONObject) { // Map) {
					types[i] = JSONObject.class;
					//性能比较差	values[i] = request.getJSONObject(keys[i]);
				}
				else if (v instanceof JSONArray) { // Collection) {
					types[i] = JSONArray.class;
					//性能比较差	values[i] = request.getJSONArray(keys[i]);
				}
				else { //FIXME 碰到null就挂了！！！
					throw new UnsupportedDataTypeException(keys[i] + ":value 中value不合法！远程函数 key():" + function + " 中的arg对应的值类型"
							+ "只能是 [Boolean, Number, String, JSONObject, JSONArray] 中的一种！");
				}
			}
		}
		else {
			types = new Class<?>[length + 1];
			types[0] = JSONObject.class;

			values = new Object[length + 1];
			values[0] = request;

			for (int i = 0; i < length; i++) {
				types[i + 1] = String.class;
				values[i + 1] = keys[i];
			}
		}

		FunctionBean fb = new FunctionBean();
		fb.setFunction(function);
		fb.setMethod(method);
		fb.setKeys(keys);
		fb.setTypes(types);
		fb.setValues(values);

		return fb;
	}


	/**
	 * @param method
	 * @param keys
	 * @return
	 */
	public static String getFunction(String method, String[] keys) {
		String f = method + "(JSONObject request";

		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				f += (", String " + keys[i]);
			}
		}

		f += ")";

		return f;
	}


	public static class FunctionBean {
		private String function;
		private String method;
		private String[] keys;
		private Class<?>[] types;
		private Object[] values;

		public String getFunction() {
			return function;
		}
		public void setFunction(String function) {
			this.function = function;
		}

		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}

		public String[] getKeys() {
			return keys;
		}
		public void setKeys(String[] keys) {
			this.keys = keys;
		}

		public Class<?>[] getTypes() {
			return types;
		}
		public void setTypes(Class<?>[] types) {
			this.types = types;
		}

		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}


		/**
		 * @param useValue
		 * @return
		 */
		public String toFunctionCallString(boolean useValue) {
			return toFunctionCallString(useValue, null);
		}
		/**
		 * @param useValue
		 * @param quote
		 * @return
		 */
		public String toFunctionCallString(boolean useValue, String quote) {
			String s = getMethod() + "(";

			Object[] args = useValue ? getValues() : getKeys();
			if (args != null && args.length > 0) {
				if (quote == null) {
					quote = "'";
				}

				Object arg;
				for (int i = 0; i < args.length; i++) {
					arg = args[i];
					s += (i <= 0 ? "" : ",") + (arg instanceof Boolean || arg instanceof Number ? arg : quote + arg + quote);
				}
			}

			return s + ")";
		}

	}

}