/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson;

import java.util.Collection;
import java.util.Map;

/**可远程调用的函数列表
 * @author Lemon
 */
public interface FunctionList {

	//判断是否为空 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**判断collection是否为空
	 * @param collection
	 * @return
	 */
	public <T> boolean isEmpty(Collection<T> collection);
	/**判断map是否为空
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public <K, V> boolean isEmpty(Map<K, V> map);
	//判断是否为空 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	//判断是否为包含 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**判断collection是否包含object
	 * @param collection
	 * @param object
	 * @return
	 */
	public <T> boolean isContain(Collection<T> collection, T object);
	/**判断map是否包含key
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	public <K, V> boolean isContainKey(Map<K, V> map, K key);
	/**判断map是否包含value
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param value
	 * @return
	 */
	public <K, V> boolean isContainValue(Map<K, V> map, V value);
	//判断是否为包含 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	//获取集合长度 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取数量
	 * @param <T>
	 * @param array
	 * @return
	 */
	public <T> int count(T[] array);
	/**获取数量
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	public <T> int count(Collection<T> collection);
	/**获取数量
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public <K, V> int count(Map<K, V> map);
	//获取集合长度 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	//获取集合长度 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取
	 * @param <T>
	 * @param array
	 * @return
	 */
	public <T> T get(T[] array, int position);
	/**获取
	 * @param <T>
	 * @param collection List, Vector, Set等都是Collection的子类
	 * @return
	 */
	public <T> T get(Collection<T> collection, int position);
	/**获取
	 * @param <K>
	 * @param <V>
	 * @param map null ? null
	 * @param key null ? null : map.get(key);
	 * @return
	 */
	public <K, V> V get(Map<K, V> map, K key);
	//获取集合长度 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	
	//获取非基本类型对应基本类型的非空值 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取非空值
	 * @param value
	 * @return
	 */
	public boolean value(Boolean value);
	/**获取非空值
	 * @param value
	 * @return
	 */
	public int value(Integer value);
	/**获取非空值
	 * @param value
	 * @return
	 */
	public long value(Long value);
	/**获取非空值
	 * @param value
	 * @return
	 */
	public float value(Float value);
	/**获取非空值
	 * @param value
	 * @return
	 */
	public double value(Double value);
	//获取非基本类型对应基本类型的非空值 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}