package zuo.biao.apijson.server;
import java.util.Set;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;

public class ServerPost {
	private static final String TAG = "ServerPost:";

	public static final String SEPARATOR = StringUtil.SEPARATOR;


	public void post(String json) {
		System.out.println(TAG + TAG + "post  json = " + json);

		postObject(null, JSON.parseObject(json));
	}


	/**获取单个对象，该对象处于parentObject内
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return
	 */
	private JSONObject postObject(String name, JSONObject request) {
		System.out.println(TAG + "\npostObject:  name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {//key-value条件
			return null;
		}

		Set<String> set = request.keySet();

		if (set != null) {
			String value;
			JSONObject object;

			for (String key : set) {
				value = request.getString(key);
				object = JSON.parseObject(value);

				if (object == null) {//key - value

				} else {
					if (isArrayKey(key)) {//json array
						object.put(key, postArray(key, object));
					} else {//json object
						object.put(key, postObject(name, object));
					}
				}
			}
		}

		return request;
	}


	//TODO 如果获取key顺序不能保证就用一个"readOrder":{"user", "work", ...}确定顺序
	/**获取对象数组，该对象数组处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @param parentConfig 
	 * @param parseRelation
	 * @return
	 */
	private JSONArray postArray(String name, JSONObject request) {
		System.out.println(TAG + "\n\n\n postArray name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {//jsonKey-jsonValue条件
			return null;
		}

		JSONArray jsonArray = new JSONArray();

		int page = 0, count = 0;
		try {
			page = request.getIntValue("page");
			count = request.getIntValue("count");
		} catch (Exception e) {
			//			System.out.println(TAG + "postArray   try { page = arrayObject.postIntValue(page); ..." +
			//					" >> } catch (Exception e) {\n" + e.postMessage());
		}
		System.out.println(TAG + "page = " + page + "; count = " + count);

		QueryConfig config = new QueryConfig(count, page);

		Set<String> set = request.keySet();
		if (set != null) {
			JSONObject parent;
			String value;
			JSONObject child;
			for (int i = 0; i < count; i++) {
				parent = new JSONObject();
				for (String key : set) {
					value = request.getString(key);
					child = JSON.parseObject(value);
					if (child == null) {//key - value
						//array里不允许关联，只能在object中关联
					} else { 
						if (isArrayKey(key)) {//json array
							parent.put(key, postArray(key, child));
						} else {//json object
							parent.put(key, postObject(key, child));
						}
					}
				}
				jsonArray.add(parent);
			}
		}

		System.out.println(TAG + "postArray  jsonArray = " + JSON.toJSONString(jsonArray) + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n");

		return jsonArray;
	}


	public boolean isObject(String json) {
		return JSON.parseObject(json) != null;//json.startsWith("{") && json.endsWith("}");
	}
	public boolean isArrayKey(String key) {
		return key.endsWith("[]");//[key]改为了key[]，更符合常规逻辑。 key.startsWith("[") && key.endsWith("]");
	}

}
