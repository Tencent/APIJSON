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

package apijson.demo.model;

import static zuo.biao.apijson.RequestRole.ADMIN;
import static zuo.biao.apijson.RequestRole.UNKNOWN;

import java.util.List;

import zuo.biao.apijson.MethodAccess;

/**用户类
 * @author Lemon
 * @see
 * <br >POST:post/register/user<pre>
{
    "User":{
        "disallow":"id",
        "necessary":"name,phone"
    },
    "necessary":"loginPassword,verify"
}
 * </pre>
 * <br >PUT:<pre>
{
    "User":{
        "disallow":"phone",
        "necessary":"id"
    }
}
 * </pre>
 * <br >PUT(User.phone):put/user/phone<pre>
{
    "User":{
        "disallow":"!",
        "necessary":"id,phone"
    },
    "necessary":"loginPassword,verify"
}
 * </pre>
 */
@MethodAccess(
		POST = {UNKNOWN, ADMIN},
		DELETE = {ADMIN}
		)
public class User extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	public static final int SEX_MAIL = 0;
	public static final int SEX_FEMALE = 1;
	public static final int SEX_UNKNOWN = 2;


	private Integer sex; //性别
	private String head; //头像url
	private String name; //姓名
	private String phone; //手机
	private String tag; //标签
	private List<String> pictureList; //照片列表
	private List<Long> contactIdList; //联系人列表

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

	public List<Long> getContactIdList() {
		return contactIdList;
	}
	public User setContactIdList(List<Long> contactIdList) {
		this.contactIdList = contactIdList;
		return this;
	}


}
