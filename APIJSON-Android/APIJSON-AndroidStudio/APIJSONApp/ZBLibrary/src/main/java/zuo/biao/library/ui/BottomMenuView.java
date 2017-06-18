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

package zuo.biao.library.ui;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.R;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.model.Menu;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.StringUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**自定义嵌入式菜单View
 * @author Lemon
 * @use 
 * <br> BottomMenuView bottomMenuView = new BottomMenuView(context, resources, toBottomMenuWindowRequestCode);
 * <br> bottomMenuView.bindView(menuList);
 * <br> bottomMenuView.setOnMenuItemClickListener(onBottomMenuItemClickListener);
 * <br> *具体参考.UserActivity
 */
public class BottomMenuView extends BaseView<List<Menu>> {
	private static final String TAG = "BottomMenuView";

	public interface OnBottomMenuItemClickListener{
		void onBottomMenuItemClick(int intentCode);
	}

	private OnBottomMenuItemClickListener onBottomMenuItemClickListener;
	public void setOnMenuItemClickListener(OnBottomMenuItemClickListener l) {
		onBottomMenuItemClickListener = l;
	}


	private int toBottomMenuWindowRequestCode;
	public BottomMenuView(Activity context, Resources resources, int toBottomMenuWindowRequestCode) {
		super(context, resources);
		this.toBottomMenuWindowRequestCode = toBottomMenuWindowRequestCode;
	}



	private LayoutInflater inflater;

	public LinearLayout llBottomMenuViewMainItemContainer;
	/**获取View
	 * @return
	 */
	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		this.inflater = inflater;
		convertView = inflater.inflate(R.layout.bottom_menu_view, null);

		llBottomMenuViewMainItemContainer = (LinearLayout) 
				convertView.findViewById(R.id.llBottomMenuViewMainItemContainer);

		return convertView;
	}

	@Override
	public List<Menu> getData() {
		return list;
	}

	private List<Menu> list;//传进来的数据
	private ArrayList<String> moreMenuNameList;
	private ArrayList<Integer> moreMenuIntentCodeList;
	@Override
	public void bindView(final List<Menu> menuList){
		if (menuList == null || menuList.isEmpty()) {
			Log.e(TAG, "bindView  menuList == null || menuList.isEmpty() >> return;");
			return;
		}
		this.list = menuList;

		llBottomMenuViewMainItemContainer.removeAllViews();
		final int mainItemCount = list.size() > 4 ? 3 : list.size();//不包括 更多 按钮
		Menu fsb;
		for (int i = 0; i < mainItemCount; i++) {
			fsb = list.get(i);
			if (fsb.getImageRes() > 0) {
				addItem(false, i, fsb);
			} else {
				break;
			}
		}

		//菜单区域外的背景及监听不好做，还是点击更多弹出BottomMenuWindow好
		if (list.size() > 4) {
			addItem(true, -1, null);

			//弹出底部菜单
			moreMenuNameList = new ArrayList<String>();
			moreMenuIntentCodeList = new ArrayList<Integer>();
			Menu moreFsb;
			for (int i = 3; i < list.size(); i++) {
				moreFsb = list.get(i);
				if (moreFsb != null) {
					moreMenuNameList.add(moreFsb.getName());
					moreMenuIntentCodeList.add(moreFsb.getIntentCode());
				}
			}
		}
	}


	/**添加带图标的主要按钮
	 * @param position
	 * @param fsb
	 */
	@SuppressLint("InflateParams")
	private void addItem(final boolean isMoreButton, final int position, final Menu fsb) {
		if (isMoreButton == false) {
			if (position < 0 || fsb == null || StringUtil.isNotEmpty(fsb.getName(), true) == false
					|| fsb.getImageRes() <= 0) {
				Log.e(TAG, "addItem isMoreButton == false >> position < 0 || fsb == null " +
						"|| StringUtil.isNotEmpty(fsb.getName(), true) == false " +
						"|| fsb.getImageRes() <= 0 >> return;");
				return;
			};
		}

		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.icon_name_item, null);
		ImageView iv = (ImageView) ll.findViewById(R.id.ivIconNameIcon);
		TextView tv = (TextView) ll.findViewById(R.id.tvIconNameName);
		try {
			iv.setImageResource(isMoreButton ? R.drawable.up2_light : fsb.getImageRes());
		} catch (Exception e) {
			Log.e(TAG, "addItem try {" +
					" iv.setImageResource(fsb.getImageRes()); " + e.getMessage() + ">> return;");
			return;
		}
		tv.setText(isMoreButton ? "更多" : "" + fsb.getName());

		ll.setPadding(
				(int) getDimension(R.dimen.common_item_left_tv_padding),
				0,
				(int) getDimension(R.dimen.common_item_right_img_padding_right),
				0);
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isMoreButton) {
					CommonUtil.toActivity(context, BottomMenuWindow.createIntent(context
							, moreMenuNameList, moreMenuIntentCodeList)
							.putExtra(BottomMenuWindow.INTENT_TITLE,  "更多"), toBottomMenuWindowRequestCode, false);
				} else {
					onBottomMenuItemClickListener.onBottomMenuItemClick(fsb.getIntentCode());
				}
			}
		});

		llBottomMenuViewMainItemContainer.addView(ll, position);
	}

}
