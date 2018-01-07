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
