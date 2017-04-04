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

package zuo.biao.apijson.server.model;

import java.util.List;

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.BaseModel;
import zuo.biao.apijson.RequestMethod;

/**用户类
 * @author Lemon
 */
@APIJSONRequest(
		method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.DELETE},
		PUT = "{disallowColumns:phone, necessaryColumns:id}",
		DELETE = "{necessaryColumns:id}"
		)
public class User extends BaseModel {
	private static final long serialVersionUID = -1635551656020732611L;

	public static final int SEX_MAIL = 0;
	public static final int SEX_FEMALE = 1;
	public static final int SEX_UNKNOWN = 2;


	private Integer sex; //性别
	private String head; //头像url
	private String name; //姓名
	private String phone; //手机
	private String picture; //照片列表
	private List<String> pictureList; //照片列表
	private String tag; //标签
	private Integer starred; //星标
	private List<Long> friendIdList; //照片列表

	/**默认构造方法，JSON等解析时必须要有
	 */
	public User() {
		super();
	}
	public User(long id) {
		this();
		setId(id);
	}

	public Integer getSex() {
		return sex;
	}
	public User setSex(Integer sex) {
		this.sex = sex;
		return this;
	}
	public String getHead() {
		return head;
	}
	public User setHead(String head) {
		this.head = head;
		return this;
	}
	public String getName() {
		return name;
	}
	public User setName(String name) {
		this.name = name;
		return this;
	}
	public String getPhone() {
		return phone;
	}
	public User setPhone(String phone) {
		this.phone = phone;
		return this;
	}
	public String getPicture() {
		return picture;
	}
	public User setPicture(String picture) {
		this.picture = picture;
		return this;
	}
	public List<String> getPictureList() {
		return pictureList;
	}
	public User setPictureList(List<String> pictureList) {
		this.pictureList = pictureList;
		return this;
	}

	public String getTag() {
		return tag;
	}
	public User setTag(String tag) {
		this.tag = tag;
		return this;
	}
	public Integer getStarred() {
		return starred;
	}
	public User setStarred(Integer starred) {
		this.starred = starred;
		return this;
	}

	public List<Long> getFriendIdList() {
		return friendIdList;
	}
	public User setFriendIdList(List<Long> friendIdList) {
		this.friendIdList = friendIdList;
		return this;
	}


}
