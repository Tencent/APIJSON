/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Lemon
 */
public abstract class OnParseCallback {


	/**
	 * @param key
	 * @param to
	 * @param ro
	 * @return false ? continue
	 * @throws Exception
	 */
	protected boolean onParse(String key, Object to, Object ro) throws Exception {
		return true;
	}

	/**
	 * @param key
	 * @param to
	 * @param ro
	 * @return
	 * @throws Exception
	 */
	protected Object onParseObject(String key, Object to, Object ro) throws Exception {
		return ro;
	}

	/**
	 * @param key
	 * @param tobj
	 * @param robj
	 * @return
	 * @throws Exception
	 */
	protected JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
		return robj;
	}

	/**
	 * @param key
	 * @param tarray
	 * @param rarray
	 * @return
	 * @throws Exception
	 */
	protected JSONArray onParseJSONArray(String key, JSONArray tarray, JSONArray rarray) throws Exception {
		return rarray;
	}

}
