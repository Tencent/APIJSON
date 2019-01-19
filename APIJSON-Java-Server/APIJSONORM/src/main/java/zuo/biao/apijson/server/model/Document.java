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

import static zuo.biao.apijson.RequestRole.ADMIN;
import static zuo.biao.apijson.RequestRole.LOGIN;

import java.io.Serializable;
import java.sql.Timestamp;

import zuo.biao.apijson.MethodAccess;

/**测试用例文档
 * @author Lemon
 */
@MethodAccess(
		GET = { LOGIN, ADMIN }, 
		HEAD = { LOGIN, ADMIN },
		PUT = { LOGIN, ADMIN }
		)
public class Document implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id; //唯一标识
	private Long userId; //用户id  应该用adminId，只有当登录账户是管理员时才能操作文档。  需要先建Admin表，新增登录等相关接口。
	private Integer version; //接口版本号  <=0 - 不限制版本，任意版本都可用这个接口  >0 - 在这个版本添加的接口
	private String name; //接口名称
	private String url; //请求地址
	private String request; //请求  用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。
	private Timestamp date; //创建日期
	private String response; //标准返回结果


	public Document() {
		super();
	}
	public Document(long id) {
		this();
		setId(id);
	}




	public Long getId() {
		return id;
	}

	public Document setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public Document setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public Integer getVersion() {
		return version;
	}

	public Document setVersion(Integer version) {
		this.version = version;
		return this;
	}

	public String getName() {
		return name;
	}

	public Document setName(String name) {
		this.name = name;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Document setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getRequest() {
		return request;
	}

	public Document setRequest(String request) {
		this.request = request;
		return this;
	}

	public Timestamp getDate() {
		return date;
	}

	public Document setDate(Timestamp date) {
		this.date = date;
		return this;
	}

	public String getResponse() {
		return response;
	}

	public Document setResponse(String response) {
		this.response = response;
		return this;
	}


}