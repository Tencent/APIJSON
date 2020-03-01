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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.NotNull;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;

/**可远程调用的函数类
 * @author Lemon
 */
public class RemoteFunction {
	//	private static final String TAG = "RemoteFunction";

	// <methodName, JSONObject>
	// <isContain, <arguments:"array,key", tag:null, methods:null>>
	public static final Map<String, JSONObject> FUNCTION_MAP;
	static {
		FUNCTION_MAP = new HashMap<>();
	}
	
	private final RequestMethod method;
	private final String tag;
	private final int version;
	public RemoteFunction(RequestMethod method, String tag, int version) {
		this.method = method == null ? RequestMethod.GET : method;
		this.tag = tag;
		this.version = version;
	}

	public RequestMethod getMethod() {
		return method;
	}
	public String getTag() {
		return tag;
	}
	public int getVersion() {
		return version;
	}
	
	/**反射调用
	 * @param fun
	 * @param request
	 * @param function 例如get(Map:map,key)，参数只允许引用，不能直接传值
	 * @return
	 */
	public static Object invoke(@NotNull RemoteFunction fun, @NotNull JSONObject request, @NotNull String function) throws Exception {

		FunctionBean fb = parseFunction(function, request, false);
		
		JSONObject row = FUNCTION_MAP.get(fb.getMethod());
		if (row == null) {
			throw new UnsupportedOperationException("不允许调用远程函数 " + fb.getMethod() + " !");
		}
		
		int v = row.getIntValue("version");
		if (fun.getVersion() < v) {
			throw new UnsupportedOperationException("不允许 version = " + fun.getVersion() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 version >= " + v + " !");
		}
		String t = row.getString("tag");
		if (t != null && t.equals(fun.getTag()) == false) {
			throw new UnsupportedOperationException("不允许 tag = " + fun.getTag() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 tag = " + t + " !");
		}
		String[] methods = StringUtil.split(row.getString("methods"));
		List<String> ml = methods == null || methods.length <= 0 ? null : Arrays.asList(methods);
		if (ml != null && ml.contains(fun.getMethod().toString()) == false) {
			throw new UnsupportedOperationException("不允许 method = " + fun.getMethod() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 method 在 " + methods + "内 !");
		}

		try {
			return invoke(fun, fb.getMethod(), fb.getTypes(), fb.getValues()); 
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


	/**反射调用
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static Object invoke(@NotNull RemoteFunction fun, @NotNull String methodName, @NotNull Class<?>[] parameterTypes, @NotNull Object[] args) throws Exception {
		return fun.getClass().getDeclaredMethod(methodName, parameterTypes).invoke(fun, args);
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