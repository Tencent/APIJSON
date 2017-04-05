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

package zuo.biao.library.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import zuo.biao.library.R;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnStopLoadListener;
import zuo.biao.library.manager.CacheManager;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.StringUtil;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

/**基础列表Activity
 * @author Lemon
 * @param <T> 数据模型(model/JavaBean)类
 * @param <LV> AbsListView的子类（ListView,GridView等）
 * @param <BA> 管理LV的Adapter
 * @see #lvBaseList
 * @see #initCache
 * @see #initView
 * @see #getListAsync
 * @see #onRefresh
 * @use extends BaseListActivity 并在子类onCreate中调用onRefresh(...), 具体参考.DemoListActivity
 * *缓存使用：在initData前调用initCache(...), 具体参考 .DemoListActivity(onCreate方法内)
 */
public abstract class BaseListActivity<T, LV extends AbsListView, BA extends BaseAdapter> extends BaseActivity {
	private static final String TAG = "BaseListActivity";

	private OnStopLoadListener onStopLoadListener;
	/**设置停止加载监听
	 * @param onStopLoadListener
	 */
	protected void setOnStopLoadListener(OnStopLoadListener onStopLoadListener) {
		this.onStopLoadListener = onStopLoadListener;
	}


	private CacheCallBack<T> cacheCallBack;
	/**初始化缓存
	 * @warn 在initData前使用才有效
	 * @param cacheCallBack
	 */
	protected void initCache(CacheCallBack<T> cacheCallBack) {
		this.cacheCallBack = cacheCallBack;
	}




	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 显示列表的ListView
	 * @warn 只使用lvBaseList为显示列表数据的AbsListView(ListView,GridView等)，不要在子类中改变它
	 */
	protected LV lvBaseList;
	/**
	 * 管理LV的Item的Adapter
	 */
	protected BA adapter;
	/**
	 * 如果在子类中调用(即super.initView());则view必须含有initView中初始化用到的id且id对应的View的类型全部相同；
	 * 否则必须在子类initView中重写这个类中initView内的代码(所有id替换成可用id)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initView() {// 必须调用

		lvBaseList = (LV) findViewById(R.id.lvBaseList);
	}

	/**设置adapter
	 * @param adapter
	 */
	public void setAdapter(BA adapter) {
		this.adapter = adapter;
		lvBaseList.setAdapter(adapter);
	}

	/**显示列表（已在UI线程中），一般需求建议直接调用setList(List<T> l, AdapterCallBack<BA> callBack)
	 * @param list
	 */
	public abstract void setList(List<T> list);

	/**显示列表（已在UI线程中）
	 * @param list
	 */
	public void setList(AdapterCallBack<BA> callBack) {
		if (adapter == null) {
			setAdapter(callBack.createAdapter());
		}
		callBack.refreshAdapter();
	}


	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private boolean isToSaveCache;
	private boolean isToLoadCache;
	@Override
	public void initData() {// 必须调用

		isToSaveCache = SettingUtil.cache && cacheCallBack != null && cacheCallBack.getCacheClass() != null;
		isToLoadCache = isToSaveCache && StringUtil.isNotEmpty(cacheCallBack.getCacheGroup(), true);
	}

	/**
	 * 获取列表，在非UI线程中
	 * @must 获取成功后调用onLoadSucceed
	 * @param page 在onLoadSucceed中传回来保证一致性
	 */
	public abstract void getListAsync(int page);



	public void loadData(int page) {
		loadData(page, isToLoadCache);
	}

	/**
	 * 数据列表
	 */
	private List<T> list;
	/**
	 * 正在加载
	 */
	protected boolean isLoading = false;
	/**
	 * 还有更多可加载数据
	 */
	protected boolean isHaveMore = true;
	/**
	 * 加载页码，每页对应一定数量的数据
	 */
	private int page;
	private int loadCacheStart;
	/**加载数据，用getListAsync方法发请求获取数据
	 * @param page_
	 * @param isCache
	 */
	private void loadData(int page_, final boolean isCache) {
		if (isLoading) {
			Log.w(TAG, "loadData  isLoading >> return;");
			return;
		}
		isLoading = true;
		isSucceed = false;

		if (page_ <= HttpManager.PAGE_NUM_0) {
			page_ = HttpManager.PAGE_NUM_0;
			isHaveMore = true;
			loadCacheStart = 0;//使用则可像网络正常情况下的重载，不使用则在网络异常情况下不重载（导致重载后加载数据下移）
		} else {
			if (isHaveMore == false) {
				stopLoadData(page_);
				return;
			}
			loadCacheStart = list == null ? 0 : list.size();
		}
		this.page = page_;
		Log.i(TAG, "loadData  page_ = " + page_ + "; isCache = " + isCache
				+ "; isHaveMore = " + isHaveMore + "; loadCacheStart = " + loadCacheStart);

		runThread(TAG + "loadData", new Runnable() {

			@Override
			public void run() {
				if (isCache == false) {//从网络获取数据
					getListAsync(page);
				} else {//从缓存获取数据
					onLoadSucceed(page, CacheManager.getInstance().getList(cacheCallBack.getCacheClass()
							, cacheCallBack.getCacheGroup(), loadCacheStart, cacheCallBack.getCacheCount()),
							true);
					if (page <= HttpManager.PAGE_NUM_0) {
						isLoading = false;//stopLoadeData在其它线程isLoading = false;后这个线程里还是true
						loadData(page, false);
					}
				}
			}
		});
	}

	/**停止加载数据
	 * isCache = false;
	 * @param page
	 */
	public synchronized void stopLoadData(int page) {
		stopLoadData(page, false);
	}
	/**停止加载数据
	 * @param page
	 * @param isCache
	 */
	private synchronized void stopLoadData(int page, boolean isCache) {
		Log.i(TAG, "stopLoadData  isCache = " + isCache);
		isLoading = false;
		dismissProgress();

		if (isCache) {
			Log.d(TAG, "stopLoadData  isCache >> return;");
			return;
		}

		if (onStopLoadListener == null) {
			Log.w(TAG, "stopLoadData  onStopLoadListener == null >> return;");
			return;
		}
		onStopLoadListener.onStopRefresh();
		if (page > HttpManager.PAGE_NUM_0) {
			onStopLoadListener.onStopLoadMore(isHaveMore);
		}
	}



	private boolean isSucceed = false;
	/**处理列表
	 * @param page
	 * @param newList 新数据列表
	 * @param isCache
	 * @return
	 * @return
	 */
	public synchronized void handleList(int page, List<T> newList, boolean isCache) {
		if (newList == null) {
			newList = new ArrayList<T>();
		}
		isSucceed = ! newList.isEmpty();
		Log.i(TAG, "\n\n<<<<<<<<<<<<<<<<<\n handleList  newList.size = " + newList.size() + "; isCache = " + isCache
				+ "; page = " + page + "; isSucceed = " + isSucceed);

		if (page <= HttpManager.PAGE_NUM_0) {
			Log.i(TAG, "handleList  page <= HttpManager.PAGE_NUM_0 >>>>  ");
			saveCacheStart = 0;
			list = new ArrayList<T>(newList);
			if (isCache == false && list.isEmpty() == false) {
				Log.i(TAG, "handleList  isCache == false && list.isEmpty() == false >>  isToLoadCache = false;");
				isToLoadCache = false;
			}
		} else {
			Log.i(TAG, "handleList  page > HttpManager.PAGE_NUM_0 >>>>  ");
			if (list == null) {
				list = new ArrayList<T>();
			}
			saveCacheStart = list.size();
			isHaveMore = ! newList.isEmpty();
			if (isHaveMore) {
				list.addAll(newList);
			}
		}

		Log.i(TAG, "handleList  list.size = " + list.size() + "; isHaveMore = " + isHaveMore
				+ "; isToLoadCache = " + isToLoadCache + "; saveCacheStart = " + saveCacheStart
				+ "\n>>>>>>>>>>>>>>>>>>\n\n");
	}



	/**加载成功
	 * isCache = false;
	 * @param page
	 * @param newList
	 */
	public synchronized void onLoadSucceed(final int page, final List<T> newList) {
		onLoadSucceed(page, newList, false);
	}
	/**加载成功
	 * @param page
	 * @param newList
	 * @param isCache newList是否为缓存
	 */
	private synchronized void onLoadSucceed(final int page, final List<T> newList, final boolean isCache) {
		runThread(TAG + "onLoadSucceed", new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "onLoadSucceed  page = " + page + "; isCache = " + isCache + " >> handleList...");
				handleList(page, newList, isCache);

				runUiThread(new Runnable() {

					@Override
					public void run() {
						stopLoadData(page, isCache);
						setList(list);
					}
				});

				if (isToSaveCache && isCache == false) {
					saveCache(newList);
				}
			}
		});
	}

	/**加载失败
	 * @param page
	 * @param e
	 */
	public synchronized void onLoadFailed(int page, Exception e) {
		Log.e(TAG, "onLoadFailed page = " + page + "; e = " + (e == null ? null : e.getMessage()));
		stopLoadData(page);
		showShortToast(R.string.get_failed);
	}




	//缓存<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	//	/**
	//	 * 获取缓存每页数量
	//	 * @return > 0 ？缓存 : 不缓存
	//	 */
	//	public int getCacheCount() {
	//		//让给服务器返回每页数量为count的数据，不行的话在子类重写 Math.max(10, newList == null ? 0 : newList.size());
	//		return CacheManager.MAX_PAGE_SIZE;
	//	}

	private int saveCacheStart;
	/**保存缓存
	 * @param newList
	 */
	public synchronized void saveCache(List<T> newList) {
		if (cacheCallBack == null || newList == null || newList.isEmpty()) {
			Log.e(TAG, "saveCache  cacheCallBack == null || newList == null || newList.isEmpty() >> return;");
			return;
		}

		LinkedHashMap<String, T> map = new LinkedHashMap<String, T>();
		for (T data : newList) {
			if (data != null) {
				map.put(cacheCallBack.getCacheId(data), data);//map.put(null, data);不会崩溃
			}
		}

		CacheManager.getInstance().saveList(cacheCallBack.getCacheClass(), cacheCallBack.getCacheGroup()
				, map, saveCacheStart, cacheCallBack.getCacheCount());
	}
	//缓存>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {

	}


	/**刷新（从头加载）
	 * @must 在子类onCreate中调用，建议放在最后
	 */
	public void onRefresh() {
		loadData(HttpManager.PAGE_NUM_0);
	}
	/**加载更多
	 */
	public void onLoadMore() {
		if (isSucceed == false && page <= HttpManager.PAGE_NUM_0) {
			Log.w(TAG, "onLoadMore  isSucceed == false && page <= HttpManager.PAGE_NUM_0 >> return;");
			return;
		}
		loadData(page + (isSucceed ? 1 : 0));
	}


	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	protected void onDestroy() {
		isLoading = false;
		isHaveMore = false;
		isToSaveCache = false;
		isToLoadCache = false;

		super.onDestroy();

		lvBaseList = null;
		list = null;

		onStopLoadListener = null;
		cacheCallBack = null;
	}

	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	// Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}