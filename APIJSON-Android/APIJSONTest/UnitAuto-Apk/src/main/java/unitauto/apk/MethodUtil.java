/*Copyright ©2020 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package unitauto.apk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;


/**针对 Apk 文件的方法/函数的工具类
 * @author Lemon
 */
public class MethodUtil extends unitauto.MethodUtil {

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

				DexFile dex = new DexFile(UnitAutoApp.getApp().getPackageResourcePath());
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

}