package zuo.biao.apijson.server;

import zuo.biao.apijson.server.ServerGet2;

public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//		new ServerGet().get(\"{User\\":{\\"id\\":1}}\");
		//		
		//		new ServerGet().get(\"{[User]\\":{ \\"range\\":1, \\"pageNum\\":1,  \\"pageSize\\":10,  \\"sortOrder\\":\\"id\\",  \\"sortDirection\\":\\"increase\\",}}\");
		//		
		//		new ServerGet().get(\"{\\"[User, Work, [picture]]\\": { \\"=\\": { \\"User\\": id,  \\"Work\\": \\"userId\\",  \\"picture\\": \\"userId\\"}\\"pageNum\\": 1, \\"pageSize\\": 10, \\"sortOrder\\": \\"id\\", \\"sortDirection\\": \\"increase\\", \\"User\\": {  \\"sex\\":SEX_FEMAIL, } \\"picture\\":{ \\"pageNum\\":1, \\"pageSize\\": 3, }}\");


//		//已经过bejson校验和传输校验
//		new ServerGet2().get("{" +
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
		new ServerGet2().get("{" +
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
