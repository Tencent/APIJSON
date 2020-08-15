package apijson.demo;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import apijson.demo.application.DemoApplication;
import dalvik.system.DexFile;

public class MethodUtil extends apijson.demo.server.MethodUtil {

	static {
		CLASS_LOADER_CALLBACK = new ClassLoaderCallback() {

			@Override
			public Class<?> loadClass(String className) {
				return null;
			}

			@Override
			public List<Class<?>> loadClassList(String packageOrFileName, String className, boolean ignoreError) throws ClassNotFoundException, IOException {
				List<Class<?>> list = new ArrayList<Class<?>>();
				int index = className.indexOf("<");
				if (index >= 0) {
					className = className.substring(0, index);
				}

				boolean allPackage = isEmpty(packageOrFileName, true);
				boolean allName = isEmpty(className, true);

				//将包名替换成目录  TODO 应该一层层查找进去，实时判断是 package 还是 class，如果已经是 class 还有下一级，应该用 $ 隔开内部类。简单点也可以认为大驼峰是类
				String fileName = allPackage ? "" : separator2dot(packageOrFileName);

				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

				DexFile dex = new DexFile(DemoApplication.getInstance().getPackageResourcePath());
				Enumeration<String> entries = dex.entries();
				while (entries.hasMoreElements()) {
					String entryName = entries.nextElement();
					if (allPackage || entryName.startsWith(fileName)) {
						Class<?> entryClass = Class.forName(entryName, true, classLoader);

						if (allName || className.equals(entryClass.getSimpleName())) {
							list.add(entryClass);
						}
					}
				}

				return list;
			}
		};
	}

	public static JSONObject listMethod(String request) {
		return apijson.demo.server.MethodUtil.listMethod(request);
	}

	public static void invokeMethod(String request, Object instance, Listener<JSONObject> listener) throws Exception {
		apijson.demo.server.MethodUtil.invokeMethod(request, instance, listener);
	}

	public static void invokeMethod(JSONObject request, Object instance, Listener<JSONObject> listener) throws Exception {
		apijson.demo.server.MethodUtil.invokeMethod(request, instance, listener);
	}

}