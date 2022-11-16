/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;

/**可远程调用的函数类
 * @author Lemon
 */
public class AbstractFunctionParser implements FunctionParser {
	//	private static final String TAG = "AbstractFunctionParser";

    /**开启支持远程函数
     */
    public static boolean ENABLE_REMOTE_FUNCTION = true;
    /**开启支持远程函数中的 JavaScript 脚本形式
     */
    public static boolean ENABLE_SCRIPT_FUNCTION = true;

    public static final int TYPE_REMOTE_FUNCTION = 0;
    //public static final int TYPE_SQL_FUNCTION = 1;
    public static final int TYPE_SCRIPT_FUNCTION = 1;

	// <methodName, JSONObject>
	// <isContain, <arguments:"array,key", tag:null, methods:null>>
	public static Map<String, JSONObject> FUNCTION_MAP;
	public static Map<String, JSONObject> SCRIPT_MAP;
	static {
		FUNCTION_MAP = new HashMap<>();
		SCRIPT_MAP = new HashMap<>();
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
	 * @param function 例如get(Map:map,key)，参数只允许引用，不能直接传值
     * @param currentObject
     * @return {@link #invoke(AbstractFunctionParser, String, Class[], Object[])}
	 */
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String function, @NotNull JSONObject currentObject) throws Exception {
        if (ENABLE_REMOTE_FUNCTION == false) {
            throw new UnsupportedOperationException("AbstractFunctionParser.ENABLE_REMOTE_FUNCTION" +
                    " == false 时不支持远程函数！如需支持则设置 AbstractFunctionParser.ENABLE_REMOTE_FUNCTION = true ！");
        }

		FunctionBean fb = parseFunction(function, currentObject, false);

		JSONObject row = FUNCTION_MAP.get(fb.getMethod());
		if (row == null) {
			throw new UnsupportedOperationException("不允许调用远程函数 " + fb.getMethod() + " !");
		}

		int type = row.getIntValue("type");
        if (type < TYPE_REMOTE_FUNCTION || type > TYPE_SCRIPT_FUNCTION) {
            throw new UnsupportedOperationException("type = " + type + " 不合法！必须是 [0, 1] 中的一个 !");
        }
        if (ENABLE_SCRIPT_FUNCTION == false && type == TYPE_SCRIPT_FUNCTION) {
            throw new UnsupportedOperationException("type = " + type + " 不合法！AbstractFunctionParser.ENABLE_SCRIPT_FUNCTION" +
                    " == false 时不支持远程函数中的脚本形式！如需支持则设置 AbstractFunctionParser.ENABLE_SCRIPT_FUNCTION = true ！");
        }


		int version = row.getIntValue("version");
		if (parser.getVersion() < version) {
			throw new UnsupportedOperationException("不允许 version = " + parser.getVersion() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 version >= " + version + " !");
		}
		String tag = row.getString("tag");  // TODO 改为 tags，类似 methods 支持多个 tag。或者干脆不要？因为目前非开放请求全都只能后端指定
		if (tag != null && tag.equals(parser.getTag()) == false) {
			throw new UnsupportedOperationException("不允许 tag = " + parser.getTag() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 tag = " + tag + " !");
		}
		String[] methods = StringUtil.split(row.getString("methods"));
		List<String> ml = methods == null || methods.length <= 0 ? null : Arrays.asList(methods);
		if (ml != null && ml.contains(parser.getMethod().toString()) == false) {
			throw new UnsupportedOperationException("不允许 method = " + parser.getMethod() + " 的请求调用远程函数 " + fb.getMethod() + " ! 必须满足 method 在 " + Arrays.toString(methods) + "内 !");
		}

		try {
            return invoke(parser, fb.getMethod(), fb.getTypes(), fb.getValues(), currentObject, type);
		} catch (Exception e) {
			if (e instanceof NoSuchMethodException) {
				throw new IllegalArgumentException("字符 " + function + " 对应的远程函数 " + getFunction(fb.getMethod(), fb.getKeys()) + " 不在后端工程的DemoFunction内！"
						+ "\n请检查函数名和参数数量是否与已定义的函数一致！"
						+ "\n且必须为 function(key0,key1,...) 这种单函数格式！"
						+ "\nfunction必须符合Java函数命名，key是用于在request内取值的键！"
						+ "\n调用时不要有空格！" + (Log.DEBUG ? e.getMessage() : ""));
			}
			if (e instanceof InvocationTargetException) {
				Throwable te = ((InvocationTargetException) e).getTargetException();
				if (StringUtil.isEmpty(te.getMessage(), true) == false) { //到处把函数声明throws Exception改成throws Throwable挺麻烦
					throw te instanceof Exception ? (Exception) te : new Exception(te.getMessage());
				}
				throw new IllegalArgumentException("字符 " + function + " 对应的远程函数传参类型错误！"
						+ "\n请检查 key:value 中value的类型是否满足已定义的函数 " + getFunction(fb.getMethod(), fb.getKeys()) + " 的要求！"
						+ (Log.DEBUG ? e.getMessage() : ""));
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
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String methodName
            , @NotNull Class<?>[] parameterTypes, @NotNull Object[] args) throws Exception {
        return invoke(parser, methodName, parameterTypes, args, null, TYPE_REMOTE_FUNCTION);
    }
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String methodName
            , @NotNull Class<?>[] parameterTypes, @NotNull Object[] args, JSONObject currentObject, int type) throws Exception {
        if (type == TYPE_SCRIPT_FUNCTION) {
            return invokeScript(parser, methodName, parameterTypes, args, currentObject);
        }

        Method m = parser.getClass().getMethod(methodName, parameterTypes);
        //费性能，还是初始化时做更好
        //if (m.getReturnType().getSimpleName().equals(returnType) == false) {
        //  throw new IllegalArgumentTypeException("");
        //}
        return m.invoke(parser, args);
	}

    public static Invocable INVOCABLE;
    public static ScriptEngine SCRIPT_ENGINE;
    static {
        try {
            System.setProperty("Dnashorn.args", "language=es6");
            System.setProperty("Dnashorn.args", "--language=es6");
            System.setProperty("-Dnashorn.args", "--language=es6");

            /*获取执行JavaScript的执行引擎*/
            SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("javascript");
            INVOCABLE = (Invocable) SCRIPT_ENGINE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object invokeScript(@NotNull AbstractFunctionParser parser, @NotNull String methodName
            , @NotNull Class<?>[] parameterTypes, @NotNull Object[] args, JSONObject currentObject) throws Exception {
        JSONObject row = SCRIPT_MAP.get(methodName);
        if (row == null) {
            throw new UnsupportedOperationException("调用远程函数脚本 " + methodName + " 不存在!");
        }

        String script = row.getString("script");
        //SCRIPT_ENGINE.eval(script);
        //Object[] newArgs = args == null || args.length <= 0 ? null : new Object[args.length];

        SCRIPT_ENGINE.eval(script);

        // APIJSON 远程函数不应该支持
        //if (row.getBooleanValue("simple")) {
        //    return SCRIPT_ENGINE.eval(script);
        //}

        Object result;
        if (args == null || args.length <= 0) {
            result = INVOCABLE.invokeFunction(methodName);
        }
        else {
            //args[0] = JSON.toJSONString(args[0]);  // Java 调 js 函数只支持传基本类型，改用 invokeMethod ？

            //for (int i = 0; i < args.length; i++) {
            //    Object a = currentObject == null ? null : currentObject.get(args[i]);
            //    newArgs[i] = a == null || apijson.JSON.isBooleanOrNumberOrString(a) ? a : JSON.toJSONString(a);
            //}

            // TODO 先试试这个
            result = INVOCABLE.invokeFunction(methodName, args);
            //result = INVOCABLE.invokeMethod(args[0], methodName, args);

            //switch (newArgs.length) {
            //    case 1:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0]);
            //        break;
            //    case 2:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1]);
            //        break;
            //    case 3:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2]);
            //        break;
            //    case 4:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2], newArgs[3]);
            //        break;
            //    case 5:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2], newArgs[3], newArgs[4]);
            //        break;
            //    case 6:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2], newArgs[3], newArgs[4], newArgs[5]);
            //        break;
            //    case 7:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2], newArgs[3], newArgs[4], newArgs[5], newArgs[6]);
            //        break;
            //    case 8:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2], newArgs[3], newArgs[4], newArgs[5], newArgs[6], newArgs[7]);
            //        break;
            //    case 9:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2], newArgs[3], newArgs[4], newArgs[5], newArgs[6], newArgs[7], newArgs[8]);
            //        break;
            //    case 10:
            //        result = INVOCABLE.invokeFunction(methodName, newArgs[0], newArgs[1], newArgs[2], newArgs[3], newArgs[4], newArgs[5], newArgs[6], newArgs[7], newArgs[8], newArgs[9]);
            //        break;
            //}
        }

        System.out.println("invokeScript " + methodName + "(..) >>  result = " + result);
        return result;
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