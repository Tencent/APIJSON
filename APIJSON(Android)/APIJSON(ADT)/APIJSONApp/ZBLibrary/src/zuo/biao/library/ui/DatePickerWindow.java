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
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

/**日期选择窗口
 * @author Lemon
 * @use 
 * <br> toActivity或startActivityForResult (DatePickerWindow.createIntent(...), requestCode);
 * <br> 然后在onActivityResult方法内
 * <br> data.getLongExtra(DatePickerWindow.RESULT_TIME_IN_MILLIS); 可得到选中的日期
 * @warn 和android系统SDK内一样，month从0开始
 */
public class DatePickerWindow extends BaseViewBottomWindow<List<Entry<Integer, String>>, GridPickerView> {
	private static final String TAG = "DatePickerWindow";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_MIN_DATE = "INTENT_MIN_DATE";
	public static final String INTENT_MAX_DATE = "INTENT_MAX_DATE";
	public static final String INTENT_DEFAULT_DATE = "INTENT_DEFAULT_DATE";

	public static final String RESULT_DATE = "RESULT_DATE";
	public static final String RESULT_TIME_IN_MILLIS = "RESULT_TIME_IN_MILLIS";
	public static final String RESULT_DATE_DETAIL_LIST = "RESULT_DATE_DETAIL_LIST";

	/**启动这个Activity的Intent
	 * @param context
	 * @param limitYearMonthDay
	 * @return
	 */
	public static Intent createIntent(Context context, int[] limitYearMonthDay) {
		return createIntent(context, limitYearMonthDay, null);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param limitYearMonthDay
	 * @param defaultYearMonthDay
	 * @return
	 */
	public static Intent createIntent(Context context, int[] limitYearMonthDay, int[] defaultYearMonthDay) {
		int[] selectedDate = TimeUtil.getDateDetail(System.currentTimeMillis());
		int[] minYearMonthDay = null;
		int[] maxYearMonthDay = null;
		if (TimeUtil.fomerIsBigger(limitYearMonthDay, selectedDate)) {
			minYearMonthDay = selectedDate;
			maxYearMonthDay = limitYearMonthDay;
		} else {
			minYearMonthDay = limitYearMonthDay;
			maxYearMonthDay = selectedDate;
		}
		return createIntent(context, minYearMonthDay, maxYearMonthDay, defaultYearMonthDay);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param minYearMonthDay
	 * @param maxYearMonthDay
	 * @return
	 */
	public static Intent createIntent(Context context, int[] minYearMonthDay, int[] maxYearMonthDay, int[] defaultYearMonthDay) {
		return new Intent(context, DatePickerWindow.class).
				putExtra(INTENT_MIN_DATE, minYearMonthDay).
				putExtra(INTENT_MAX_DATE, maxYearMonthDay).
				putExtra(INTENT_DEFAULT_DATE, defaultYearMonthDay);
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

						//防止选中非闰年2月29日
						if (tabPosition < 2) {
							ArrayList<String> selectedList = containerView.getSelectedItemList();
							if (selectedList != null && selectedList.size() >= 3) {

								if (TimeUtil.isLeapYear(0 + Integer.valueOf(
										StringUtil.getNumber(selectedList.get(0)))) == false) {

									if ("2".equals(StringUtil.getNumber(selectedList.get(1)))
											&& "29".equals(StringUtil.getNumber(selectedList.get(2)))) {

										onItemSelectedListener.onItemSelected(
												null, null, containerView.getCurrentSelectedItemPosition(), 0);
									}
								}
							}
						}
					}
				});
			}
		});
	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//	private long minDate;
	//	private long maxDate;

	private int[] minDateDetails;
	private int[] maxDateDetails;
	private int[] defaultDateDetails;

	private ArrayList<GridPickerConfig> configList;
	@Override
	public void initData() {//必须调用
		super.initData();

		intent = getIntent();

		//		minDate = getIntent().getLongExtra(INTENT_MIN_DATE, 0);
		//		maxDate = getIntent().getLongExtra(INTENT_MAX_DATE, 0);
		//		if (minDate >= maxDate) {
		//			Log.e(TAG, "initData minDate >= maxDate >> finish(); return; ");
		//			finish();
		//			return;
		//		}
		//		

		//		int[] minDateDetails = TimeUtil.getDateDetail(minDate);
		//		int[] maxDateDetails = TimeUtil.getDateDetail(maxDate);
		minDateDetails = intent.getIntArrayExtra(INTENT_MIN_DATE);
		maxDateDetails = intent.getIntArrayExtra(INTENT_MAX_DATE);
		defaultDateDetails = intent.getIntArrayExtra(INTENT_DEFAULT_DATE);

		if (minDateDetails == null || minDateDetails.length <= 0) {
			minDateDetails = new int[]{1970, 1, 1};
		}
		if (maxDateDetails == null || maxDateDetails.length <= 0) {
			maxDateDetails = new int[]{2020, 11, 31};
		}
		if (minDateDetails == null || minDateDetails.length <= 0
				|| maxDateDetails == null || minDateDetails.length != maxDateDetails.length) {
			finish();
			return;
		}
		if (defaultDateDetails == null || defaultDateDetails.length < 3) {
			defaultDateDetails = TimeUtil.getDateDetail(System.currentTimeMillis());
		}


		runThread(TAG + "initData", new Runnable() {

			@Override
			public void run() {
				final ArrayList<Integer> selectedItemList = new ArrayList<Integer>();
				selectedItemList.add(defaultDateDetails[0]);
				selectedItemList.add(defaultDateDetails[1]);
				selectedItemList.add(defaultDateDetails[2]);

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


	@SuppressLint("ResourceAsColor")
	private synchronized List<Entry<Integer, String>> getList(int tabPosition, ArrayList<Integer> selectedItemList) {
		int level = TimeUtil.LEVEL_YEAR + tabPosition;
		if (selectedItemList == null || selectedItemList.size() != 3 || TimeUtil.isContainLevel(level) == false) {
			return null;
		}

		list = new ArrayList<Entry<Integer, String>>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(selectedItemList.get(0), selectedItemList.get(1) - 1, 1);
		switch (level) {
		case TimeUtil.LEVEL_YEAR:
			for (int i = 0; i < maxDateDetails[0] - minDateDetails[0]; i++) {
				list.add(new Entry<Integer, String>(GridPickerAdapter.TYPE_CONTNET_ENABLE, String.valueOf(i + 1 + minDateDetails[0])));
			}
			break;
		case TimeUtil.LEVEL_MONTH:
			for (int i = 0; i < 12; i++) {
				list.add(new Entry<Integer, String>(GridPickerAdapter.TYPE_CONTNET_ENABLE, String.valueOf(i + 1)));
			}
			break;
		case TimeUtil.LEVEL_DAY:
			for (int i = calendar.get(Calendar.DAY_OF_WEEK) - 1; i < 7; i++) {
				list.add(new Entry<Integer, String>(GridPickerAdapter.TYPE_TITLE, TimeUtil.Day.getDayNameOfWeek(i)));
			}
			for (int i = 0; i < calendar.get(Calendar.DAY_OF_WEEK) - 1; i++) {
				list.add(new Entry<Integer, String>(GridPickerAdapter.TYPE_TITLE, TimeUtil.Day.getDayNameOfWeek(i)));
			}
			for (int i = 0; i < calendar.getActualMaximum(Calendar.DATE); i++) {
				list.add(new Entry<Integer, String>(GridPickerAdapter.TYPE_CONTNET_ENABLE, String.valueOf(i + 1)));
			}
			break;
		default:
			break;
		}

		if (configList == null || configList.size() < 3) {
			configList = new ArrayList<GridPickerConfig>();

			configList.add(new GridPickerConfig(TimeUtil.NAME_YEAR, "" + selectedItemList.get(0)
					, selectedItemList.get(0) - 1 - minDateDetails[0], 5, 4));
			configList.add(new GridPickerConfig(TimeUtil.NAME_MONTH, "" + selectedItemList.get(1)
					, selectedItemList.get(1) - 1, 4, 3));
			configList.add(new GridPickerConfig(TimeUtil.NAME_DAY, "" + selectedItemList.get(2)
					, selectedItemList.get(2) - 1 + 7, 7, 6));
		}

		return list;
	}



	@Override
	public String getTitleName() {
		return "选择日期";
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
		if (list != null && list.size() >= 3) {
			ArrayList<Integer> detailList = new ArrayList<Integer>(); 
			for (int i = 0; i < list.size(); i++) {
				detailList.add(0 + Integer.valueOf(StringUtil.getNumber(list.get(i))));
			}
			detailList.set(1, detailList.get(1) - 1);

			Calendar calendar = Calendar.getInstance();
			calendar.set(detailList.get(0), detailList.get(1), detailList.get(2));
			intent.putExtra(RESULT_TIME_IN_MILLIS, calendar.getTimeInMillis());
			intent.putIntegerArrayListExtra(RESULT_DATE_DETAIL_LIST, detailList);
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
			int tabPosition = containerView.getCurrentTabPosition() + 1;
			setPickerView(tabPosition);
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