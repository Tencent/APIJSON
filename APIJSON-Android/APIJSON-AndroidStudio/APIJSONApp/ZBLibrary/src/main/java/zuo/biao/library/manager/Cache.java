/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import android.content.Context;
import android.content.SharedPreferences;

/**磁盘缓存类
 * @author Lemon
 * @param <T> 缓存的数据类
 * @use new Cache<T>(context, clazz, path).xxxMethod(...);具体参考.CacheManager
 */
public class Cache<T> {
	public static final String TAG = "Cache";

	public Cache(Context context, Class<T> clazz, String path) {
		this(context, clazz, context.getSharedPreferences(StringUtil.getTrimedString(path), Context.MODE_PRIVATE));
	}
	@SuppressWarnings("unused")
	private Context context;
	private Class<T> clazz;
	private SharedPreferences sp;
	public Cache(Context context, Class<T> clazz, SharedPreferences sp) {
		this.context = context;
		this.clazz = clazz;
		this.sp = sp;
	}



	/**获取列表大小
	 * @return
	 */
	public int getSize() {
		Map<String, ?> map = sp.getAll();
		return map == null ? 0 : map.size();
	}


	/**保存
	 * @param map
	 */
	public void saveList(Map<String, T> map) {
		if (map == null) {
			Log.e(TAG, "saveList  map == null >> return;");
			return;
		}
		Set<String> keySet = map.keySet();
		if (keySet != null) {
			for (String id: keySet) {
				save(id, map.get(id));
			}
		}
	}

	/**保存
	 * @param key
	 * @param value
	 */
	public void save(String key, T value) {
		if (StringUtil.isNotEmpty(key, true) == false || value == null) {
			Log.e(TAG, "save StringUtil.isNotEmpty(key, true) == false || value == null >> return;");
			return;
		}
		key = StringUtil.getTrimedString(key);

		sp.edit().remove(key).putString(key, JSON.toJSONString(value)).commit();
	}

	/**判断是否已存
	 * @param key
	 * @return
	 */
	public boolean isContain(String key) {
		if (StringUtil.isNotEmpty(key, true) == false) {
			Log.e(TAG, "isContain StringUtil.isNotEmpty(key, true) == false >> return false;");
			return false;
		}

		return sp.contains(StringUtil.getTrimedString(key));
	}

	/**获取
	 * @param key
	 * @return
	 */
	public T get(String key) {
		if (StringUtil.isNotEmpty(key, true) == false) {
			Log.e(TAG, "get (sp == null" +
					" || StringUtil.isNotEmpty(key, true) == false >> return null; ");
			return null;
		}

		return JSON.parseObject(sp.getString(StringUtil.getTrimedString(key), null), clazz);
	}


	/**ROOT
	 * 获取列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMap() {
		try {
			return (Map<String, String>) sp.getAll();
		} catch (Exception e) {
			Log.e(TAG, "getMap try { return (Map<String, String>) sp.getAll();" +
					"}catch(Exception e) {\n " + e.getMessage());
		}
		return null;
	}

	/**ROOT
	 * 获取列表
	 * @return
	 */
	public Set<String> getKeySet() {
		Map<String, String> map = getMap();
		return map == null ? null : map.keySet();
	}

	/**ROOT
	 * 获取列表
	 * @param start < 0 ? all : [start, end] 
	 * @param end
	 * @return
	 */
	public List<T> getValueList(int start, int end) {
		List<T> list = getAllValueList();
		return start < 0 || start > end || list == null || end >= list.size() ? list : list.subList(start, end);
	}
	/**ROOT
	 * 获取列表,顺序由keyList指定
	 * @param keyList
	 * @return
	 */
	public List<T> getValueList(List<String> keyList) {
		if (keyList != null) {
			List<T> list = new ArrayList<T>();
			T data;
			for (String key : keyList) {
				data = get(key);
				if (data != null) {
					list.add(data);
				}
			}
			return list;
		}
		return null;
	}
	/**ROOT
	 * 获取列表
	 * @return
	 */
	public List<T> getAllValueList() {
		Map<String, String> map = getMap();
		if (map != null) {
			List<T> list = new ArrayList<T>();
			T data;
			for (String value : map.values()) {
				data = JSON.parseObject(value, clazz);
				if (data != null) {
					list.add(data);
				}
			}
			return list;
		}
		return null;
	}

	/**删除
	 * @param key
	 */
	public void remove(String key) {
		if (StringUtil.isNotEmpty(key, true) == false) {
			Log.e(TAG, "deleteGroup  context == null " +
					" || StringUtil.isNotEmpty(groupName, true) == fal >> return;");
			return;
		}

		sp.edit().remove(StringUtil.getTrimedString(key)).commit();
	}


}
