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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**request receiver and controller
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class Controller {

	@RequestMapping("get/{request}")
	public String get(@PathVariable String request) {
		return new RequestParser(RequestMethod.GET).parse(request);
	}

	@RequestMapping(value="post", method = RequestMethod.POST)
	public String post(@RequestBody String request) {
		return new RequestParser(RequestMethod.POST).parse(request);
	}

	/**用POST方法GET数据，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value="post_get", method = RequestMethod.POST)
	public String post_get(@RequestBody String request) {
		return new RequestParser(RequestMethod.GET).parse(request);
	}
	
	/**以下接口继续用POST接口是为了客户端方便，只需要做get，post请求。也可以改用实际对应的方法。
	 * post，put方法名可以改为add，update等更客户端容易懂的名称
	*/
	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(@RequestBody String request) {
		return new RequestParser(RequestMethod.DELETE).parse(request);
	}
	
	@RequestMapping(value="put", method = RequestMethod.POST)
	public String put(@RequestBody String request) {
		return new RequestParser(RequestMethod.PUT).parse(request);
	}

}
