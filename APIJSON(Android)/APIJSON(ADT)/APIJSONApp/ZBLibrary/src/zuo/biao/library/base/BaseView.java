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

import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

/**基础自定义View
 * @author Lemon
 * @param <T> 数据模型(model/JavaBean)类
 * @see #onDestroy
 * @use extends BaseView<T>, 具体参考.DemoView
 */
public abstract class BaseView<T> {
	private static final String TAG = "BaseView";

	/**
	 * 传入的Activity,可在子类直接使用
	 */
	public Activity context;
	public Resources resources;
	public BaseView(Activity context, Resources resources) {
		this.context = context;
		this.resources = resources;
	}

	/**点击View的事件监听回调，主要是为了activity或fragment间接通过adapter接管baseView的点击事件
	 * @param <T>
	 * @param <BV>
	 * @must 子类重写setOnClickListener方法且view.setOnClickListener(listener)事件统一写在这个方法里面
	 */
	public interface OnViewClickListener<T, BV extends BaseView<T>> {
		/**onClick(v)事件由这个方法接管
		 * @param v
		 * @param bv
		 */
		void onViewClick(View v, BV bv);
	}

	/**数据改变回调接口
	 * (Object) getData() - 改变的数据
	 */
	public interface OnDataChangedListener {
		void onDataChanged();
	}

	public OnDataChangedListener onDataChangedListener;//数据改变回调监听回调的实例
	/**设置数据改变事件监听回调
	 * @param listener
	 */
	public void setOnDataChangedListener(OnDataChangedListener listener) {
		onDataChangedListener = listener;
	}

	public OnTouchListener onTouchListener;//接触View回调监听回调的实例
	/**设置接触View事件监听回调
	 * @param listener
	 */
	public void setOnTouchListener(OnTouchListener listener) {
		onTouchListener = listener;
	}

	public OnClickListener onClickListener;//点击View回调监听回调的实例
	/**设置点击View事件监听回调
	 * @param listener
	 */
	public void setOnClickListener(OnClickListener listener) {
		onClickListener = listener;
		if (onClickViewList != null) {
			for (View v : onClickViewList) {
				if (v != null) {
					v.setOnClickListener(listener);
				}
			}
		}
	}

	public OnLongClickListener onLongClickListener;//长按View回调监听回调的实例
	/**设置长按View事件监听回调
	 * @param listener
	 */
	public void setOnLongClickListener(OnLongClickListener listener) {
		onLongClickListener = listener;
	}


	/**
	 * 子类整个视图,可在子类直接使用
	 * @must createView方法内对其赋值且不能为null
	 */
	protected View convertView = null;

	protected List<View> onClickViewList;
	/**通过id查找并获取控件，使用时不需要强转
	 * @param id
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public <V extends View> V findViewById(int id) {
		return (V) convertView.findViewById(id);
	}
	/**通过id查找并获取控件，并setOnClickListener
	 * @param id
	 * @param listener
	 * @return
	 */
	public <V extends View> V findViewById(int id, OnClickListener listener) {
		V v = findViewById(id);
		v.setOnClickListener(listener);
		if (onClickViewList == null) {
			onClickViewList = new ArrayList<View>();
		}
		onClickViewList.add(v);
		return v;
	}

	/**
	 * 视图类型，部分情况下需要根据viewType使用不同layout，对应Adapter的itemViewType
	 */
	protected int viewType = 0;
	/**
	 * data在列表中的位置
	 * @must 只使用bindView(int position, T data)方法来设置position，保证position与data对应正确
	 */
	protected int position = 0;
	/**获取data在列表中的位置
	 */
	public int getPosition() {
		return position;
	}

	/**创建一个新的View
	 * @param inflater - @NonNull，布局解释器
	 * @param viewType - 视图类型，部分情况下需要根据viewType使用不同layout
	 * @return
	 */
	public View createView(LayoutInflater inflater, int position, int viewType) {
		this.position = position;
		this.viewType = viewType;
		return createView(inflater);
	}
	/**创建一个新的View
	 * @param inflater - @NonNull，布局解释器
	 * @return
	 */
	public abstract View createView(LayoutInflater inflater);

	/**获取convertView的宽度
	 * @warn 只能在createView后使用
	 * @return
	 */
	public int getWidth() {
		return convertView.getWidth();
	}
	/**获取convertView的高度
	 * @warn 只能在createView后使用
	 * @return
	 */
	public int getHeight() {
		return convertView.getHeight();
	}



	protected T data = null;
	/**获取数据
	 * @return
	 */
	public T getData() {
		return data;
	}


	/**设置并显示内容，建议在子类bindView内this.data = data;
	 * @warn 只能在createView后使用
	 * @param data - 传入的数据
	 * @param position - data在列表中的位置
	 * @param viewType - 视图类型，部分情况下需要根据viewType使用不同layout
	 */
	public void bindView(T data, int position, int viewType) {
		this.position = position;
		this.viewType = viewType;
		bindView(data);
	}
	/**设置并显示内容，建议在子类bindView内this.data = data;
	 * @warn 只能在createView后使用
	 * @param data - 传入的数据
	 */
	public abstract void bindView(T data);

	/**获取可见性
	 * @warn 只能在createView后使用
	 * @return 可见性 (View.VISIBLE, View.GONE, View.INVISIBLE);
	 */
	public int getVisibility() {
		return convertView.getVisibility();
	}
	/**设置可见性
	 * @warn 只能在createView后使用
	 * @param visibility - 可见性 (View.VISIBLE, View.GONE, View.INVISIBLE);
	 */
	public void setVisibility(int visibility) {
		convertView.setVisibility(visibility);
	}


	/**设置背景
	 * @warn 只能在createView后使用
	 * @param resId
	 */
	public void setBackground(int resId) {
		if (resId > 0 && convertView != null) {
			try {
				convertView.setBackgroundResource(resId);
			} catch (Exception e) {
				Log.e(TAG, "setBackground   try { convertView.setBackgroundResource(resId);" +
						" \n >> } catch (Exception e) { \n" + e.getMessage());
			}
		}
	}


	//	/**性能不好
	//	 * @param id
	//	 * @param s
	//	 */
	//	public void setText(int id, String s) {
	//		TextView tv = (TextView) findViewById(id);
	//		tv.setText(s);
	//	}



	//resources方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public final Resources getResources() {
		if(resources == null) {
			resources = context.getResources();
		} 
		return resources;
	}

	public String getString(int id) {
		return getResources().getString(id);
	}
	public int getColor(int id) {
		return getResources().getColor(id);
	}
	public Drawable getDrawable(int id) {
		return getResources().getDrawable(id);
	}
	public float getDimension(int id) {
		return getResources().getDimension(id);
	}
	//resources方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


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

	/**销毁并回收内存，建议在对应的View占用大量内存时使用
	 * @warn 只能在UI线程中调用
	 */
	public void onDestroy() {
		if (convertView != null) {
			try {
				convertView.destroyDrawingCache();
			} catch (Exception e) {
				Log.w(TAG, "onDestroy  try { convertView.destroyDrawingCache();" +
						" >> } catch (Exception e) {\n" + e.getMessage());
			}
			convertView = null;
		}

		onDataChangedListener = null;
		onTouchListener = null;
		onClickListener = null;
		onLongClickListener = null;
//		onClickViewList = null;

		data = null;
		position = 0;

		context = null;
	}

}
