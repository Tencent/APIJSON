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

import zuo.biao.apijson.JSON;
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
		data.setHead("http://common.cnblogs.com/images/icon_weibo_24.png");
		JSONRequest request = new JSONRequest();
		request.put(data);
		return request.setTag(User.class.getSimpleName());//;// 测试必须指定tag通过
	}
	
	public static JSONObject newDeleteRequest() {
		// 测试necessaryColumns = [id]通过
		// 测试对象不存在通过，存在返回success通过
		return new JSONRequest(new User(10000)).setTag(User.class.getSimpleName());//;// 测试必须指定tag通过
	}
	
	public static JSONObject newPutRequest() {
		User data = new User(38710);//);// 测试necessaryColumns = [id]通过
		data.setName("Lemon");
		//测试disallowColumns = [phone]通过 data.setPhone("1234567890");
		
		List<String> list = new ArrayList<String>();
		list.add("http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000");
		list.add("http://common.cnblogs.com/images/icon_weibo_24.png");
		list.add("http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000");
		data.setPicture(JSON.toJSONString(list));
		
		return new JSONRequest(data).setTag(User.class.getSimpleName());
	}

	
	
	public static JSONObject newSingleRequest() {
		return new JSONRequest(new User(38710));
	}
	
	public static JSONObject newColumnsRequest() {
		JSONObject object = new JSONObject(new User(38710));
		object.setColumns("id,name,phone");//StringUtil.getString(new String[]{"id", "name", "phone"}));//
		return new JSONRequest(User.class.getSimpleName(), object);
	}

	public static JSONObject newRelyRequest() {
		JSONRequest request = new JSONRequest();
		request.put(new User(70793));
		request.put(Work.class.getSimpleName(), new JSONRequest("userId", "User/id"));
		return request;
	}
	
	public static JSONObject newArrayRequest() {
		return new JSONRequest(new User()).toArray(5, 1, User.class.getSimpleName());
	}

	public static JSONObject newComplexRequest() {
		JSONRequest request = new JSONRequest();
		request.put(new User().setSex(0));
		request.put(Work.class.getSimpleName(), new JSONRequest("userId", "/User/id"));

		request.add(new JSONRequest(Comment.class.getSimpleName(), new JSONRequest("workId", "[]/Work/id")).
				toArray(3, 0, Comment.class.getSimpleName()));

		return request.toArray(2, 0);
	}

	public static JSONObject newAccessErrorRequest() {
		return new JSONRequest(new Wallet(38710));
	}
	
	public static JSONObject newAccessPermittedRequest() {
		JSONRequest request = new JSONRequest();
		request.put(new Wallet().setUserId((long) 38710));
		request.put("currentUserId", 38710);
		request.put("payPassword", "123456");
		return request;
	}

}
