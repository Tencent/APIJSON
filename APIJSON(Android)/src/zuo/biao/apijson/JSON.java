/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

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

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**阿里json封装类 防止解析时异常
 * @author Lemon
 */
public class JSON {
	private static final String TAG = "JSON";

	/**判断json格式是否正确
	 * @param s
	 * @return
	 */
	public static boolean isJsonCorrect(String s) {
		System.out.println(TAG + "isJsonCorrect  <<<<     " + s + "     >>>>>>>");
		if (s == null || s.equals("[]") 
				|| s.equals("{}") || s.equals("") || s.equals("[null]") || s.equals("{null}") || s.equals("null")) {
			return false;
		}
		return true;
	}

	/**获取有效的json
	 * @param s
	 * @return
	 */
	public static String getCorrectJson(String s) {
		s = StringUtil.getNoBlankString(s);
		return isJsonCorrect(s) ? s : "";
	}

	/**json转JSONObject
	 * @param s
	 * @return
	 */
	public static JSONObject parseObject(String s) {
		try {
			int features = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE;
			features |= Feature.SortFeidFastMatch.getMask();
			return com.alibaba.fastjson.JSON.parseObject(getCorrectJson(s), JSONObject.class, features);
		} catch (Exception e) {
			System.out.println(TAG + "parseObject  catch \n" + e.getMessage());
		}
		return null;
	}

	/**json转实体类
	 * @param s
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String s, Class<T> clazz) {
		try {
			int features = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE;
			features |= Feature.SortFeidFastMatch.getMask();
			return com.alibaba.fastjson.JSON.parseObject(getCorrectJson(s), clazz, features);
		} catch (Exception e) {
			System.out.println(TAG + "parseObject  catch \n" + e.getMessage());
		}
		return null;
	}
	
	/**json转实体类列表
	 * @param s
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String s, Class<T> clazz) {
		try {
			return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(s), clazz);
		} catch (Exception e) {
			System.out.println(TAG + "parseArray  catch \n" + e.getMessage());
		}
		return null;
	}

	/**实体类转json
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		try {
			return com.alibaba.fastjson.JSON.toJSONString(obj, SerializerFeature.SortField);
		} catch (Exception e) {
			System.out.println(TAG + "toJSONString  catch \n" + e.getMessage());
		}
		return null;
	}

}
