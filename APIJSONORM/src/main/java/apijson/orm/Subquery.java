/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import apijson.JSONField;

import java.util.List;
import java.util.Map;

/**子查询 配置
 * @author Lemon
 */
public class Subquery<T, M extends Map<String, Object>, L extends List<Object>> {
	
	private String path; // []/0/User
	private String originKey; //id{}@
	private M originValue; // { "from": "Comment", "Comment": {...} }

	private String from; // Comment
	private String range; // ANY, ALL
	private String key; //id{}
	private SQLConfig<T, M, L> config;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig<T, JSONRequest, L> 里的 dbPassword 等
	public String getOriginKey() {
		return originKey;
	}
	public void setOriginKey(String originKey) {
		this.originKey = originKey;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig<T, JSONRequest, L> 里的 dbPassword 等
	public M getOriginValue() {
		return originValue;
	}
	public void setOriginValue(M originValue) {
		this.originValue = originValue;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig<T, JSONRequest, L> 里的 dbPassword 等
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig<T, JSONRequest, L> 里的 dbPassword 等
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig<T, JSONRequest, L> 里的 dbPassword 等
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig<T, JSONRequest, L> 里的 dbPassword 等
	public SQLConfig<T, M, L> getConfig() {
		return config;
	}
	public void setConfig(SQLConfig<T, M, L> config) {
		this.config = config;
	}

}
