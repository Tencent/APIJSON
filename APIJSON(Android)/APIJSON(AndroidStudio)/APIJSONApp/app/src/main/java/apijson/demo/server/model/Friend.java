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

package apijson.demo.server.model;

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.BaseModel;
import zuo.biao.apijson.RequestMethod;

/**朋友类
 * @author Lemon
 * @see
 * <br >POST:<pre>
{
    "Friend":{
        "disallow":"state",
        "necessary":"userId,toUserId"
    }
}
 * </pre>
 * <br >PUT<pre>
{
    "Friend":{
        "disallow":"!",
        "necessary":"id,state"
    }
}
 * </pre>
 */
@APIJSONRequest(
		method = {RequestMethod.POST_HEAD, RequestMethod.POST, RequestMethod.PUT},
		POST_HEAD = "{}",
		POST = "{\"disallow\": \"state\", \"necessary\": \"userId,toUserId\"}",
		PUT = "{\"disallow\": \"!\", \"necessary\": \"id,state\"}"
		)
public class Friend extends BaseModel {
	private static final long serialVersionUID = -4478257698563522976L;

	public static final int STATE_SEND = 0;
	public static final int STATE_READ = 1;
	public static final int STATE_ACCEPT = 2;
	public static final int STATE_REFUSE = 4;


	private Long userId;//加好友方
	private Long toUserId;//被加好友方
	private String letter;//加友时稍的话
	private Integer state;//状态

	public Friend() {
		super();
	}

	public Friend(long id) {
		this();
		setId(id);
	}


	public Long getUserId() {
		return userId;
	}

	public Friend setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public Friend setToUserId(Long toUserId) {
		this.toUserId = toUserId;
		return this;
	}

	public String getLetter() {
		return letter;
	}

	public Friend setLetter(String letter) {
		this.letter = letter;
		return this;
	}

	public Integer getState() {
		return state;
	}

	public Friend setState(Integer state) {
		this.state = state;
		return this;
	}


}
