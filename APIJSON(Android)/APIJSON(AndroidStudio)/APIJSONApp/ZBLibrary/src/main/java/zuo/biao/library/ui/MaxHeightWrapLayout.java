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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**限制最大高度为屏幕宽度的类
 * @author Lemon
 * @use 使用方法：写在xml文件中即可
 */
public class MaxHeightWrapLayout extends LinearLayout {
	
	public MaxHeightWrapLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MaxHeightWrapLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MaxHeightWrapLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// For simple implementation, or internal size is always 0.
		// We depend on the container to specify the layout size of
		// our view. We can't really know what it is since we will be
		// adding and removing different arbitrary views and do not
		// want the layout to change as this happens.
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
		if (heightMeasureSpec > widthMeasureSpec) {//没用，在显示前height和width都为0
			heightMeasureSpec = widthMeasureSpec;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}