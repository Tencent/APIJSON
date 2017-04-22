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

package apijson.demo.client.model;

import apijson.demo.client.application.APIJSONApplication;

/**用户类
 * @author Lemon
 */
public class User extends apijson.demo.client.server.model.User {
	/**
	 */
	private static final long serialVersionUID = -8778034378026642371L;


	public User() {
		super();
	}
	public User(long id) {
		super(id);
	}

	@Override
	public Long getId() {
		return value(super.getId());
	}
	@Override
	public Long getDate() {
		return value(super.getDate());
	}

	@Override
	public Integer getStarred() {
		return value(super.getStarred());
	}

	@Override
	public Integer getSex() {
		return value(super.getSex());
	}



	/**判断是否为当前用户的朋友
	 * @return
	 */
	public boolean isFriend() {
		return isFriend(APIJSONApplication.getInstance().getCurrentUserId());
	}
	/**判断是否为朋友，双方friendIdList都必须包含对方id
	 * @param user
	 * @return
	 */
	public boolean isFriend(User user) {
		return isFriend(this, user);
	}
	/**判断是否为朋友，双方friendIdList都必须包含对方id
	 * @param user0
	 * @param user1
	 * @return
	 */
	public static boolean isFriend(User user0, User user1) {
		return user0 != null && user1 != null && isFriend(user0, user1.getId()) && isFriend(user1, user0.getId());
	}
	/**判断是否为当前用户的朋友，仅从单方的friendIdList判断
	 * @param id
	 * @return
	 */
	public boolean isFriend(long id) {
		return isFriend(this, id);
	}
	/**判断是否为朋友，仅从单方的friendIdList判断
	 * @param user0
	 * @param id1
	 * @return
	 */
	public static boolean isFriend(User user0, long id1) {
		//id
		if (id1 <= 0) {
			return false;
		}
		long id0 = user0 == null ? 0 : user0.getId();
		if (id0 <= 0) {
			return false;
		}

		//friendIdList
		return isContain(user0.getFriendIdList(), id1);
	}

}