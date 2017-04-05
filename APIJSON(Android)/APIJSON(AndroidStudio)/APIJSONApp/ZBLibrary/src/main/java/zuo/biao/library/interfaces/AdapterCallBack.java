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

import android.widget.BaseAdapter;

/**Adapter使用回调
 * @author Lemon
 * @param <A> adapter名称
 * @see #createAdapter
 * @see #refreshAdapter
 * @use implements AdapterCallBack<A>,具体参考.DemoListActivity和.DemoListFragment
 */
public interface AdapterCallBack<A extends BaseAdapter> {
	
	/**创建一个Adapter
	 * @return new A();
	 */
	A createAdapter();
	
	/**
	 * BaseAdapter#notifyDataSetChanged()有时无效，有时因列表更新不及时而崩溃，所以需要在自定义adapter内自定义一个刷新方法。
	 * 为什么不直接让自定义Adapter implement OnRefreshListener，从而直接 onRefreshListener.onRefresh(List<T> list) ？
	 * 因为这样的话会不兼容部分 Android SDK 或 第三方库的Adapter
	 */
	void refreshAdapter();
}
