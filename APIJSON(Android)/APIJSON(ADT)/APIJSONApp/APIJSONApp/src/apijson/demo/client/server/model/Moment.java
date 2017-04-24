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

package apijson.demo.client.server.model;

import java.util.List;

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.BaseModel;
import zuo.biao.apijson.RequestMethod;

/**作品类
 * @author Lemon
 */
@APIJSONRequest(
		method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
		DELETE = "{necessaryColumns:id}"
		)
public class Moment extends BaseModel {
	private static final long serialVersionUID = -7437225320551780084L;

	private Long userId;
	private String content;
	private List<String> pictureList;
	private List<Long> praiseUserIdList;

	public Moment() {
		super();
	}
	public Moment(long id) {
		this();
		setId(id);
	}

	public Long getUserId() {
		return userId;
	}
	public Moment setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	public String getContent() {
		return content;
	}
	public Moment setContent(String content) {
		this.content = content;
		return this;
	}
	public String getFirstPicture() {
		return get(pictureList, 0);
	}
	public List<String> getPictureList() {
		return pictureList;
	}
	public Moment setPictureList(List<String> pictureList) {
		this.pictureList = pictureList;
		return this;
	}
	public List<Long> getPraiseUserIdList() {
		return praiseUserIdList;
	}
	public Moment setPraiseUserIdList(List<Long> praiseUserIdList) {
		this.praiseUserIdList = praiseUserIdList;
		return this;
	}

}
