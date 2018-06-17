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

import zuo.biao.apijson.MethodAccess;

import static zuo.biao.apijson.RequestRole.ADMIN;
import static zuo.biao.apijson.RequestRole.UNKNOWN;

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
		GET = {UNKNOWN},
		POST = {UNKNOWN, ADMIN},
		DELETE = {ADMIN}
		)
public class NetsbdNews extends BaseModel {
	private static final long serialVersionUID = 1L;

//	public static final int SEX_MAIL = 0;
//	public static final int SEX_FEMALE = 1;
//	public static final int SEX_UNKNOWN = 2;

//	private Integer sex; //性别
//	private String head; //头像url
//	private String name; //姓名
//	private String tag; //标签
//	private List<String> pictureList; //照片列表
//	private List<Long> contactIdList; //朋友列表

//	private Long uid;   //对应ims_user表中的uid，外键
    private Long cid;   //对应ims_netsbd_news_category表中的id，外键
	private Long uid;   //对应ims_users表中的id，外键  == 5 (0750kj)
	private String title;
	private String content;
	private String tag;
	private Integer ishide;
	private Integer sort;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getIshide() {
		return ishide;
	}

	public void setIshide(Integer ishide) {
		this.ishide = ishide;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**默认构造方法，JSON等解析时必须要有
	 */
	public NetsbdNews() {
		super();
	}
	public NetsbdNews(long id) {
		this();
		setId(id);
	}

}
