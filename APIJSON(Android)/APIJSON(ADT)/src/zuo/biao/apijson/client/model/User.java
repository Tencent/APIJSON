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

package zuo.biao.apijson.client.model;



/**用户类
 * @author Lemon
 */
public class User {//extends BaseModel {
	public static final String TAG = "User";

//	private static final long serialVersionUID = 1L;

	public Long id;
	public Integer sex;
	public String head;
	public String name;
	public String phone;
	public String picture;

	/**默认构造方法，JSON等解析时必须要有
	 */
	public User() {
		super();
	}
	public User(Long id) {
		this();
		this.id = id;
	}
	public User(Long id, String name) {
		this(id);
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public User setId(Long id) {
		this.id = id;
		return this;
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
	public void setHead(String head) {
		this.head = head;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	////	@Override
	////	public boolean isCorrect() {//根据自己的需求决定，也可以直接 return true
	////		return id != null && id > 0;// && StringUtil.isNotEmpty(phone, true);
	////	}
	//
	//	@Override
	//	public JSONObject toJSONObject() {
	//		return toJSONObject(this);
	//	}
}
