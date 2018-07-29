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

import com.alibaba.fastjson.JSONObject;

/**JOIN 配置
 * @author Lemon
 */
public class Join {

	private String joinType; // "&" - INNER, "|" - FULL/UNION, "<" - LEFT, ">" - RIGHT
	private String relateType; // "" - 一对一, "{}" - 一对多, "<>" - 多对一
	private JSONObject table; // { "id@":"/Moment/userId" }
	private String name; //User
	private String key; //id
	private String targetName; // Moment
	private String targetKey; // userId

	private String joinSQL; //TODO 是否必要? 直接SQLConfig.getJoinString时生成？   LEFT JOIN sys.apijson_user AS User ON User.id = Moment.userId， 都是用 = ，通过relateType处理缓存
	private SQLConfig joinConfig;
	private SQLConfig cacheConfig;


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

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JSONObject getTable() {
		return table;
	}
	public void setTable(JSONObject table) {
		this.table = table;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetKey() {
		return targetKey;
	}
	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}


	public String getJoinSQL() {
		return joinSQL;
	}
	public void setJoinSQL(String joinSQL) {
		this.joinSQL = joinSQL;
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


	public String getJoinTypeName() throws Exception {
		return getJoinTypeName(joinType);
	}
	public static String getJoinTypeName(String type) throws Exception {
		switch (type) {
		case "&":
			return " INNER JOIN ";
		case "<":
			return " LEFT JOIN ";
		case ">":
			return " RIGIHT JOIN ";
		case "":
		case "|":
			return " FULL JOIN ";
		default:
			throw new UnsupportedOperationException("服务器内部错误：不支持JOIN类型 " + type + " !");
		}
	}

}
