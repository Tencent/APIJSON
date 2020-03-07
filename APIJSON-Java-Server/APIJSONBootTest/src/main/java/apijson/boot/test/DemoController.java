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

package apijson.boot.test;

import java.net.URLDecoder;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.framework.APIJSONController;
import apijson.orm.Parser;


@RestController
@RequestMapping("")
public class DemoController extends APIJSONController {
	
	@Override
	public Parser<Long> newParser(HttpSession session, RequestMethod method) {
		return super.newParser(session, method).setNoVerify(true);
	}
	
	@PostMapping(value = "get")
	public String get(@RequestBody String request, HttpSession session) {
		return super.get(request, session);
	}

	/**获取
	 * 只为兼容HTTP GET请求，推荐用HTTP POST，可删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@RequestMapping("get/{request}")
	public String openGet(@PathVariable String request, HttpSession session) {
		try {
			request = URLDecoder.decode(request, StringUtil.UTF_8);
		} catch (Exception e) {
			// Parser会报错
		}
		return get(request, session);
	}
	
}