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
import java.util.Calendar;
import java.util.List;

import zuo.biao.library.base.BaseViewBottomWindow;
import zuo.biao.library.model.Entry;
import zuo.biao.library.model.GridPickerConfig;
import zuo.biao.library.ui.GridPickerView.OnTabClickListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

/**时间选择弹窗
 * @author Lemon
 * @use
 * <br> toActivity或startActivityForResult (TimePickerWindow.createIntent(...), requestCode);
 * <br> 然后在onActivityResult方法内
 * <br> data.getLongExtra(TimePickerWindow.RESULT_TIME_IN_MILLIS); 可得到选中的日期
 */
public class TimePickerWindow extends BaseViewBottomWindow<List<Entry<Integer, String>>, GridPickerView> {
	private static final String TAG = "TimePickerWindow";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_MIN_TIME = "INTENT_MIN_TIME";
	public static final String INTENT_MAX_TIME = "INTENT_MAX_TIME";
	public static final String INTENT_DEFAULT_TIME = "INTENT_DEFAULT_TIME";

	public static final String RESULT_TIME = "RESULT_TIME";
	public static final String RESULT_TIME_IN_MILLIS = "RESULT_TIME_IN_MILLIS";
	public static final String RESULT_TIME_DETAIL_LIST = "RESULT_TIME_DETAIL_LIST";

	/**启动这个Activity的Intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return createIntent(context, null);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param limitTimeDetail
	 * @return
	 */
	public static Intent createIntent(Context context, int[] limitTimeDetail) {
		return createIntent(context, limitTimeDetail, null);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param limitTimeDetail
	 * @param defaultTimeDetail
	 * @return
	 */
	public static Intent createIntent(Context context, int[] limitTimeDetail, int[] defaultTimeDetail) {
		int[] minTimeDetail = null;
		int[] maxTimeDetail = null;
		if (limitTimeDetail != null && limitTimeDetail.length >= MIN_LENGHT) {
			minTimeDetail = TimeUtil.getTimeDetail(System.currentTimeMillis());//基本只会选后面的时间
			maxTimeDetail = limitTimeDetail;
		}
		return createIntent(context, minTimeDetail, maxTimeDetail, defaultTimeDetail);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param minTimeDetail
	 * @param maxTimeDetail
	 * @return
	 */
	public static Intent createIntent(Context context, int[] minTimeDetail, int[] maxTimeDetail, int[] defaultTimeDetail) {
		return new Intent(context, TimePickerWindow.class).
				putExtra(INTENT_MIN_TIME, minTimeDetail).
				putExtra(INTENT_MAX_TIME, maxTimeDetail).
				putExtra(INTENT_DEFAULT_TIME, defaultTimeDetail);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void initView() {//必须调用
		super.initView();

	}

	private List<Entry<Integer, String>> list;
	private void setPickerView(final int tabPosition) {
		runThread(TAG + "setPickerView", new Runnable() {
			@Override
			public void run() {

				final ArrayList<Integer> selectedItemList = new ArrayList<Integer>();
				for (GridPickerConfig gpcb : configList) {
					selectedItemList.add(0 + Integer.valueOf(StringUtil.getNumber(gpcb.getSelectedItemName())));
				}

				list = getList(tabPosition, selectedItemList);
				runUiThread(new Runnable() {
					@Override
					public void run() {
						containerView.bindView(tabPosition, list);
					}
				});
			}
		});
	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final int MIN_LENGHT = 2;

	//	private long minTime;
	//	private long maxTime;

	private int[] minTimeDetails;
	private int[] maxTimeDetails;
	private int[] defaultTimeDetails;

	private ArrayList<GridPickerConfig> configList;
	@Override
	public void initData() {//必须调用
		super.initData();

		intent = getIntent();

		//		minTime = getIntent().getLongExtra(INTENT_MIN_TIME, 0);
		//		maxTime = getIntent().getLongExtra(INTENT_MAX_TIME, 0);
		//		if (minTime >= maxTime) {
		//			Log.e(TAG, "initData minTime >= maxTime >> finish(); return; ");
		//			finish();
		//			return;
		//		}
		//		

		//		int[] minTimeDetails = TimeUtil.getTimeDetail(minTime);
		//		int[] maxTimeDetails = TimeUtil.getTimeDetail(maxTime);
		minTimeDetails = intent.getIntArrayExtra(INTENT_MIN_TIME);
		maxTimeDetails = intent.getIntArrayExtra(INTENT_MAX_TIME);
		defaultTimeDetails = intent.getIntArrayExtra(INTENT_DEFAULT_TIME);

		if (minTimeDetails == null || minTimeDetails.length <= 0) {
			minTimeDetails = new int[]{0, 0};
		}
		if (maxTimeDetails == null || maxTimeDetails.length <= 0) {
			maxTimeDetails = new int[]{23, 59};
		}
		if (minTimeDetails == null || minTimeDetails.length < MIN_LENGHT
				|| maxTimeDetails == null || maxTimeDetails.length < MIN_LENGHT) {
			finish();
			return;
		}
		if (defaultTimeDetails == null || defaultTimeDetails.length < MIN_LENGHT) {
			defaultTimeDetails = TimeUtil.getTimeDetail(System.currentTimeMillis());
		}


		runThread(TAG + "initData", new Runnable() {

			@Override
			public void run() {

				final ArrayList<Integer> selectedItemList = new ArrayList<Integer>();
				selectedItemList.add(defaultTimeDetails[0]);
				selectedItemList.add(defaultTimeDetails[1]);

				//放getList导致中间时钟对应的分钟不全
				configList = new ArrayList<GridPickerConfig>();
				configList.add(new GridPickerConfig(TimeUtil.NAME_HOUR, "" + selectedItemList.get(0)
						, selectedItemList.get(0), 6, 4));
				configList.add(new GridPickerConfig(TimeUtil.NAME_MINUTE, "" + selectedItemList.get(1)
						, selectedItemList.get(1), 5, 6));

				list = getList(selectedItemList.size() - 1, selectedItemList);

				runUiThread(new Runnable() {

					@Override
					public void run() {
						containerView.init(configList, list);
					}
				});
			}
		});

	}

	boolean[] isCenter = new boolean[3];
	boolean isEqualStart[] = new boolean[3];
	/**获取列表
	 * @param tabPosition
	 * @param selectedItemList
	 * @return
	 */
	private synchronized List<Entry<Integer, String>> getList(int tabPosition, ArrayList<Integer> selectedItemList) {
		int level = TimeUtil.LEVEL_HOUR + tabPosition;
		if (selectedItemList == null || selectedItemList.size() < MIN_LENGHT
				|| TimeUtil.isContainLevel(level) == false) {
			Log.e(TAG, "getList  (selectedItemList == null || selectedItemList.size() < MIN_LENGHT" +
					" || TimeUtil.isContainLevel(level) == false >> return null;");
			return null;
		}

		list = new ArrayList<Entry<Integer, String>>();

		boolean isContinuous = TimeUtil.fomerIsEqualOrBigger(maxTimeDetails, minTimeDetails);//范围是连续的

		isCenter[0] = selectedItemList.get(0) != minTimeDetails[0] && selectedItemList.get(0) != maxTimeDetails[0];
		isEqualStart[0] = selectedItemList.get(0) == minTimeDetails[0];

		if (selectedItemList.size() >= 2) {
			isCenter[1] = selectedItemList.get(1) != minTimeDetails[1] && selectedItemList.get(1) != maxTimeDetails[1];
			isCenter[1] = isCenter[0] || isCenter[1];

			isEqualStart[1] = selectedItemList.get(1) == minTimeDetails[1];
		}

		boolean isEnable;
		switch (level) {
		case TimeUtil.LEVEL_HOUR:
			for (int i = 0; i < 24; i++) {
				list.add(new Entry<Integer, String>(getItemType(
						( isContinuous && (i >= minTimeDetails[0] && i <= maxTimeDetails[0]) )
						|| ( isContinuous == false && (i >= minTimeDetails[0] || i <= maxTimeDetails[0]) ) 
						), String.valueOf(i)));
			}
			break;
		case TimeUtil.LEVEL_MINUTE:
			for (int i = 0; i < 60; i++) {
				if (minTimeDetails[0] == maxTimeDetails[0]) {// && i == minTimeDetails[0]) {
//					if (isContinuous) {
						isEnable = i >= minTimeDetails[1] && i <= maxTimeDetails[1];//时钟只有一个选项
//					} else {
//						isEnable = i >= minTimeDetails[1] || i <= maxTimeDetails[1];//时钟只有一个选项
//					}
				} else {
					isEnable = isCenter[0]//时钟在中间
							|| (isEqualStart[0] && i >= minTimeDetails[1])//时钟最小
							|| (selectedItemList.get(0) == maxTimeDetails[0] && i <= maxTimeDetails[1]);//时钟最大
				}
				list.add(new Entry<Integer, String>(getItemType(isEnable), String.valueOf(i)));
			}
			break;
		case TimeUtil.LEVEL_SECOND:
			for (int i = 0; i < 60; i++) {
				if (minTimeDetails[0] == maxTimeDetails[0] && minTimeDetails[1] == maxTimeDetails[1]
						&& i == minTimeDetails[1]) {
					if (isContinuous) {
						isEnable = i >= minTimeDetails[1] && i <= maxTimeDetails[1];//时钟只有一个选项
					} else {
						isEnable = i >= minTimeDetails[1] || i <= maxTimeDetails[1];//时钟只有一个选项
					}
				} else {
					isEnable = isCenter[0]//时钟在中间
							|| (isEqualStart[0] && i >= minTimeDetails[1])//时钟最小
							|| (selectedItemList.get(0) == maxTimeDetails[0] && i <= maxTimeDetails[1]);//时钟最大
				}
				list.add(new Entry<Integer, String>(getItemType(isEnable), String.valueOf(i)));
			}
			break;
		default:
			break;
		}

		return list;
	}

	private Integer getItemType(boolean isEnable) {
		return isEnable ? GridPickerAdapter.TYPE_CONTNET_ENABLE : GridPickerAdapter.TYPE_CONTNET_UNABLE;
	}



	@Override
	public String getTitleName() {
		return "选择时间";
	}
	@Override
	public String getReturnName() {
		return null;
	}
	@Override
	public String getForwardName() {
		return null;
	}

	@Override
	protected GridPickerView createView() {
		return new GridPickerView(context, getResources());
	}

	@Override
	protected void setResult() {
		intent = new Intent();

		List<String> list = containerView.getSelectedItemList();
		if (list != null) {
			ArrayList<Integer> detailList = new ArrayList<Integer>(); 
			for (int i = 0; i < list.size(); i++) {
				detailList.add(0 + Integer.valueOf(StringUtil.getNumber(list.get(i))));
			}

			Calendar calendar = Calendar.getInstance();
			calendar.set(0, 0, 0, detailList.get(0), detailList.get(1));
			intent.putExtra(RESULT_TIME_IN_MILLIS, calendar.getTimeInMillis());
			intent.putIntegerArrayListExtra(RESULT_TIME_DETAIL_LIST, detailList);
		}

		setResult(RESULT_OK, intent);
	}


	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用
		super.initEvent();

		containerView.setOnTabClickListener(onTabClickListener);
		containerView.setOnItemSelectedListener(onItemSelectedListener);
	}


	private OnTabClickListener onTabClickListener = new OnTabClickListener() {

		@Override
		public void onTabClick(int tabPosition, TextView tvTab) {
			setPickerView(tabPosition);
		}
	};

	private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
			containerView.doOnItemSelected(containerView.getCurrentTabPosition()
					, position, containerView.getCurrentSelectedItemName());
			setPickerView(containerView.getCurrentTabPosition() + 1);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
	};

	//系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}