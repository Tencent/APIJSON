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

package apijson.framework;

import apijson.NotNull;
import apijson.orm.ParserCreator;
import apijson.orm.SQLCreator;


/**SpringBootApplication
 * 右键这个类 > Run As > Java Application
 * @author Lemon
 */
public class APIJSONApplication {

	@NotNull
	public static APIJSONCreator DEFAULT_APIJSON_CREATOR;
	static {
		DEFAULT_APIJSON_CREATOR = new APIJSONCreator();
	}


	public static void init() throws Exception {
		init(true);
	}
	public static void init(boolean shutdownWhenServerError) throws Exception {
		init(shutdownWhenServerError, null, null);
	}
	public static void init(APIJSONCreator creator) throws Exception {
		init(false, creator);
	}
	public static void init(boolean shutdownWhenServerError, APIJSONCreator creator) throws Exception {
		init(shutdownWhenServerError, creator, creator);
	}
	public static void init(boolean shutdownWhenServerError, ParserCreator<Long> parserCreator, SQLCreator sqlCreator) throws Exception {
		System.out.println("\n\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON 开始启动 >>>>>>>>>>>>>>>>>>>>>>>>\n");

		if (parserCreator == null) {
			parserCreator = DEFAULT_APIJSON_CREATOR;
		}
		if (sqlCreator == null) {
			sqlCreator = DEFAULT_APIJSON_CREATOR;
		}

		System.out.println("\n\n\n开始初始化:远程函数配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			APIJSONFunctionParser.init(shutdownWhenServerError, parserCreator);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成初始化:远程函数配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		System.out.println("开始测试:远程函数 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			APIJSONFunctionParser.test();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:远程函数 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");



		System.out.println("\n\n\n开始初始化:请求校验配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			StructureUtil.init(shutdownWhenServerError, parserCreator);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成初始化:请求校验配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		System.out.println("\n\n\n开始测试:请求校验 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			StructureUtil.test(sqlCreator);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:请求校验 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");



		System.out.println("\n\n\n开始初始化:权限校验配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			APIJSONVerifier.init(shutdownWhenServerError, parserCreator);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成初始化:权限校验配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


		System.out.println("\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON 启动完成，试试调用自动化 API 吧 ^_^ >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

}
