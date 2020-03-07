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

package apijson.server;

/**对请求JSON的操作
 * @author Lemon
 */
public enum Operation {

	/**
	 * 不允许传的字段，结构是
	 * "key0,key1,key2..."
	 */
	DISALLOW,
	
	/**
	 * 必须传的字段，结构是
	 * "key0,key1,key2..."
	 */
	NECESSARY,
	
	
	/**TODO 是否应该把数组类型写成 BOOLEANS, NUMBERS 等复数单词，以便抽取 enum ？扩展用 VERIFY 或 INSERT/UPDATE 远程函数等
	 * 验证是否符合预设的类型:
	 * BOOLEAN, NUMBER, DECIMAL, STRING, URL, DATE, TIME, DATETIME, OBJECT, ARRAY ] 
	 * 或它们的数组
	 * BOOLEAN[], NUMBER[], DECIMAL[], STRING[], URL[], DATE[], TIME[], DATETIME[], OBJECT[], ARRAY[]
	 * 结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 * 例如
	 * {
	 *   "id": "NUMBER", //id 类型必须为 NUMBER
	 *   "pictureList": "URL[]", //pictureList 类型必须为 URL[]
	 * }
	 * @see {@link Structure#type(String, String, Object, boolean)}
	 */
	TYPE,
	
	/**
	 * 验证是否符合预设的条件，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 * 例如
	 * {
	 *   "phone~": "PHONE",  //phone 必须满足 PHONE 的格式
	 *   "status{}": [1,2,3],  //status 必须在给出的范围内
	 *   "balance&{}":">0,<=10000"  //必须满足 balance>0 & balance<=10000
	 * }
	 */
	VERIFY,
	
	/**
	 * 验证是否不存在，除了本身的记录，结构是
	 * "key0,key1,key2..."
	 */
	UNIQUE,
	
	
	/**
	 * 添加，当要被添加的对象不存在时，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 */
	INSERT,
	
	/**
	 * 强行放入，不存在时就添加，存在时就修改，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 */
	UPDATE,
	
	/**
	 * 替换，当要被替换的对象存在时，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 */
	REPLACE,
	
	/**
	 * 移除，当要被移除的对象存在时，结构是
	 * "key0,key1,key2..."
	 */
	REMOVE;
	
}
