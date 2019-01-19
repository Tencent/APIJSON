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

import zuo.biao.apijson.StringUtil;

/**对请求JSON的操作
 * @author Lemon
 */
public enum Operation {

	/**
	 * 不允许传的字段
	 */
	DISALLOW,
	/**
	 * 必须传的字段
	 */
	NECESSARY,
	
	/**
	 * 验证是否符合预设的类型:
	 * Boolean, Long, Double, String, Object, Array //目前在业务表中还用不上，单一的类型校验已经够用 , JSON(包括 {Object}, [Array], "{Object}", "Array")
	 */
	TYPE,
	/**
	 * 验证是否符合预设的条件
	 */
	VERIFY,
	/**
	 * 验证是否不存在，除了本身的记录
	 */
	UNIQUE,
	
	/**
	 * 添加，当要被添加的对象不存在时
	 */
	ADD,
	/**
	 * 强行放入，不存在时就添加，存在时就修改
	 */
	UPDATE,
	@Deprecated
	PUT, //use UPDATE instead，容易和 RequestMethod.PUT 混淆，最快在 4.0.0 移除，请尽快修改 Request 表 structure 字段对应值里的 PUT
	/**
	 * 替换，当要被替换的对象存在时
	 */
	REPLACE,
	/**
	 * 移除，当要被移除的对象存在时
	 */
	REMOVE;
	
	public static Operation get(String name) {
		try {//Enum.valueOf只要找不到对应的值就会抛异常
			return Operation.valueOf(StringUtil.toUpperCase(name));
		} catch (Exception e) {
			//empty
		}
		return null;
	}
	
}
