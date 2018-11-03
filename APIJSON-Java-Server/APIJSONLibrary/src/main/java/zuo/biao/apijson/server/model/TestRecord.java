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

/**条件测试
 * @author Lemon
 */
@MethodAccess(GET = { LOGIN, ADMIN }, HEAD = { LOGIN, ADMIN })
public class TestRecord implements Serializable {
	  private static final long serialVersionUID = 1L;

	  private Long id; //唯一标识
	  private Long userId; //用户id
	  private Long documentId; //测试用例文档id
	  private Timestamp date; //创建日期
	  private String compare; //对比结果
	  private String response; //接口返回结果JSON  用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。
	  private String standard; //response 的校验标准，是一个 JSON 格式的 AST ，描述了正确 Response 的结构、里面的字段名称、类型、长度、取值范围 等属性。


	  public TestRecord() {
	    super();
	  }
	  public TestRecord(long id) {
	    this();
	    setId(id);
	  }




	  public Long getId() {
	    return id;
	  }

	  public TestRecord setId(Long id) {
	    this.id = id;
	    return this;
	  }

	  public Long getUserId() {
	    return userId;
	  }

	  public TestRecord setUserId(Long userId) {
	    this.userId = userId;
	    return this;
	  }

	  public Long getDocumentId() {
	    return documentId;
	  }

	  public TestRecord setDocumentId(Long documentId) {
	    this.documentId = documentId;
	    return this;
	  }

	  public Timestamp getDate() {
	    return date;
	  }

	  public TestRecord setDate(Timestamp date) {
	    this.date = date;
	    return this;
	  }

	  public String getCompare() {
	    return compare;
	  }

	  public TestRecord setCompare(String compare) {
	    this.compare = compare;
	    return this;
	  }

	  public String getResponse() {
	    return response;
	  }

	  public TestRecord setResponse(String response) {
	    this.response = response;
	    return this;
	  }

	  public String getStandard() {
	    return standard;
	  }

	  public TestRecord setStandard(String standard) {
	    this.standard = standard;
	    return this;
	  }


	}