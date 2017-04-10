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

import zuo.biao.library.base.BaseView.OnViewClickListener;
import zuo.biao.library.interfaces.AdapterViewPresenter;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**基础Adapter，使用自定义View
 * <br> 适用于ListView,GridView等AbsListView的子类
 * @author Lemon
 * @param <T> 数据模型(model/JavaBean)类
 * @param <BV> BaseView的子类
 * @see #setOnViewClickListener
 * @use extends BaseViewAdapter<T, BV>, 具体参考 .DemoAdapter3
 */
public abstract class BaseViewAdapter<T, BV extends BaseView<T>> extends BaseAdapter<T>
implements AdapterViewPresenter<T, BV> {
	//	private static final String TAG = "BaseViewAdapter";


	public OnViewClickListener<T, BV> onViewClickListener;
	/**为ItemView设置点击View的事件监听
	 * @param listener
	 * @see BaseView.OnViewClickListener
	 */
	public void setOnViewClickListener(OnViewClickListener<T, BV> listener) {
		onViewClickListener = listener;
	}
	

	private AdapterViewPresenter<T, BV> presenter;
	/**在子类构造方法内使用可重写AdapterViewPresenter里的方法
	 * @param presenter
	 */
	protected final void setPresenter(AdapterViewPresenter<T, BV> presenter) {
		this.presenter = presenter;
	}
	/**
	 * @return presenter == null ? this : presenter;
	 */
	protected final AdapterViewPresenter<T, BV> getPresenter() {
		return presenter == null ? this : presenter;
	}


	public BaseViewAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		@SuppressWarnings("unchecked")
		BV bv = convertView == null ? null : (BV) convertView.getTag();
		if (bv == null) {
			bv = getPresenter().createView(position, parent);
			convertView = bv.createView(inflater, position, getItemViewType(position));
			setOnClickListener(bv);//比在bindView里调用效率高。像是小众需求，应该去掉直接在子类针对性地实现？

			convertView.setTag(bv);
		}

		getPresenter().bindView(position, bv);

		return super.getView(position, convertView, parent);
	}

	/**bv的显示方法
	 * @param position
	 * @param bv
	 */
	@Override
	public void bindView(int position, BV bv) {
		bv.bindView(getItem(position), position, getItemViewType(position));
	}
	

	/**设置对ItemView点击事件的处理
	 * @param bv
	 */
	protected void setOnClickListener(final BV bv) {
		if (onViewClickListener != null) {
			bv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onViewClickListener.onViewClick(v, bv);
				}
			});
		}
	}
}
