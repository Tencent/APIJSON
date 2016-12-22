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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import com.alibaba.fastjson.JSONObject;
import zuo.biao.apijson.JSON;

/**config model for query
 * @author Lemon
 */
public class QueryConfig {

	private String table;
	private String[] columns;
	private String[][] values;
	private Map<String, Object> where;
	private int limit;
	private int page;
	private int position;

	public QueryConfig() {
	}
	public QueryConfig(String table) {
		this();
		setTable(table);
	}
	public QueryConfig(String table, Map<String, Object> where) {
		this(table);
		setWhere(where);
	}
	public QueryConfig(String table, String[] columns, String[][] values) {
		this(table);
		setColumns(columns);
		setValues(values);
	}
	public QueryConfig(int limit, int page) {
		this();
		setLimit(limit);
		setPage(page);
	}


	public String getTable() {
		return table;
	}
	public QueryConfig setTable(String table) {
		this.table = table;
		return this;
	}
	public String[] getColumns() {
		return columns;
	}
	public QueryConfig setColumns(String[] columns) {
		this.columns = columns;
		return this;
	}
	public String[][] getValues() {
		return values;
	}
	public QueryConfig setValues(String[][] values) {
		this.values = values;
		return this;
	}
	public Map<String, Object> getWhere() {
		return where;
	}
	public QueryConfig setWhere(Map<String, Object> where) {
		this.where = where;
		return this;
	}
	public int getLimit() {
		return limit;
	}
	public QueryConfig setLimit(int limit) {
		this.limit = limit;
		return this;
	}
	public int getPage() {
		return page;
	}
	public QueryConfig setPage(int page) {
		this.page = page;
		return this;
	}
	public int getPosition() {
		return position;
	}
	public QueryConfig setPosition(int position) {
		this.position = position;
		return this;
	}

	/**获取限制数量
	 * @return
	 */
	public String getLimitString() {
		return getLimitString(page, limit);// + 1);
	}
	/**获取限制数量
	 * @param limit
	 * @return
	 */
	public static String getLimitString(int page, int limit) {
		return limit <= 0 ? "" : " limit " + page*limit + ", " + limit;
	}

	/**获取筛选方法
	 * @return
	 */
	public String getWhereString() {
		return getWhereString(where);
	}
	/**获取筛选方法
	 * @param where
	 * @return
	 */
	public static String getWhereString(Map<String, Object> where) {
		Set<String> set = where == null ? null : where.keySet();
		if (set != null && set.size() > 0) {
			String whereString = " where ";
			for (String key : set) {
				//避免筛选到全部	value = key == null ? null : where.get(key);
				if (key == null) {
					continue;
				}
				whereString += (key + "=" + where.get(key) + " &");
			}
			if (whereString.endsWith("&")) {
				whereString = whereString.substring(0, whereString.length() - 1);
			}
			if (whereString.trim().endsWith("where") == false) {
				return whereString;
			}
		}
		return "";
	}


	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	public static synchronized QueryConfig getQueryConfig(String table, JSONObject request) {
		Set<String> set = request == null ? null : request.keySet();
		Map<String, Object> transferredRequest = null;
		if (set != null) {
			transferredRequest = new HashMap<String, Object>();
			for (String key : set) {
				if (JSON.parseObject(request.getString(key)) == null) {//非key-value
					transferredRequest.put(key, request.get(key));
				}
			}
		}
		return new QueryConfig(table).setWhere(transferredRequest);
	}
	
	/**
	 * @return
	 */
	public String getSQL() {
		return getSQL(this);
	}
	/**
	 * @param config
	 * @return
	 */
	public static String getSQL(QueryConfig config) {
		return config == null ? null : "select * from " + config.getTable() + config.getWhereString() + config.getLimitString();
	}

}
