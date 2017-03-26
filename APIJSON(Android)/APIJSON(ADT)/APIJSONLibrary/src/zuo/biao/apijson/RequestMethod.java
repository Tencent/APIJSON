/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson;

/**请求方法，对应org.springframework.web.bind.annotation.RequestMethod，多出一个POST_GET方法
 * @author Lemon
 */
public enum RequestMethod {

	/**
	 * 常规获取数据方式
	 */
	GET,
	
	/**
	 * 通过POST来HEAD数据，不显示请求内容和返回结果，一般用于对安全要求比较高的请求
	 */
	POST_HEAD,
	
	/**
	 * 通过POST来GET数据，不显示请求内容和返回结果，一般用于对安全要求比较高的请求
	 */
	POST_GET,
	
	/**
	 * 新增(或者说插入)数据
	 */
	POST,
	
	/**
	 * 修改数据，只修改传入字段对应的值
	 */
	PUT,
	
	/**
	 * 删除数据
	 */
	DELETE,
	
	/**
	 * 检查，默认是非空检查，返回数据总数
	 */
	HEAD
}
