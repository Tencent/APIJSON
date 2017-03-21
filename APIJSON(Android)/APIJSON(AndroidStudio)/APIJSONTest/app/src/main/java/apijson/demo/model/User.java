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

import java.util.List;

/**用户类
 * @author Lemon
 */
public class User extends BaseModel {
	private static final long serialVersionUID = -1635551656020732611L;
	
	public Integer sex;
	public String head;
	public String name;
	public String phone;
	public List<String> pictureList;

	/**默认构造方法，JSON等解析时必须要有
	 */
	public User() {
		super();
	}
	public User(long id) {
		this();
		this.id = id;
	}
	public User(long id, String name) {
		this(id);
		this.name = name;
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
	public List<String> getPictureList() {
		return pictureList;
	}
	public void setPictureList(List<String> pictureList) {
		this.pictureList = pictureList;
	}
}
