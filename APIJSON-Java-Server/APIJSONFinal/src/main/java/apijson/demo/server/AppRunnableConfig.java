package apijson.demo.server;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

import apijson.Log;

/**JFinalConfig
 * 右键这个类 > Run As > Java Application
 * @author Lemon
 * @see
 * <pre>
 * FIXME 目前在 http://apijson.org 请求会导致 JSON 解析问题，
 * 推测原因是这个 Demo 不支持跨域(HTTP OPTIONS请求报错)，导致 HttpKit.readData(getRequest()) 返回的字符串不符合 JSON 格式。
 * 可以先用 Postman 等工具，不加这个请求头直接发送 HTTP POST 传 JSON 参数来请求。
 * </pre>
 */
public class AppRunnableConfig extends JFinalConfig {

	public static void main(String[] args) {
		UndertowServer.start(AppRunnableConfig.class);
		
		Log.DEBUG = true; //上线生产环境前改为 false，可不输出 APIJSONORM 的日志 以及 SQLException 的原始(敏感)信息
		
		System.out.println("\n\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON 开始启动 >>>>>>>>>>>>>>>>>>>>>>>>\n");
		
		System.out.println("\n\n\n开始初始化:远程函数配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			DemoFunction.init(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成初始化:远程函数配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		System.out.println("开始测试:远程函数 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			DemoFunction.test();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:远程函数 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		
		
		System.out.println("\n\n\n开始初始化:请求校验配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			StructureUtil.init(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成初始化:请求校验配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		System.out.println("\n\n\n开始测试:请求校验 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			StructureUtil.test();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:请求校验 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		
		
		System.out.println("\n\n\n开始初始化:权限校验配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			DemoVerifier.init(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成初始化:权限校验配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		
		System.out.println("\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON 启动完成，试试调用自动化 API 吧 ^_^ >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

	

	public void configRoute(Routes me) {
		me.add("/", Controller.class);
	}

	public void configEngine(Engine me) {
		me.setBaseTemplatePath("webapp").setToClassPathSourceFactory();
	}

	public void configConstant(Constants me) {}
	public void configPlugin(Plugins me) {}
	public void configHandler(Handlers me) {}
	
	public void configInterceptor(Interceptors me) {
		me.add(new Interceptor() {
			
			@Override
			public void intercept(Invocation inv) {
				com.jfinal.core.Controller controller = inv.getController();
				controller.getResponse().addHeader("Access-Control-Allow-Origin", "*"); //允许的域名或IP地址
				inv.invoke();
			}
		});
		
	}
}
