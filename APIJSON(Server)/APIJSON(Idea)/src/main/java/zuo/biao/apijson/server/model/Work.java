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
import zuo.biao.apijson.RequestMethod;

/**作品类
 * @author Lemon
 */
@APIJSONRequest(
		method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
		DELETE = "{necessaryColumns:id}"
		)
public class Work extends BaseModel {
	private static final long serialVersionUID = -7437225320551780084L;

	private Long userId;
	private String content;
	private String picture;
	private List<String> pictureList;
	private List<Long> praiseUserIdList;
	private List<Long> commentIdList;

	public Work() {
		super();
	}
	public Work(long id) {
		this();
		setId(id);
	}

	public Long getUserId() {
		return userId;
	}
	public Work setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	public String getContent() {
		return content;
	}
	public Work setContent(String content) {
		this.content = content;
		return this;
	}
	public String getPicture() {
		return picture;
	}
	public Work setPicture(String picture) {
		this.picture = picture;
		return this;
	}
	public List<String> getPictureList() {
		return pictureList;
	}
	public Work setPictureList(List<String> pictureList) {
		this.pictureList = pictureList;
		return this;
	}
	public List<Long> getPraiseUserIdList() {
		return praiseUserIdList;
	}
	public Work setPraiseUserIdList(List<Long> praiseUserIdList) {
		this.praiseUserIdList = praiseUserIdList;
		return this;
	}
	public List<Long> getCommentIdList() {
		return commentIdList;
	}
	public Work setCommentIdList(List<Long> commentIdList) {
		this.commentIdList = commentIdList;
		return this;
	}
}
