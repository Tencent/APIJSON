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

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.BaseModel;
import zuo.biao.apijson.RequestMethod;

/**评论类
 * @author Lemon
 * @see
* <br >POST:<pre>
{
 "Comment":{
     "disallowColumns":"id",
     "necessaryColumns":"userId,momentId,content"
 }
}
 * </pre>
 */
@APIJSONRequest(
		method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.DELETE},
		POST = "{\"disallowColumns\": \"id\", \"necessaryColumns\": \"userId,momentId,content\"}",
		DELETE = "{\"necessaryColumns\": \"id\"}"
		)
public class Comment extends BaseModel {
	private static final long serialVersionUID = -1011007127735372824L;
	
	private Long momentId;
	private Long parentId;
	private String content;
	private Long userId;
	private Long toUserId;
	public Comment() {
		super();
	}
	public Comment(long id) {
		this();
		setId(id);
	}
	
	public Long getMomentId() {
		return momentId;
	}
	public Comment setMomentId(Long momentId) {
		this.momentId = momentId;
		return this;
	}
	public Long getParentId() {
		return parentId;
	}
	public Comment setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}
	public String getContent() {
		return content;
	}
	public Comment setContent(String content) {
		this.content = content;
		return this;
	}
	public Long getUserId() {
		return userId;
	}
	public Comment setUserId(Long userId) {
		this.momentId = userId;
		return this;
	}
	public Long getToUserId() {
		return toUserId;
	}
	public Comment setToUserId(Long toUserId) {
		this.toUserId = toUserId;
		return this;
	}
}
