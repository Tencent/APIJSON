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

package apijson.framework;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import apijson.StringUtil;

/**base model for reduce model codes
 * @author Lemon
 * @use extends BaseModel
 */
public abstract class BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;       //主键，唯一标识
	private Long userId;   //对应User表中的id，外键
	private String date;   //创建时间，JSON没有Date,TimeStamp类型，都会被转成Long，不能用！

	public Long getId() {
		return id;
	}
	public BaseModel setId(Long id) {
		this.id = id;
		return this;
	}
	public Long getUserId() {
		return userId;
	}
	public BaseModel setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	public String getDate() {
		return date;
	}
	public BaseModel setDate(String date) {
		this.date = date;
		return this;
	}
	
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	/**获取当前时间戳
	 * @return
	 */
	public static Timestamp currentTimeStamp() {  
	    return new Timestamp(new Date().getTime());  
	}
	/**获取时间戳 TODO 判空？ 还是要报错？
	 * @param time
	 * @return
	 */
	public static Timestamp getTimeStamp(String time) {
		return Timestamp.valueOf(time);
	}
	/**获取时间毫秒值 TODO 判空？ 还是要报错？
	 * @param time
	 * @return
	 */
	public static long getTimeMillis(String time) {
		return StringUtil.isEmpty(time, true) ? 0 : getTimeStamp(time).getTime();
	}
	
	
	//判断是否为空 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**判断array是否为空
	 * @param array
	 * @return
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length <= 0;
	}
	/**判断collection是否为空
	 * @param collection
	 * @return
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}
	/**判断map是否为空
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return map == null || map.isEmpty();
	}
	//判断是否为空 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	//判断是否包含 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**判断array是否包含a
	 * @param array
	 * @param a
	 * @return
	 */
	public static <T> boolean isContain(T[] array, T a) {
		return array == null ? false : Arrays.asList(array).contains(a);
	}
	/**判断collection是否包含object
	 * @param collection
	 * @param object
	 * @return
	 */
	public static <T> boolean isContain(Collection<T> collection, T object) {
		return collection != null && collection.contains(object);
	}
	/**判断map是否包含key
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K, V> boolean isContainKey(Map<K, V> map, K key) {
		return map != null && map.containsKey(key);
	}
	/**判断map是否包含value
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param value
	 * @return
	 */
	public static <K, V> boolean isContainValue(Map<K, V> map, V value) {
		return map != null && map.containsValue(value);
	}
	//判断是否为包含 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	//获取集合长度 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取数量
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> int count(T[] array) {
		return array == null ? 0 : array.length;
	}
	/**获取数量
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	public static <T> int count(Collection<T> collection) {
		return collection == null ? 0 : collection.size();
	}
	/**获取数量
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> int count(Map<K, V> map) {
		return map == null ? 0 : map.size();
	}
	//获取集合长度 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	//获取集合长度 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> T get(T[] array, int position) {
		return position < 0 || position >= count(array) ? null : array[position];
	}
	/**获取
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Collection<T> collection, int position) {
		return collection == null ? null : (T) get(collection.toArray(), position);
	}
	/**获取
	 * @param <K>
	 * @param <V>
	 * @param map null ? null
	 * @param key null ? null : map.get(key);
	 * @return
	 */
	public static <K, V> V get(Map<K, V> map, K key) {
		return key == null || map == null ? null : map.get(key);
	}
	//获取集合长度 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	
	//获取非基本类型对应基本类型的非空值 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static boolean value(Boolean value) {
		return value == null ? false : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static int value(Integer value) {
		return value == null ? 0 : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static long value(Long value) {
		return value == null ? 0 : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static float value(Float value) {
		return value == null ? 0 : value;
	}
	/**获取非空值
	 * @param value
	 * @return
	 */
	public static double value(Double value) {
		return value == null ? 0 : value;
	}
	//获取非基本类型对应基本类型的非空值 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/**index是否在arr长度范围内
	 * @param index
	 * @param array
	 * @return
	 */
	public static boolean isIndexInRange(Integer index, Object[] array) {
		return index != null && index >= 0 && index < count(array);
	}

	/**获取在arr长度范围内的index
	 * defaultIndex = 0
	 * @param index
	 * @param array
	 * @return
	 */
	public static int getIndexInRange(Integer index, Object[] array) {
		return getIndexInRange(index, array, 0);
	}
	/**获取在arr长度范围内的index
	 * @param index
	 * @param array
	 * @param defaultIndex
	 * @return
	 */
	public static int getIndexInRange(Integer index, Object[] array, int defaultIndex) {
		return isIndexInRange(index, array) ? index : defaultIndex;
	}

	/**获取在arr长度范围内的index
	 * defaultIndex = 0
	 * @param <T>
	 * @param index
	 * @param array
	 * @return
	 */
	public static <T> T getInRange(Integer index, T[] array) {
		return getInRange(index, array, 0);
	}
	/**获取在arr长度范围内的index
	 * @param <T>
	 * @param index
	 * @param array
	 * @param defaultIndex
	 * @return
	 */
	public static <T> T getInRange(Integer index, T[] array, int defaultIndex) {
		return get(array, getIndexInRange(index, array, defaultIndex));
	}

}
