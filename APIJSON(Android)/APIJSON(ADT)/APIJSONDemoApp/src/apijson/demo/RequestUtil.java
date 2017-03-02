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

import java.util.ArrayList;
import java.util.List;

import zuo.biao.apijson.client.JSONObject;
import zuo.biao.apijson.client.JSONRequest;
import apijson.demo.model.Comment;
import apijson.demo.model.User;
import apijson.demo.model.Wallet;
import apijson.demo.model.Work;

/**create request JSONObjects
 * @author Lemon
 */
public class RequestUtil {

	public static JSONObject newPostRequest() {
		User data = new User();//10000);// 测试disallowColumns = [id]通过
		data.setName("Tommy");// 测试necessaryColumns = [name,phone]通过
		data.setSex(0);
		data.setPhone("1234567890");// 测试necessaryColumns = [name,phone]通过
		//		data.setHead("http://common.cnblogs.com/images/icon_weibo_24.png");
		JSONRequest request = new JSONRequest();
		request.put(data);
		return request.setTag(User.class.getSimpleName());//;// 测试必须指定tag通过
	}

	public static JSONObject newPutRequest(long id) {
		User data = new User(id <= 0 ? 38710 : id);//);// 测试necessaryColumns = [id]通过
		data.setName("Lemon");
		//测试disallowColumns = [phone]通过 data.setPhone("1234567890");

		List<String> list = new ArrayList<String>();
		list.add("http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000");
		list.add("http://common.cnblogs.com/images/icon_weibo_24.png");
		data.setPictureList(list);

		return new JSONRequest(data).setTag(User.class.getSimpleName());
	}

	public static JSONObject newDeleteRequest(long id) {
		// 测试necessaryColumns = [id]通过
		// 测试对象不存在通过，存在返回success通过
		return new JSONRequest(new User(id <= 0 ? 10000 : id)).setTag(User.class.getSimpleName());//;// 测试必须指定tag通过
	}




	public static JSONObject newSingleRequest(long id) {
		return new JSONRequest(new User(id <= 0 ? 38710 : id));
	}

	public static JSONObject newColumnsRequest(long id) {
		JSONObject object = new JSONObject(new User(id <= 0 ? 38710 : id));
		object.setColumns("id,name,phone");//测试排序通过 //StringUtil.getString(new String[]{"id", "name", "phone"}));//
		return new JSONRequest(User.class.getSimpleName(), object);
	}

	public static JSONObject newRelyRequest(long id) {
		JSONRequest request = new JSONRequest();
		request.put(new User(id <= 0 ? 70793 : id));
		request.put(Work.class.getSimpleName(), new JSONRequest("userId@", "User/id"));
		return request;
	}

	public static JSONObject newArrayRequest() {
		return new JSONRequest(new User()).toArray(5, 1, User.class.getSimpleName());
	}

	public static JSONObject newComplexRequest() {
		JSONRequest request = new JSONRequest();
		request.put(new User().setSex(0));
		request.put(Work.class.getSimpleName(), new JSONRequest("userId@", "/User/id"));

		request.add(new JSONRequest(Comment.class.getSimpleName(), new JSONRequest("workId@", "[]/Work/id")).
				toArray(3, 0, Comment.class.getSimpleName()));

		return request.toArray(2, 0);
	}

	public static JSONObject newAccessErrorRequest(long id) {
		return new JSONRequest(new Wallet().setUserId(id <= 0 ? 38710 : id)).setTag(Wallet.class.getSimpleName());
	}

	public static JSONObject newAccessPermittedRequest(long id) {
		JSONRequest request = new JSONRequest();
		request.put(new Wallet().setUserId(id <= 0 ? 38710 : id));
		request.put("currentUserId", 38710);
		request.put("loginPassword", "apijson");
		return request.setTag(Wallet.class.getSimpleName());
	}

}
