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

package zuo.biao.apijson.server;

/**自定义Entry
 * *java.util.Map.Entry是interface，new Entry(...)不好用，其它的Entry也不好用
 * @author Lemon
 * @param <K> key
 * @param <V> value
 * @use new Entry<K, V>(...)
 * @warn K,V都需要基本类型时不建议使用，判空麻烦，不如新建一个Model
 */
public class Entry<K, V> {

	public K key;
	public V value;

	public Entry() {
		//default
	}
	public Entry(K key) {
		this(key, null);
	}
	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}


	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}

	public boolean isEmpty() {
		return key == null && value == null;
	}

}
