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

package apijson.demo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import zuo.biao.apijson.server.Structure;

/**application
 * @author Lemon
 */
@SpringBootApplication
public class APIJSONApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(APIJSONApplication.class, args);

		System.out.println("\n\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON >>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println("开始测试:远程函数 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			Function.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:远程函数 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

//		Structure.init();
		
		System.out.println("\n\n\n开始测试:请求校验 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			Structure.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:请求校验 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		System.out.println("\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON已启动 >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}



	//支持JavaScript跨域请求<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/** 
	 * 跨域过滤器 
	 * @return 
	 */  
	@Bean  
	public CorsFilter corsFilter() {  
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);  
	}  
	/**CORS跨域配置
	 * @return
	 */
	private CorsConfiguration buildConfig() {  
		CorsConfiguration corsConfiguration = new CorsConfiguration();  
		corsConfiguration.addAllowedOrigin("*"); //允许的域名或IP地址
		corsConfiguration.addAllowedHeader("*"); //允许的请求头
		corsConfiguration.addAllowedMethod("*"); //允许的HTTP请求方法
		corsConfiguration.setAllowCredentials(true); //允许发送跨域凭据，前端Axios存取JSESSIONID必须要
		return corsConfiguration;  
	}  
	//支持JavaScript跨域请求 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
