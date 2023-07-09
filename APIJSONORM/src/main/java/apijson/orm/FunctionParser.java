/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;
import apijson.RequestMethod;


/**远程函数解析器
 * @author Lemon
 */
public interface FunctionParser {

	Object invoke(@NotNull String function, @NotNull JSONObject currentObject) throws Exception;
	Object invoke(@NotNull String function, @NotNull JSONObject currentObject, boolean containRaw) throws Exception;

	Parser<?> getParser();

	AbstractFunctionParser setParser(Parser<?> parser);

	RequestMethod getMethod();
	FunctionParser setMethod(RequestMethod method);

	String getTag();
	FunctionParser setTag(String tag);

	int getVersion();
	AbstractFunctionParser setVersion(int version);

	@NotNull 
	JSONObject getRequest();
	FunctionParser setRequest(@NotNull JSONObject request);


	String getKey();
	FunctionParser setKey(String key);
	
	String getParentPath();
	FunctionParser setParentPath(String parentPath);

	String getCurrentName();
	FunctionParser setCurrentName(String currentName);

	@NotNull
	JSONObject getCurrentObject();
	FunctionParser setCurrentObject(@NotNull JSONObject currentObject);



}
