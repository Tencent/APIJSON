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

package apijson.demo;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.apijson.JSONObject;
import zuo.biao.apijson.JSONRequest;
import android.content.Context;
import apijson.demo.application.DemoApplication;
import apijson.demo.model.Comment;
import apijson.demo.model.Moment;
import apijson.demo.model.User;
import apijson.demo.model.Wallet;

/**请求工具类
 * 设置encode参数只为方便展示，实际使用时并不需要
 * @author Lemon
 */
public class RequestUtil {

	private static Context context;
	static {
		context = DemoApplication.getInstance();
	}

	private static final long DEFAULT_MOMENT_ID = 15;
	private static final long DEFAULT_USER_ID = 38710;



	public static JSONObject newPostRequest(boolean encode) {
		Moment data = new Moment();
		data.setUserId(DEFAULT_USER_ID);
		data.setContent(context.getString(R.string.apijson_slogan));
		List<String> list = new ArrayList<String>();
		list.add("http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000");
		list.add("http://common.cnblogs.com/images/icon_weibo_24.png");
		data.setPictureList(list);
		return new JSONRequest(data, encode).setTag(Moment.class.getSimpleName());
	}

	public static JSONObject newPutRequest(long id, boolean encode) {
		Moment data = new Moment(id <= 0 ? DEFAULT_MOMENT_ID : id);
		//		data.setContent(context.getString(R.string.apijson_info));//一般可用这种方式，encode是为了展示方便
		List<Long> list = new ArrayList<>();
		list.add((long) 10000);
		list.add((long) 10001);
		JSONObject momentObject = new JSONObject(data, encode);
		momentObject.put("praiseUserIdList+", list, encode);
		momentObject.put("content", context.getString(R.string.apijson_info), encode);
		return new JSONRequest(Moment.class.getSimpleName(), momentObject, encode).setTag(Moment.class.getSimpleName());
	}

	public static JSONObject newDeleteRequest(long id, boolean encode) {
		return new JSONRequest(new Moment(id <= 0 ? 10000 : id), encode).setTag(Moment.class.getSimpleName());
	}




	public static JSONObject newSingleRequest(long id, boolean encode) {
		return new JSONRequest(new Moment(id <= 0 ? DEFAULT_MOMENT_ID : id), encode);
	}

	public static JSONObject newColumnsRequest(long id, boolean encode) {
		JSONObject object = new JSONObject(new Moment(id <= 0 ? DEFAULT_MOMENT_ID : id), encode);
		object.setColumns("id,userId,content");
		return new JSONRequest(Moment.class.getSimpleName(), object, encode);
	}

	public static JSONObject newRelyRequest(long id, boolean encode) {
		JSONRequest request = new JSONRequest();
		request.put(new Moment(id <= 0 ? DEFAULT_MOMENT_ID : id), encode);
		request.put(User.class.getSimpleName(), new JSONRequest("id@", "Moment/userId", encode));
		return request;
	}

	public static JSONObject newArrayRequest(boolean encode) {
		JSONRequest dataObject = new JSONRequest();
		dataObject.put("name$", "%o%", encode);
		JSONRequest request = new JSONRequest(User.class.getSimpleName(), dataObject, encode);
		return request.toArray(5, 1, User.class.getSimpleName(), encode);
	}

	public static JSONObject newComplexRequest(boolean encode) {
		JSONRequest request = new JSONRequest();

		List<Long> idList = new ArrayList<Long>();
		idList.add(DEFAULT_USER_ID);
		idList.add((long) 93793);
		request.put(Moment.class.getSimpleName(), new JSONRequest("userId{}", idList, encode), encode);

		request.put(User.class.getSimpleName(), new JSONRequest("id@", "/Moment/userId", encode), encode);

		request.add(new JSONRequest(Comment.class.getSimpleName()
				, new JSONRequest("momentId@", "[]/Moment/id", encode), encode).
				toArray(3, 0, Comment.class.getSimpleName()), encode);

		return request.toArray(3, 0, encode);
	}

	public static JSONObject newAccessErrorRequest(boolean encode) {
		return new JSONRequest(new Wallet().setUserId(DEFAULT_USER_ID), encode)
		.setTag(Wallet.class.getSimpleName());
	}

	public static JSONObject newAccessPermittedRequest(boolean encode) {
		JSONRequest request = new JSONRequest();
		request.put(new Wallet().setUserId(DEFAULT_USER_ID), encode);
		request.put("currentUserId", DEFAULT_USER_ID, encode);
		request.put("loginPassword", "apijson", encode);
		return request.setTag(Wallet.class.getSimpleName());
	}

}
