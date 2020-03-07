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

package apijson.boot;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import apijson.Log;
import apijson.StringUtil;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.orm.FunctionParser;
import apijson.orm.Parser;
import apijson.orm.SQLConfig;
import apijson.orm.SQLExecutor;
import apijson.orm.Structure;
import apijson.orm.Verifier;


/**SpringBootApplication
 * 右键这个类 > Run As > Java Application
 * @author Lemon
 */
@Configuration
@SpringBootApplication
public class DemoApplication {

	static {
		Map<String, Pattern> COMPILE_MAP = Structure.COMPILE_MAP;
		COMPILE_MAP.put("PHONE", StringUtil.PATTERN_PHONE);
		COMPILE_MAP.put("EMAIL", StringUtil.PATTERN_EMAIL);
		COMPILE_MAP.put("ID_CARD", StringUtil.PATTERN_ID_CARD);
		
		APIJSONApplication.DEFAULT_APIJSON_CREATOR = new APIJSONCreator() {
			
			@Override
			public Parser<Long> createParser() {
				return new DemoParser();
			}
			@Override
			public FunctionParser createFunctionParser() {
				return new DemoFunctionParser();
			}
			
			@Override
			public Verifier<Long> createVerifier() {
				return new DemoVerifier();
			}
			
			@Override
			public SQLConfig createSQLConfig() {
				return new DemoSQLConfig();
			}
			@Override
			public SQLExecutor createSQLExecutor() {
				return new DemoSQLExecutor();
			}
		
		};
	}

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);

		Log.DEBUG = true; //上线生产环境前改为 false，可不输出 APIJSONORM 的日志 以及 SQLException 的原始(敏感)信息
		APIJSONApplication.init(true);
	}

	//SpringBoot 2.x 自定义端口方式
	//	@Bean
	//	public TomcatServletWebServerFactory servletContainer(){
	//		return new TomcatServletWebServerFactory(8081) ;
	//	}
	//SpringBoot 1.x 自定义端口方式，配置文件加 server.port=80 无效(MacOS 10.10.?)
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.setPort(8080); //自定义端口号，如果和 TiDB 等其它程序端口有冲突，可改为 8081, 9090, 9091 等未被占用的端口 	
			}
		};
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
