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
	private String range; // any, all
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
