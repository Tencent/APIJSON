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

package zuo.biao.apijson.server;

import zuo.biao.apijson.RequestMethod;

/**mock test of client
 * @author Lemon
 */
public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//		new RequestParser().get(\"{User\\":{\\"id\\":1}}\");
		//		
		//		new RequestParser().get(\"{[User]\\":{ \\"range\\":1, \\"pageNum\\":1,  \\"pageSize\\":10,  \\"sortOrder\\":\\"id\\",  \\"sortDirection\\":\\"increase\\",}}\");
		//		
		//		new RequestParser().get(\"{\\"[User, Work, [picture]]\\": { \\"=\\": { \\"User\\": id,  \\"Work\\": \\"userId\\",  \\"picture\\": \\"userId\\"}\\"pageNum\\": 1, \\"pageSize\\": 10, \\"sortOrder\\": \\"id\\", \\"sortDirection\\": \\"increase\\", \\"User\\": {  \\"sex\\":SEX_FEMAIL, } \\"picture\\":{ \\"pageNum\\":1, \\"pageSize\\": 3, }}\");


//		//已经过bejson校验和传输校验
//		new RequestParser().parse("{" +
////				     "\"[]\": {" +
////				        "\"page\": 1, " +
////				        "\"count\": 5," +
//				        " \"User\": {" +
//				               "\"id\": 38710," +//70793
////				               "\"range\": 1," +
////				               "\"sex\": 0" +
//				         "}," +
//				         "\"Work\": {" +
//				               "\"userId\": \"User/id\"" +
//				         "}," +
//				        "\"Comment[]\": {" +
//				              "\"page\": 0," +
//				              "\"count\": 3," +
//				              " \"Comment\": {" +
//				                   "\"workId\": \"Work/id\"" +
//		                      "}," +
//				         "}" +
////				    "}" +
//		"}");
		
		//已经过bejson校验和传输校验
		new RequestParser(RequestMethod.GET).parse("{" +
				     "\"[]\": {" +
				        "\"page\": 1, " +
				        "\"count\": 10," +
				        " \"User\": {" +
				               "\"sex\": 0" +
				         "}," +
				         "\"Work\": {" +
				               "\"userId\": \"/User/id\"" +
				         "}," +
				        "\"Comment[]\": {" +
				              "\"page\": 0," +
				              "\"count\": 3," +
				              " \"Comment\": {" +
				                   "\"workId\": \"[]/Work/id\"" +
		                      "}," +
				         "}" +
				    "}" +
		"}");


	}

}
