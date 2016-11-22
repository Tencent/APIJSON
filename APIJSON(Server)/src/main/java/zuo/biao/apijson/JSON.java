package zuo.biao.apijson;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**阿里json封装类 防止解析时异常
 * @author Young
 * @modifier Lemon
 */
public class JSON {
	private static final String TAG = "Json";

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


	// parseObject(String text, Class<PersonalInfoDO> clazz)

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

	// com.alibaba.fastjson.JSON.toJSONString
	public static String toJSONString(Object obj) {
		try {
			return com.alibaba.fastjson.JSON.toJSONString(obj, SerializerFeature.SortField);
		} catch (Exception e) {
			System.out.println(TAG + "toJSONString  catch \n" + e.getMessage());
		}
		return null;
	}

	// // com.alibaba.fastjson.JSON.toJSONString
	// public static <T> String toJSONString(List<T> list ){
	//
	// try {
	// return com.alibaba.fastjson.JSON.toJSONString(list);
	// } catch (Exception e) {
	// }
	// return null;
	// }
	// com.alibaba.fastjson.JSON.parseArray(json, NearbyCraftsManDO.class);
	public static <T> List<T> parseArray(String s, Class<T> clazz) {
		try {
			return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(s), clazz);
		} catch (Exception e) {
			System.out.println(TAG + "parseArray  catch \n" + e.getMessage());
		}
		return null;
	}

}
