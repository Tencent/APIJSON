package zuo.biao.apijson;

import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class OnParseCallback {



	/**
	 * @param key
	 * @param to
	 * @param ro
	 * @return false ? continue
	 * @throws Exception
	 */
	protected boolean onParse(String key, Object to, Object ro) throws Exception {
//		int operate = getOperate(key);
//		if (operate <= 0) {
//			return true;
//		}
//
//		if (to instanceof JSONObject == false) {
//			throw new UnsupportedDataTypeException("服务器Request表配置错误！");
//		}
//		if (ro instanceof JSONObject == false) {
//			throw new UnsupportedDataTypeException(key + ":value 中 value不是JSONObject！");
//		}
//
//		if (operate == OPERATE_PUT) {
//			((JSONObject) ro).putAll((JSONObject) to);
//			return false;
//		}
//
//		Set<Entry<String, Object>> set = new LinkedHashSet<>(((JSONObject) to).entrySet());
//		String k;
//		for (Entry<String, Object> e : set) {
//			k = e == null ? null : e.getKey();
//			if (k == null) {
//				continue;
//			}
//
//			if (operate == OPERATE_VERIFY) {//TODO {}, $, <>
//
//
//			} else {
//				if (((JSONObject) ro).containsKey(k)) {
//					switch (operate) {
//					case OPERATE_REPLACE:
//						((JSONObject) ro).put(k, e.getValue());
//						break;
//					case OPERATE_REMOVE:
//						((JSONObject) ro).remove(k);
//						break;
//					}
//				} else {
//					if (operate == OPERATE_ADD) {
//						((JSONObject) ro).put(k, e.getValue());
//					}
//				}
//			}
//		}
//
//
//		return false;
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
