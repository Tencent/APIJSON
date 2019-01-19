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

package zuo.biao.apijson;

/**请求方法，对应org.springframework.web.bind.annotation.RequestMethod，多出GETS,HEADS方法
 * @author Lemon
 */
public enum RequestMethod {

	/**
	 * 常规获取数据方式
	 */
	GET,
	
	/**
	 * 检查，默认是非空检查，返回数据总数
	 */
	HEAD,
	
	/**Safe, Single, Simple
	 * <br > 限制性GET，通过POST来GET数据，不显示请求内容和返回结果，并且校验请求，一般用于对安全要求比较高的请求
	 */
	GETS,
	
	/**Safe, Single, Simple
	 * <br > 限制性HEAD，通过POST来HEAD数据，不显示请求内容和返回结果，并且校验请求，一般用于对安全要求比较高的请求
	 */
	HEADS,
	
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
	DELETE;
	
	
	/**是否为GET请求方法
	 * @param method
	 * @param containPrivate 包含私密(非明文)获取方法GETS
	 * @return
	 */
	public static boolean isGetMethod(RequestMethod method, boolean containPrivate) {
		boolean is = method == null || method == GET;
		return containPrivate == false ? is : is || method == GETS;
	}
	
	/**是否为HEAD请求方法
	 * @param method
	 * @param containPrivate 包含私密(非明文)获取方法HEADS
	 * @return
	 */
	public static boolean isHeadMethod(RequestMethod method, boolean containPrivate) {
		boolean is = method == HEAD;
		return containPrivate == false ? is : is || method == HEADS;
	}
	
	/**是否为查询的请求方法
	 * @param method
	 * @return 读操作(GET型或HEAD型) - true, 写操作(POST,PUT,DELETE) - false
	 */
	public static boolean isQueryMethod(RequestMethod method) {
		return isGetMethod(method, true) || isHeadMethod(method, true);
	}
	
	/**是否为开放(不限制请求的结构或内容；明文，浏览器能直接访问及查看)的请求方法
	 * @param method
	 * @return
	 */
	public static boolean isPublicMethod(RequestMethod method) {
		return method == null || method == GET || method == HEAD;
	}

	public static String getName(RequestMethod method) {
		return method == null ? GET.name() : method.name();
	}
	
}
