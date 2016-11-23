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

/**作品类，名字对应服务器数据库的table_name，变量名及其类型和table里的column一一对应
 * @author Lemon
 */
public class Work extends BaseModel {
	private static final long serialVersionUID = -7437225320551780084L;

	public long userId;
	public String title;
	public String content;
	public String picture;

	public Work() {
		super();
	}
	public Work(long id, String title) {
		this();
		this.id = id;
		this.title = title;
	}

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}


//	@Override
//	public boolean isCorrect() {
//		return getId() > 0 && getUserId() > 0
//				&& (StringUtil.isNotEmpty(getTitle(), true) || StringUtil.isNotEmpty(getContent(), true));
//	}

}
