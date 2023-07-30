/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.exception.UnsupportedDataTypeException;
import apijson.orm.script.ScriptExecutor;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static apijson.orm.AbstractSQLConfig.PATTERN_SCHEMA;

/**可远程调用的函数类
 * @author Lemon
 */
public class AbstractFunctionParser implements FunctionParser {
    private static final String TAG = "AbstractFunctionParser";

    /**是否解析参数 key 的对应的值，不用手动编码 curObj.getString(key)
     */
    public static boolean IS_PARSE_ARG_VALUE = false;

    /**开启支持远程函数
     */
    public static boolean ENABLE_REMOTE_FUNCTION = true;
    /**开启支持远程函数中的 JavaScript 脚本形式
     */
    public static boolean ENABLE_SCRIPT_FUNCTION = true;

	// <methodName, JSONObject>
	// <isContain, <arguments:"array,key", tag:null, methods:null>>
    public static Map<String, ScriptExecutor> SCRIPT_EXECUTOR_MAP;
	public static Map<String, JSONObject> FUNCTION_MAP;
	static {
		FUNCTION_MAP = new HashMap<>();
		SCRIPT_EXECUTOR_MAP = new HashMap<>();
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

	private Parser<?> parser;
	@Override
	public Parser<?> getParser() {
		return parser;
	}
	@Override
	public AbstractFunctionParser setParser(Parser<?> parser) {
		this.parser = parser;
		return this;
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

	/**根据路径从当前对象取值
	 * @param path
	 * @return
	 * @param <T>
	 */
	public <T extends Object> T getArgVal(String path) {
		return getArgVal(getCurrentObject(), path);
	}
	/**根据路径从对象 obj 中取值
	 * @param obj
	 * @param path
	 * @return
	 * @param <T>
	 */
	public static <T extends Object> T getArgVal(JSONObject obj, String path) {
		return AbstractParser.getValue(obj, StringUtil.splitPath(path));
	}


	/**反射调用
	 * @param function 例如get(object,key)，参数只允许引用，不能直接传值
	 * @param currentObject 不作为第一个参数，就不能远程调用invoke，避免死循环
	 * @return {@link #invoke(String, JSONObject, boolean)}
	 */
	@Override
	public Object invoke(@NotNull String function, @NotNull JSONObject currentObject) throws Exception {
        return invoke(function, currentObject, false);
    }
	/**反射调用
	 * @param function 例如get(object,key)，参数只允许引用，不能直接传值
	 * @param currentObject 不作为第一个参数，就不能远程调用invoke，避免死循环
	 * @param containRaw 包含原始 SQL 片段
	 * @return {@link #invoke(AbstractFunctionParser, String, JSONObject, boolean)}
	 */
	@Override
	public Object invoke(@NotNull String function, @NotNull JSONObject currentObject, boolean containRaw) throws Exception {
		return invoke(this, function, currentObject, containRaw);
	}
	
	/**反射调用
	 * @param parser
	 * @param function 例如get(Map:map,key)，参数只允许引用，不能直接传值
     * @param currentObject
     * @return {@link #invoke(AbstractFunctionParser, String, Class[], Object[])}
	 */
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String function, @NotNull JSONObject currentObject, boolean containRaw) throws Exception {
        if (ENABLE_REMOTE_FUNCTION == false) {
            throw new UnsupportedOperationException("AbstractFunctionParser.ENABLE_REMOTE_FUNCTION" +
                    " == false 时不支持远程函数！如需支持则设置 AbstractFunctionParser.ENABLE_REMOTE_FUNCTION = true ！");
        }

		FunctionBean fb = parseFunction(function, currentObject, false, containRaw);

		JSONObject row = FUNCTION_MAP.get(fb.getMethod()); //FIXME  fb.getSchema() + "." + fb.getMethod()
		if (row == null) {
			throw new UnsupportedOperationException("不允许调用远程函数 " + fb.getMethod() + " !");
		}

        String language = row.getString("language");
        String lang = "java".equalsIgnoreCase(language) ? null : language;

        if (ENABLE_SCRIPT_FUNCTION == false && lang != null) {
            throw new UnsupportedOperationException("language = " + language + " 不合法！AbstractFunctionParser.ENABLE_SCRIPT_FUNCTION" +
                    " == false 时不支持远程函数中的脚本形式！如需支持则设置 AbstractFunctionParser.ENABLE_SCRIPT_FUNCTION = true ！");
        }
        
		if (lang != null && SCRIPT_EXECUTOR_MAP.get(lang) == null) {
			throw new ClassNotFoundException("找不到脚本语言 " + lang + " 对应的执行引擎！请先依赖相关库并在后端 APIJSONFunctionParser 中注册！");
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
            return invoke(parser, fb.getMethod(), fb.getTypes(), fb.getValues(), row.getString("returnType"), currentObject, SCRIPT_EXECUTOR_MAP.get(lang));
		}
        catch (Exception e) {
			if (e instanceof NoSuchMethodException) {
				throw new IllegalArgumentException("字符 " + function + " 对应的远程函数 " + getFunction(fb.getMethod(), fb.getKeys())
                        + " 不在后端 " + parser.getClass().getName() + " 内，也不在父类中！如果需要则先新增对应方法！"
						+ "\n请检查函数名和参数数量是否与已定义的函数一致！"
						+ "\n且必须为 function(key0,key1,...) 这种单函数格式！"
						+ "\nfunction 必须符合 Java 函数命名，key 是用于在 curObj 内取值的键！"
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
     * @param parser
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return {@link #invoke(AbstractFunctionParser, String, Class[], Object[], String, JSONObject, ScriptExecutor)}
     * @throws Exception
     */
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String methodName
            , @NotNull Class<?>[] parameterTypes, @NotNull Object[] args) throws Exception {
        return invoke(parser, methodName, parameterTypes, args, null, null, null);
    }
    /**反射调用
     * @param parser
     * @param methodName
     * @param parameterTypes
     * @param args
     * @param returnType
     * @param currentObject
     * @param scriptExecutor
     * @return
     * @throws Exception
     */
	public static Object invoke(@NotNull AbstractFunctionParser parser, @NotNull String methodName
            , @NotNull Class<?>[] parameterTypes, @NotNull Object[] args, String returnType
            , JSONObject currentObject, ScriptExecutor scriptExecutor) throws Exception {
        if (scriptExecutor != null) {
            return invokeScript(parser, methodName, parameterTypes, args, returnType, currentObject, scriptExecutor);
        }

        Method m = parser.getClass().getMethod(methodName, parameterTypes); // 不用判空，拿不到就会抛异常

        if (Log.DEBUG) {
            String rt = Log.DEBUG && m.getReturnType() != null ? m.getReturnType().getSimpleName() : null;

            if ("void".equals(rt)) {
                rt = null;
            }
            if ("void".equals(returnType)) {
                returnType = null;
            }

            if (rt != returnType && (rt == null || rt.equals(returnType) == false)) {
                throw new WrongMethodTypeException("远程函数 " + methodName + " 的实际返回值类型 " + rt + " 与 Function 表中的配置的 " + returnType + " 不匹配！");
            }
        }

        return m.invoke(parser, args);
	}

    /**Java 调用 JavaScript 函数
     * @param parser
     * @param methodName
     * @param parameterTypes
     * @param args
     * @param returnType
     * @param currentObject
     * @return
     * @throws Exception
     */
    public static Object invokeScript(@NotNull AbstractFunctionParser parser, @NotNull String methodName
            , @NotNull Class<?>[] parameterTypes, @NotNull Object[] args, String returnType, JSONObject currentObject, ScriptExecutor scriptExecutor) throws Exception {
    	Object result = scriptExecutor.execute(parser, currentObject, methodName, args);
        if (Log.DEBUG && result != null) {
            Class<?> rt = result.getClass(); // 作为远程函数的 js 类型应该只有 JSON 的几种类型
            String fullReturnType = (StringUtil.isSmallName(returnType)
                    ? returnType : (returnType.startsWith("JSON") ? "com.alibaba.fastjson." : "java.lang.") + returnType);

            if ((rt == null && returnType != null) || (rt != null && returnType == null)) {
                throw new WrongMethodTypeException("远程函数 " + methodName + " 的实际返回值类型 "
                        + (rt == null ? null : rt.getName()) + " 与 Function 表中的配置的 " + fullReturnType + " 不匹配！");
            }

            Class<?> cls;
            try {
                cls = Class.forName(fullReturnType);
            }
            catch (Exception e) {
                throw new WrongMethodTypeException("远程函数 " + methodName + " 在 Function 表中的配置的类型 "
                        + returnType + " 对应的 " + fullReturnType + " 错误！在 Java 中 Class.forName 找不到这个类型！");
            }

            if (cls.isAssignableFrom(rt) == false) {
                throw new WrongMethodTypeException("远程函数 " + methodName + " 的实际返回值类型 "
                        + (rt == null ? null : rt.getName()) + " 与 Function 表中的配置的 "
                        + returnType + " 对应的 " + fullReturnType + " 不匹配！");
            }
        }

        Log.d(TAG, "invokeScript " + methodName + "(..) >>  result = " + result);
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
        return parseFunction(function, request, isSQLFunction, false);
    }
    /**解析函数，自动解析的值类型只支持 Boolean, Number, String, Map, List
     * @param function
     * @param request
     * @param isSQLFunction
     * @param containRaw
     * @return
     * @throws Exception
     */
	public static FunctionBean parseFunction(@NotNull String function, @NotNull JSONObject request, boolean isSQLFunction, boolean containRaw) throws Exception {

		int start = function.indexOf("(");
		int end = function.lastIndexOf(")");
		String method = (start <= 0 || end != function.length() - 1) ? null : function.substring(0, start);

        int dotInd = method == null ? -1 : method.indexOf(".");
        String schema = dotInd < 0 ? null : method.substring(0, dotInd);
        method = dotInd < 0 ? method : method.substring(dotInd + 1);

        if (StringUtil.isName(method) == false) {
			throw new IllegalArgumentException("字符 " + method + " 不合法！函数的名称 function 不能为空且必须符合方法命名规范！"
					+ "总体必须为 function(key0,key1,...) 这种单函数格式！"
					+ "\nfunction必须符合 " + (isSQLFunction ? "SQL 函数/SQL 存储过程" : "Java 函数") + " 命名，key 是用于在 request 内取值的键！");
		}
        if (isSQLFunction != true && schema != null) { // StringUtil.isNotEmpty(schema, false)) {
            throw new IllegalArgumentException("字符 " + schema + " 不合法！远程函数不允许指定类名！"
                    + "且必须为 function(key0,key1,...) 这种单函数格式！"
                    + "\nfunction必须符合 " + (isSQLFunction ? "SQL 函数/SQL 存储过程" : "Java 函数") + " 命名，key 是用于在 request 内取值的键！");
        }
        if (schema != null) { // StringUtil.isName(schema) == false) {
			schema = extractSchema(schema, null);
        }

		String[] keys = StringUtil.split(function.substring(start + 1, end));
		int length = keys == null ? 0 : keys.length;

		Class<?>[] types;
		Object[] values;

		if (isSQLFunction || IS_PARSE_ARG_VALUE) {
			types = new Class<?>[length];
			values = new Object[length];

			//碰到null就挂了！！！Number还得各种转换不灵活！不如直接传request和对应的key到函数里，函数内实现时自己 getLongValue,getJSONObject ...
			Object v;
			for (int i = 0; i < length; i++) {
				v = values[i] = getArgValue(request, keys[i], containRaw); // request.get(keys[i]);
				if (v == null) {
					types[i] = Object.class;
					values[i] = null;
					break;
				}

				if (v instanceof Boolean) {
					types[i] = Boolean.class; //只支持JSON的几种类型 
				} // 怎么都有 bug，如果是引用的值，很多情况下无法指定  //  用 1L 指定为 Long ？ 其它的默认按长度分配为 Integer 或 Long？
				//else if (v instanceof Long || v instanceof Integer || v instanceof Short) {
				//	types[i] = Long.class;
				//}
				else if (v instanceof Number) {
					types[i] = Number.class;
				}
				else if (v instanceof String) {
					types[i] = String.class;
				}
				else if (v instanceof Map) { // 泛型兼容？ // JSONObject
					types[i] = Map.class;
					//性能比较差
                    //values[i] = TypeUtils.cast(v, Map.class, ParserConfig.getGlobalInstance());
				}
				else if (v instanceof Collection) { // 泛型兼容？ // JSONArray
					types[i] = List.class;
					//性能比较差
                    values[i] = TypeUtils.cast(v, List.class, ParserConfig.getGlobalInstance());
				}
				else {
					throw new UnsupportedDataTypeException(keys[i] + ":value 中value不合法！远程函数 key():"
                            + function + " 中的 arg 对应的值类型只能是 [Boolean, Number, String, JSONObject, JSONArray] 中的一种！");
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
		fb.setSchema(schema);
		fb.setMethod(method);
		fb.setKeys(keys);
		fb.setTypes(types);
		fb.setValues(values);

		return fb;
	}

	public static void verifySchema(String sch, String table) {
		extractSchema(sch, table);
	}

	public static String extractSchema(String sch, String table) {
		if (table == null) {
			table = "Table";
		}

		int ind = sch.indexOf("`");
		if (ind > 0) {
			throw new IllegalArgumentException(table + ": { @key(): value } 对应存储过程 value 中字符 "
					+ sch + " 不合法！`schema` 当有 ` 包裹时一定是首尾各一个，不能多也不能少！");
		}

		if (ind == 0) {
			sch = sch.substring(1);
			if (sch.indexOf("`") != sch.length() - 1) {
				throw new IllegalArgumentException(table + ": { @key(): value } 对应存储过程 value 中字符 `"
						+ sch + " 不合法！`schema` 当有 ` 包裹时一定是首尾各一个，不能多也不能少！");
			}

			sch = sch.substring(0, sch.length() - 1);
		}

		if (PATTERN_SCHEMA.matcher(sch).matches() == false || sch.contains("--")) {
			throw new IllegalArgumentException(table + ": { @key(): value } 对应存储过程 value 中字符 "
					+ sch + " 不合法！schema.function(arg) 中 schema 必须符合 数据库名/模式名 的命名规则！"
					+ "一般只能传英文字母、数字、下划线！不允许 -- 等可能导致 SQL 注入的符号！");
		}

		return sch;
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

    public static <T> T getArgValue(@NotNull JSONObject currentObject, String keyOrValue) {
        return getArgValue(currentObject, keyOrValue, false);
    }
    public static <T> T getArgValue(@NotNull JSONObject currentObject, String keyOrValue, boolean containRaw) {
        if (keyOrValue == null) {
            return null;
        }


        if (keyOrValue.endsWith("`") && keyOrValue.substring(1).indexOf("`") == keyOrValue.length() - 2) {
            return (T) currentObject.get(keyOrValue.substring(1, keyOrValue.length() - 1));
        }

        if (keyOrValue.endsWith("'") && keyOrValue.substring(1).indexOf("'") == keyOrValue.length() - 2) {
            return (T) keyOrValue.substring(1, keyOrValue.length() - 1);
        }

        // 传参加上 @raw:"key()" 避免意外情况
        Object val = containRaw ? AbstractSQLConfig.RAW_MAP.get(keyOrValue) : null;
        if (val != null) {
            return (T) ("".equals(val) ? keyOrValue : val);
        }

        if (StringUtil.isName(keyOrValue.startsWith("@") ? keyOrValue.substring(1) : keyOrValue)) {
            return (T) currentObject.get(keyOrValue);
        }

        if ("true".equals(keyOrValue)) {
            return (T) Boolean.TRUE;
        }
        if ("false".equals(keyOrValue)) {
            return (T) Boolean.FALSE;
        }

        // 性能更好，但居然非法格式也不报错
        //try {
        //    val = Boolean.valueOf(keyOrValue); // JSON.parse(keyOrValue);
        //    return (T) val;
        //}
        //catch (Throwable e) {
        //    Log.d(TAG, "getArgValue  try {\n" +
        //            "            val = Boolean.valueOf(keyOrValue);" +
        //            "} catch (Throwable e) = " + e.getMessage());
        //}

        try {
            val = Double.valueOf(keyOrValue); // JSON.parse(keyOrValue);
            return (T) val;
        }
        catch (Throwable e) {
            Log.d(TAG, "getArgValue  try {\n" +
                    "            val = Double.valueOf(keyOrValue);" +
                    "} catch (Throwable e) = " + e.getMessage());
        }

        return (T) currentObject.get(keyOrValue);
    }

	public static class FunctionBean {
		private String function;
		private String schema;
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

        public String getSchema() {
            return schema;
        }
        public void setSchema(String schema) {
            this.schema = schema;
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
            //String sch = getSchema();
			//String s = (StringUtil.isEmpty(sch) ? "" : sch + ".") + getMethod() + "(";
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