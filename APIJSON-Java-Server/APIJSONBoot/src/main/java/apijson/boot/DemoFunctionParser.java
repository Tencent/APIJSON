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

package apijson.boot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.JSONResponse;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.RequestRole;
import apijson.StringUtil;
import apijson.framework.APIJSONFunctionParser;
import apijson.server.JSONRequest;


/**可远程调用的函数类
 * @author Lemon
 */
public class DemoFunctionParser extends APIJSONFunctionParser {
	public static final String TAG = "DemoFunctionParser";

	public DemoFunctionParser() {
		this(null, null, 0, null);
	}
	public DemoFunctionParser(RequestMethod method, String tag, int version, HttpSession session) {
		super(method, tag, version, session);
	}

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object verifyIdList(@NotNull JSONObject request, @NotNull String idList) throws Exception {
		Object obj = request.get(idList);
		if (obj instanceof Collection == false) {
			throw new IllegalArgumentException(idList + " 不符合 Array 类型! 结构必须是 [] ！");
		}
		JSONArray array = (JSONArray) obj;
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				if (array.get(i) instanceof Long == false && array.get(i) instanceof Integer == false) {
					throw new IllegalArgumentException(idList + " 内字符 " + array.getString(i) + " 不符合 Long 类型!");
				}
			}
		}
		return null;
	}


	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object verifyURLList(@NotNull JSONObject request, @NotNull String urlList) throws Exception {
		Object obj = request.get(urlList);
		if (obj instanceof Collection == false) {
			throw new IllegalArgumentException(urlList + " 不符合 Array 类型! 结构必须是 [] ！");
		}
		JSONArray array = (JSONArray) obj;
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				if (StringUtil.isUrl(array.getString(i)) == false) {
					throw new IllegalArgumentException(urlList + " 内字符 " + array.getString(i) + " 不符合 URL 格式!");
				}
			}
		}
		return null;
	}


	/**
	 * @param rq
	 * @param momentId
	 * @return
	 * @throws Exception
	 */
	public int deleteCommentOfMoment(@NotNull JSONObject rq, @NotNull String momentId) throws Exception {
		long mid = rq.getLongValue(momentId);
		if (mid <= 0 || rq.getIntValue(JSONResponse.KEY_COUNT) <= 0) {
			return 0;
		}

		JSONRequest request = new JSONRequest();

		//Comment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONRequest comment = new JSONRequest();
		comment.put("momentId", mid);

		request.put("Comment", comment);
		//Comment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		JSONObject rp = new DemoParser(RequestMethod.DELETE).setNoVerify(true).parseResponse(request);

		JSONObject c = rp.getJSONObject("Comment");
		return c == null ? 0 : c.getIntValue(JSONResponse.KEY_COUNT);
	}


	/**删除评论的子评论
	 * @param rq
	 * @param toId
	 * @return
	 */
	public int deleteChildComment(@NotNull JSONObject rq, @NotNull String toId) throws Exception {
		long tid = rq.getLongValue(toId);
		if (tid <= 0 || rq.getIntValue(JSONResponse.KEY_COUNT) <= 0) {
			return 0;
		}

		//递归获取到全部子评论id

		JSONRequest request = new JSONRequest();

		//Comment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONRequest comment = new JSONRequest();
		comment.put("id{}", getChildCommentIdList(tid));

		request.put("Comment", comment);
		//Comment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		JSONObject rp = new DemoParser(RequestMethod.DELETE).setNoVerify(true).parseResponse(request);

		JSONObject c = rp.getJSONObject("Comment");
		return c == null ? 0 : c.getIntValue(JSONResponse.KEY_COUNT);
	}


	private JSONArray getChildCommentIdList(long tid) {

		JSONArray arr = new JSONArray();

		JSONRequest request = new JSONRequest();

		//Comment-id[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONRequest idItem = new JSONRequest();

		//Comment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONRequest comment = new JSONRequest();
		comment.put("toId", tid);
		comment.setColumn("id");
		idItem.put("Comment", comment);
		//Comment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		request.putAll(idItem.toArray(0, 0, "Comment-id"));
		//Comment-id[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		JSONObject rp = new DemoParser().setNoVerify(true).parseResponse(request);

		JSONArray a = rp.getJSONArray("Comment-id[]");
		if (a != null) {
			arr.addAll(a);

			JSONArray a2;
			for (int i = 0; i < a.size(); i++) {

				a2 = getChildCommentIdList(a.getLongValue(i));
				if (a2 != null) {
					arr.addAll(a2);
				}
			}
		}

		return arr;
	}


	/**TODO 仅用来测试 "key-()":"getIdList()" 和 "key()":"getIdList()"
	 * @param request
	 * @return JSONArray 只能用JSONArray，用long[]会在SQLConfig解析崩溃
	 * @throws Exception
	 */
	public JSONArray getIdList(@NotNull JSONObject request) {
		return new JSONArray(new ArrayList<Object>(Arrays.asList(12, 15, 301, 82001, 82002, 38710)));
	}


	/**TODO 仅用来测试 "key-()":"verifyAccess()"
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object verifyAccess(@NotNull JSONObject request) throws Exception {
		long userId = request.getLongValue(JSONRequest.KEY_USER_ID);
		RequestRole role = RequestRole.get(request.getString(JSONRequest.KEY_ROLE));
		if (role == RequestRole.OWNER && userId != DemoVerifier.getVisitorId(getSession())) {
			throw new IllegalAccessException("登录用户与角色OWNER不匹配！");
		}
		return null;
	}




}