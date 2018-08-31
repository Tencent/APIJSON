package apijson.demo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**application
 * @author Lemon
 */
@SpringBootApplication
public class APIJSONApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(APIJSONApplication.class, args);
		System.out.println("\n\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON >>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println("开始测试:远程函数 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		System.out.println("\n完成测试:远程函数 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("\n\n\n开始测试:请求校验 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
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
