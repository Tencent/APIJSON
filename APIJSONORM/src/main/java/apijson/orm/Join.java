/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;

/**连表 配置
 * @author Lemon
 */
public class Join {

	private String path;

	private String originKey;
	private String originValue;

	private String joinType; // "@" - APP, "<" - LEFT, ">" - RIGHT, "*" - CROSS, "&" - INNER, "|" - FULL, "!" - OUTER, "^" - SIDE, "(" - ANTI, ")" - FOREIGN
	private String relateType; // "" - 一对一, "{}" - 一对多, "<>" - 多对一
	private JSONObject request; // { "id@":"/Moment/userId" }
	private String table; //User
	private String alias; //owner
	private String key; //id
	private String targetTable; // Moment
	private String targetAlias; //main
	private String targetKey; // userId

	private JSONObject outter;

	private SQLConfig joinConfig;
	private SQLConfig cacheConfig;
	private SQLConfig outterConfig;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String getOriginKey() {
		return originKey;
	}
	public void setOriginKey(String originKey) {
		this.originKey = originKey;
	}
	public String getOriginValue() {
		return originValue;
	}
	public void setOriginValue(String originValue) {
		this.originValue = originValue;
	}

	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	public String getRelateType() {
		return relateType;
	}
	public void setRelateType(String relateType) {
		this.relateType = relateType;
	}

	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public JSONObject getRequest() {
		return request;
	}
	public void setRequest(JSONObject request) {
		this.request = request;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	public String getTargetTable() {
		return targetTable;
	}
	public void setTargetAlias(String targetAlias) {
		this.targetAlias = targetAlias;
	}
	public String getTargetAlias() {
		return targetAlias;
	}
	public String getTargetKey() {
		return targetKey;
	}
	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}

	public JSONObject getOuter() {
		return outter;
	}
	public void setOuter(JSONObject outter) {
		this.outter = outter;
	}

	public SQLConfig getJoinConfig() {
		return joinConfig;
	}
	public void setJoinConfig(SQLConfig joinConfig) {
		this.joinConfig = joinConfig;
	}
	public SQLConfig getCacheConfig() {
		return cacheConfig;
	}
	public void setCacheConfig(SQLConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
	}

	public SQLConfig getOuterConfig() {
		return outterConfig;
	}
	public void setOuterConfig(SQLConfig outterConfig) {
		this.outterConfig = outterConfig;
	}


	public void setKeyAndType(@NotNull String originKey) throws Exception { //id, id@, id{}@, contactIdList<>@ ...
		if (originKey.endsWith("@")) {
			originKey = originKey.substring(0, originKey.length() - 1);
		}
		else { //TODO 暂时只允许 User.id = Moment.userId 字段关联，不允许 User.id = 82001 这种
			throw new IllegalArgumentException(joinType + "/.../" + table + "/" + originKey + " 不合法！join:'.../refKey'" + " 中 refKey 必须以 @ 结尾！");
		}

		if (originKey.endsWith("{}")) {
			setRelateType("{}");
			setKey(originKey.substring(0, originKey.length() - 2));
		}
		else if (originKey.endsWith("<>")) {
			setRelateType("<>");
			setKey(originKey.substring(0, originKey.length() - 2));
		}
		else {
			setRelateType("");
			setKey(originKey);
		}
	}


	public boolean isAppJoin() {
		return "@".equals(getJoinType());
	}
	public boolean isLeftJoin() {
		return "<".equals(getJoinType());
	}
	public boolean isRightJoin() {
		return ">".equals(getJoinType());
	}
	public boolean isCrossJoin() {
		return "*".equals(getJoinType());
	}
	public boolean isInnerJoin() {
		return "&".equals(getJoinType());
	}
	public boolean isFullJoin() {
		String jt = getJoinType();
		return "".equals(jt) || "|".equals(jt);
	}
	public boolean isOuterJoin() {
		return "!".equals(getJoinType());
	}
	public boolean isSideJoin() {
		return "^".equals(getJoinType());
	}
	public boolean isAntiJoin() {
		return "(".equals(getJoinType());
	}
	public boolean isForeignJoin() {
		return ")".equals(getJoinType());
	}

	public boolean isLeftOrRightJoin() {
		String jt = getJoinType();
		return "<".equals(jt) || ">".equals(jt);
	}
	
	public boolean canCacheViceTable() {
		String jt = getJoinType();
		return "@".equals(jt) || "<".equals(jt) || ">".equals(jt) || "&".equals(jt) || "*".equals(jt) || ")".equals(jt);
	}
	
	public boolean isSQLJoin() {
		return ! isAppJoin();
	}

	public static boolean isSQLJoin(Join j) {
		return j != null && j.isSQLJoin();
	}

	public static boolean isAppJoin(Join j) {
		return j != null && j.isAppJoin();
	}
	
	public static boolean isLeftOrRightJoin(Join j) {
		return j != null && j.isLeftOrRightJoin();
	}


}
