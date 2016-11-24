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

package zuo.biao.apijson.client;

import zuo.biao.apijson.client.model.Comment;
import zuo.biao.apijson.client.model.User;
import zuo.biao.apijson.client.model.Work;

/**
 * @author Lemon
 */
public class RequestUtil {

	public static JSONObject newSingleRequest() {
		return new JSONRequest(new User((long) 38710));
	}

	public static JSONObject newRelyRequest() {
		JSONRequest request = new JSONRequest();
		request.put(new User((long) 70793));
		request.put(Work.class.getSimpleName(), new JSONRequest("userId", "User/id"));
		return request;
	}
	
	public static JSONObject newArrayRequest() {
		return new JSONRequest(new User().setSex(0)).toArray(10, 0, User.class.getSimpleName());
	}

	public static JSONObject newComplexRequest() {

		JSONRequest request = new JSONRequest();
		request.put(new User().setSex(0));
		request.put(Work.class.getSimpleName(), new JSONRequest("userId", "/User/id"));

		request.add(new JSONRequest(Comment.class.getSimpleName(), new JSONRequest("workId", "[]/Work/id")).
				toArray(3, 0, Comment.class.getSimpleName()));

		return request.toArray(10, 1);
	}

}
