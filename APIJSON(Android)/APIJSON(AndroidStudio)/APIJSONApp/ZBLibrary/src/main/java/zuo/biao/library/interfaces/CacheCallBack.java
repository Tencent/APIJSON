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

package zuo.biao.library.interfaces;

/**缓存回调
 * @author Lemon
 * @param <T>
 * @use 见 .DemoListActivity 和 .UserListFragment
 */
public interface CacheCallBack<T> {
	/**
	 * 获取缓存的类，通常是数据模型(model/JavaBean)类
	 * @warn Entry<K, V>这种带类型(这里是K和V)的类不能作为返回值，应该用其它不带类型的类(比如.User)替换
	 * @return null-不缓存
	 */
	Class<T> getCacheClass();
	
	/**
	 * 获取缓存的分组
	 * @return 含非空字符的String ？缓存至对应class的group中 : 至缓存至对应class中
	 */
	String getCacheGroup();
	
	/**
	 * 获取缓存单个数据的id
	 * @param data
	 * @return data == null ? null : "" + data.getId(); //不用long是因为某些数据(例如订单)的id超出long的最大值
	 */
	String getCacheId(T data);
	
	/**
	 * 获取缓存每页数量
	 * @return > 0 ？缓存 : 不缓存
	 */
	int getCacheCount();
	
}