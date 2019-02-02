package apijson.demo.server;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

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
		
		System.out.println("\n\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON >>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println("开始测试:远程函数 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			DemoFunction.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:远程函数 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


		System.out.println("\n\n\n开始测试:请求校验 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			StructureUtil.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n完成测试:请求校验 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		System.out.println("\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON已启动 >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

	public void configRoute(Routes me) {
		me.add("/", Controller.class);
	}

	public void configEngine(Engine me) {
		me.setBaseTemplatePath("webapp").setToClassPathSourceFactory();
	}

	public void configConstant(Constants me) {}
	public void configPlugin(Plugins me) {}
	public void configInterceptor(Interceptors me) {}
	public void configHandler(Handlers me) {}
}
