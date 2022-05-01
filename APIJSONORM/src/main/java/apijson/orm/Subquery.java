/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**子查询 配置
 * @author Lemon
 */
public class Subquery {
	
	private String path; // []/0/User
	private String originKey; //id{}@
	private JSONObject originValue; // { "from": "Comment", "Comment": {...} }

	private String from; // Comment
	private String range; // ANY, ALL
	private String key; //id{}
	private SQLConfig config;
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig 里的 dbPassword 等
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig 里的 dbPassword 等
	public String getOriginKey() {
		return originKey;
	}
	public void setOriginKey(String originKey) {
		this.originKey = originKey;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig 里的 dbPassword 等
	public JSONObject getOriginValue() {
		return originValue;
	}
	public void setOriginValue(JSONObject originValue) {
		this.originValue = originValue;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig 里的 dbPassword 等
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig 里的 dbPassword 等
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig 里的 dbPassword 等
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@JSONField(serialize = false) //解决泄漏 SQLConfig 里的 dbPassword 等
	public SQLConfig getConfig() {
		return config;
	}
	public void setConfig(SQLConfig config) {
		this.config = config;
	}

	

}
