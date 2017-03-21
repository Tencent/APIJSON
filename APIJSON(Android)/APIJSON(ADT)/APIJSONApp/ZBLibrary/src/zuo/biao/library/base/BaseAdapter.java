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
import java.util.List;

import zuo.biao.library.interfaces.OnReachViewBorderListener;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.SettingUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**基础Adapter
 * @author Lemon
 * @warn 出于性能考虑，里面很多方法对变量(比如list)都没有判断，应在adapter外判断
 * @param <T> 数据模型(model/JavaBean)类
 * @use extends BaseAdapter<T>, 具体参考.DemoAdapter
 *      <br> 预加载使用：
 *      <br> 1.在子类getView中最后 return super.getView(position, convertView, parent);//非必须，只在预加载用到
 *      <br> 2.在使用子类的类中调用子类setOnReachViewBorderListener方法（这个方法就在这个类）//非必须
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
	//	private static final String TAG = "BaseAdapter";

	
	/**
	 * 管理整个界面的Activity实例
	 */
	public Activity context;
	/**
	 * 布局解释器,用来实例化列表的item的界面
	 */
	public LayoutInflater inflater;
	/**
	 * 资源获取器，用于获取res目录下的文件及文件中的内容等
	 */
	public Resources resources;
	public BaseAdapter(Activity context) {
		this.context = context;

		inflater = context.getLayoutInflater();
		resources = context.getResources();
	}

	/**
	 * 传进来的数据列表
	 */
	public List<T> list;
	/**刷新列表
	 */
	public synchronized void refresh(List<T> list) {
		this.list = list == null ? null : new ArrayList<T>(list);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}
	/**获取item数据
	 */
	@Override
	public T getItem(int position) {
		return list.get(position);
	}
	/**获取item的id，如果不能满足需求可在子类重写
	 * @param position
	 * @return position
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	
	//预加载，可不使用 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	protected OnReachViewBorderListener onReachViewBorderListener;
	/**设置到达parent的边界的监听
	 * @param onReachViewBorderListener
	 */
	public void setOnReachViewBorderListener(OnReachViewBorderListener onReachViewBorderListener) {
		this.onReachViewBorderListener = onReachViewBorderListener;
	}
	
	/**
	 * 预加载提前数
	 * @use 可在子类getView前赋值;
	 */
	protected int preloadCount = 1;

	/**获取item对应View的方法，带item滑到底部等监听
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 * @use 子类的getView中最后 return super.getView(position, convertView, parent);
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (SettingUtil.preload && onReachViewBorderListener != null && position >= getCount() - 1 - preloadCount) {
			onReachViewBorderListener.onReach(OnReachViewBorderListener.TYPE_BOTTOM, parent);
		}
		return convertView;
	}

	//预加载，可不使用 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	



	//show short toast 方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param stringResId
	 */
	public void showShortToast(int stringResId) {
		CommonUtil.showShortToast(context, stringResId);
	}
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param string
	 */
	public void showShortToast(String string) {
		CommonUtil.showShortToast(context, string);
	}
	//show short toast 方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**打开新的Activity，向左滑入效果
	 * @param intent
	 */
	public void toActivity(final Intent intent) {
		CommonUtil.toActivity(context, intent);
	}
	/**打开新的Activity
	 * @param intent
	 * @param showAnimation
	 */
	public void toActivity(final Intent intent, final boolean showAnimation) {
		CommonUtil.toActivity(context, intent, showAnimation);
	}
	/**打开新的Activity，向左滑入效果
	 * @param intent
	 * @param requestCode
	 */
	public void toActivity(final Intent intent, final int requestCode) {
		CommonUtil.toActivity(context, intent, requestCode);
	}
	/**打开新的Activity
	 * @param intent
	 * @param requestCode
	 * @param showAnimation
	 */
	public void toActivity(final Intent intent, final int requestCode, final boolean showAnimation) {
		CommonUtil.toActivity(context, intent, requestCode, showAnimation);
	}
	//启动新Activity方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
