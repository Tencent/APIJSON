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

/**子查询 配置
 * @author Lemon
 */
public class Subquery {
	
	private String path; // any, all
	private String originKey;
	private JSONObject originValue;

	private String range; // any, all
	private String key; //id
	private SQLConfig config;
	
	
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
	public JSONObject getOriginValue() {
		return originValue;
	}
	public void setOriginValue(JSONObject originValue) {
		this.originValue = originValue;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public SQLConfig getConfig() {
		return config;
	}
	public void setConfig(SQLConfig config) {
		this.config = config;
	}

	

}
