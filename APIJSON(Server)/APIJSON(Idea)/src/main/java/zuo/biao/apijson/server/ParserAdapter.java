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

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.server.sql.SQLConfig;

/**
 * @author Lemon
 */
public interface ParserAdapter {

	/**
	 * @param path
	 * @return
	 */
	Object getTarget(@NotNull String path);

	/**
	 * @param path
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	JSON parseChild(@NotNull String path, @NotNull String key, @NotNull JSON value) throws Exception;
	
	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	JSONObject parseResponse(@NotNull JSONRequest request) throws Exception;
	
	/**
	 * @param path
	 * @param config
	 * @return
	 * @throws Exception
	 */
	JSONObject executeSQL(@NotNull String path, @NotNull SQLConfig config) throws Exception;
	
}
