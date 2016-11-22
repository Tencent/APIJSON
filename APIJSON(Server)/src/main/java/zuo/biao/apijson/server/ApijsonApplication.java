package zuo.biao.apijson.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class ApijsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApijsonApplication.class, args);
//		ClientPost.main(null);
	}
}
