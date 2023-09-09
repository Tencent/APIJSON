/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;
import apijson.RequestMethod;


/**远程函数解析器
 * @author Lemon
 */
public interface FunctionParser<T> {

	Object invoke(@NotNull String function, @NotNull JSONObject currentObject) throws Exception;
	Object invoke(@NotNull String function, @NotNull JSONObject currentObject, boolean containRaw) throws Exception;

	Parser<T> getParser();

	FunctionParser<T> setParser(Parser<T> parser);

	RequestMethod getMethod();
	FunctionParser<T> setMethod(RequestMethod method);

	String getTag();
	FunctionParser<T> setTag(String tag);

	int getVersion();
	FunctionParser<T> setVersion(int version);

	@NotNull 
	JSONObject getRequest();
	FunctionParser<T> setRequest(@NotNull JSONObject request);


	String getKey();
	FunctionParser<T> setKey(String key);
	
	String getParentPath();
	FunctionParser<T> setParentPath(String parentPath);

	String getCurrentName();
	FunctionParser<T> setCurrentName(String currentName);

	@NotNull
	JSONObject getCurrentObject();
	FunctionParser<T> setCurrentObject(@NotNull JSONObject currentObject);



}
