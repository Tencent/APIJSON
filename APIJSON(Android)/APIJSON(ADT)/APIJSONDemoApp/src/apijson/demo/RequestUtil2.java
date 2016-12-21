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

package apijson.demo;

import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import apijson.demo.model.User;
import apijson.demo.model.Work;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.client.JSONObject;

/**RequestUtil is suggested
 * @author Lemon
 */
public class RequestUtil2 {


	/**
	 * @param object
	 * @return
	 */
	public static String getString(JSONObject object) {
		try {
			return URLEncoder.encode(JSON.toJSONString(object), UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * name = null, {"[]":object}
	 * @param object
	 * @return NonNull
	 */
	public static JSONObject getArrayRequest(JSONObject object) {
		return getArrayRequest(null, object);
	}
	/**
	 * name = null, {"name+"[]"":object}
	 * @param object
	 * @return NonNull
	 */
	public static JSONObject getArrayRequest(String name, JSONObject object) {
		return getObjectRequest((name == null ? "" : name) + "[]", object);
	}
	
	/**
	 * @param object
	 * @return
	 */
	public static <T> JSONObject getObjectRequest(T object) {
		return getObjectRequest(null, object);
	}
	/**
	 * @param name
	 * @param object
	 * @return NonNull
	 */
	public static <T> JSONObject getObjectRequest(String name, T object) {
		JSONObject jsonObject = new JSONObject();
		if (object != null) {
			jsonObject.put(StringUtil.isNotEmpty(name, true) ? name : object.getClass().getSimpleName(), object);
		}
		return jsonObject;
	}


	/**
	 * @param parent
	 * @param child
	 * @return
	 */
	public static <T> JSONObject put(JSONObject parent, T child) {
		if (parent == null) {
			parent = new JSONObject();
		}
		if (child != null) {
			parent.put(child.getClass().getSimpleName(), child);
		}
		return parent;
	}


	/**
	 * @return
	 */
	public static JSONObject newSingleRequest() {
		return getObjectRequest(new User((long) 38710));
	}
	/**
	 * @return
	 */
	public static JSONObject newRelyRequest() {
		JSONObject object = new JSONObject();

		object.put(User.class.getSimpleName(), new User((long) 38710));

		JSONObject workObject = new JSONObject();
		try {
			workObject.put("userId", URLEncoder.encode("User/id", UTF_8));//User.getFieldPath("id"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		object.put(Work.class.getSimpleName(), workObject);

		return object;
	}
	/**
	 * @return
	 */
	public static JSONObject newArrayRequest() {
		JSONObject object = new JSONObject();
		object.put("count", 10);
		object = put(object, new User().setSex(0));
		return getArrayRequest(object);
	}
	/**
	 * @return
	 */
	public static JSONObject newComplexRequest() {
		JSONObject userObject = new JSONObject();
		userObject.put("sex", 0);

		JSONObject workObject = new JSONObject();
		//		workObject.put("userId", "/User/id");
		try {
			workObject.put("userId", URLEncoder.encode("/User/id", UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject commentObject = new JSONObject();
		//		commentObject.put("workId", "[]/Work/id");
		try {
			commentObject.put("workId", URLEncoder.encode("[]/Work/id", UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject commentArrayObject = new JSONObject();
		commentArrayObject.put("page", 0);
		commentArrayObject.put("count", 3);
		commentArrayObject.put("Comment", commentObject);

		JSONObject arrayObject = new JSONObject();
		arrayObject.put("page", 1);
		arrayObject.put("count", 10);
		arrayObject.put("User", userObject);
		arrayObject.put("Work", workObject);
		try {
			arrayObject.put(URLEncoder.encode("Comment[]", UTF_8), commentArrayObject);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		return getArrayRequest(arrayObject);
	}

}
