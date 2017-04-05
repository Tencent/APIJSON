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

import zuo.biao.library.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;

/**
 * 原工程地址及具体介绍
 * https://github.com/TommyLemon/PagedListView
 */
/**页面滚动，解决ListView等滚动位置经常不符合用户预期问题。
 * *中低速滑动会滚动一页，很慢滑动或很快滑动则按默认处理。
 * *适用于ListView，GridView等AbsListView的子类
 * @author Lemon
 * @see #dispatchTouchEvent
 * @use new PageScroller(listView).init();
 */
public class PageScroller implements OnGestureListener, OnTouchListener {
	private static final String TAG = "PageScroller";

	private OnTouchListener onTouchListener;
	/**避免lv设置OnTouchListener冲突
	 * @param onTouchListener
	 * @warn 在init前调用有效
	 * @use 调用该方法 >> onTouch中pageScroller.onTouch(v, event);
	 */
	public void setOnTouchListener(OnTouchListener onTouchListener) {
		this.onTouchListener = onTouchListener;
	}


	private Context context;
	private AbsListView lv;
	/**
	 * 不支持ScrollView,因为ScrollView往往不会太长,而且尝试多次smoothScrollBy,pageScroll都无效。
	 * 不支持WebView,因为没找到WebView滚动网页方法,尝试过scrollBy也无效。
	 */
	public PageScroller(AbsListView lv) {
		this.lv = lv;
		this.context = lv.getContext();
	}

	/**
	 * 最小纵向滑动距离
	 */
	private float minDistanceY;
	/**
	 * 最大纵向滑动距离
	 */
	private float maxDistanceY;
	/**
	 * 手势监听类
	 */
	private GestureDetector gestureDetector;
	/**初始化
	 * @return
	 * @must 调用
	 */
	public void init() {
		minDistanceY = context.getResources().getDimension(R.dimen.min_distance_y);
		maxDistanceY = context.getResources().getDimension(R.dimen.max_distance_y);

		gestureDetector = new GestureDetector(context, this);

		lv.setOnTouchListener(onTouchListener == null ? this : onTouchListener);
	}

	/**
	 * @param ev
	 * @return
	 * @must 在Activity的dispatchTouchEvent方法内调用
	 */
	public void dispatchTouchEvent(MotionEvent ev) {  
		gestureDetector.onTouchEvent(ev);  
	}




	/**滚动页面
	 * @param upToDown
	 */
	private void scrollPage(boolean upToDown) {
		Log.d(TAG, "scrollPage upToDown = " + upToDown + "; lv.getHeight() = " + lv.getHeight());
		lv.smoothScrollBy((int) ((upToDown ? -0.8 : 0.8) * lv.getHeight()), 120);
	}  



	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
	}

	private float distanceY;
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		//TODO 连续快速刷新列表(比如疯狂点击DemoActivity列表Item中的Name)有时导致e1或e2为null而崩溃。考虑到发生概率小，判空损耗性能就暂时不管。
		distanceY = e2.getRawY() - e1.getRawY();
		if (Math.abs(distanceY) < maxDistanceY) {
			if (distanceY > minDistanceY) {
				scrollPage(true);
				return true;
			}
			if (distanceY < - minDistanceY) {
				scrollPage(false);
				return true;
			}
		}

		return false;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

}
