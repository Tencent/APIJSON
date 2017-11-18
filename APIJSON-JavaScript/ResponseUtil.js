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

/**parser for response
 * @author Lemon
 * @see #getObject
 * @see #getList
 * @use JSONResponse response = new JSONResponse(json);
 * <br> User user = response.getObject(User.class);//not a must
 * <br> List<Comment> commentList = response.getList("Comment[]", Comment.class);//not a must
 */


//状态信息，非GET请求获得的信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

const CODE_SUCCESS = 200; //成功
const CODE_UNSUPPORTED_ENCODING = 400; //编码错误
const CODE_ILLEGAL_ACCESS = 401; //权限错误
const CODE_UNSUPPORTED_OPERATION = 403; //禁止操作
const CODE_NOT_FOUND = 404; //未找到
const CODE_ILLEGAL_ARGUMENT = 406; //参数错误
const CODE_NOT_LOGGED_IN = 407; //未登录
const CODE_TIME_OUT = 408; //超时
const CODE_CONFLICT = 409; //重复，已存在
const CODE_CONDITION_ERROR = 412; //条件错误，如密码错误
const CODE_UNSUPPORTED_TYPE = 415; //类型错误
const CODE_OUT_OF_RANGE = 416; //超出范围
const CODE_NULL_POINTER = 417; //对象为空
const CODE_SERVER_ERROR = 500; //服务器内部错误


const MSG_SUCCEED = "success"; //成功
const MSG_SERVER_ERROR = "Internal Server Error!"; //服务器内部错误


const KEY_CODE = "code";
const KEY_MSG = "msg";
const KEY_ID = "id";
const KEY_ID_IN = KEY_ID + "{}";
const KEY_COUNT = "count";
const KEY_TOTAL = "total";



/**是否成功
 * @param code
 * @return
 */
function isSuccess(code) {
  return code == CODE_SUCCESS;
}

/**校验服务端是否存在table
 * @param count
 * @return
 */
function isExist(count) {
  return count > 0;
}

//状态信息，非GET请求获得的信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>







/**格式化key名称
 * @param object
 * @return
 */
function formatObject(object) {
  //太长查看不方便，不如debug	 log(TAG, "format  object = \n" + JSON.toJSONString(object));
  if (object == null || object == '') {
    log(TAG, "format  object == null || object == '' >> return object;");
    return object;
  }
  let formattedObject = {};

  let value;
  for (let key in object) {
    value = object[key];

    if (value instanceof Array) { // JSONArray，遍历来format内部项
      formattedObject[replaceArray(key)] = formatArray(value);
    }
    else if (value instanceof Object) { // JSONObject，往下一级提取
      formattedObject[getSimpleName(key)] = formatObject(value);
    }
    else { // 其它Object，直接填充
      formattedObject[getSimpleName(key)] = value;
    }
  }

  //太长查看不方便，不如debug	 log(TAG, "format  return formattedObject = " + JSON.toJSONString(formattedObject));
  return formattedObject;
}

/**格式化key名称
 * @param array
 * @return
 */
function formatArray(array) {
  //太长查看不方便，不如debug	 log(TAG, "format  array = \n" + JSON.toJSONString(array));
  if (array == null || array == '') {
    log(TAG, "format  array == null || array == '' >> return array;");
    return array;
  }
  let formattedArray = [];

  let value;
  for (let i = 0; i < array.length; i++) {
    value = array[i];
    if (value instanceof Array) { // JSONArray，遍历来format内部项
      formattedArray.push(formatArray(value));
    }
    else if (value instanceof Object) { // JSONObject，往下一级提取
      formattedArray.push(formatObject(value));
    }
    else { // 其它Object，直接填充
      formattedArray.push(value);
    }
  }

  //太长查看不方便，不如debug	 log(TAG, "format  return formattedArray = " + JSON.toJSONString(formattedArray));
  return formattedArray;
}

/**替换key+KEY_ARRAY为keyList
 * @param key
 * @return getSimpleName(isArrayKey(key) ? getArrayKey(...) : key) {@link #getSimpleName(String)}
 */
function replaceArray(key) {
  if (isArrayKey(key)) {
    key = getArrayKey(key.substring(0, key.lastIndexOf('[]')));
  }
  return getSimpleName(key);
}
/**获取列表变量名
 * @param key => getNoBlankString(key)
 * @return empty ? "list" : key + "List" 且首字母小写
 */
function getArrayKey(key) {
  return addSuffix(key, "list");
}

/**获取简单名称
 * @param fullName name 或 name:alias
 * @return name => name; name:alias => alias
 */
function getSimpleName(fullName) {
  //key:alias  -> alias; key:alias[] -> alias[]
  let index = fullName == null ? -1 : fullName.indexOf(":");
  if (index >= 0) {
    fullName = fullName.substring(index + 1);
  }
  return fullName;
}


