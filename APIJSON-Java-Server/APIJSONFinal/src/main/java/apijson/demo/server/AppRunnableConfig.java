package apijson.demo.server;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

public class AppRunnableConfig extends JFinalConfig {

	public static void main(String[] args) {
                UndertowServer.start(AppRunnableConfig.class);
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
