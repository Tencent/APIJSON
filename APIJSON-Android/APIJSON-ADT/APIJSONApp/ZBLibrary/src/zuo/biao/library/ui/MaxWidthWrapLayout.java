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
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**限制控件最大宽度的类
 * @author Lemon
 * @use 写在xml文件中即可
 */
public class MaxWidthWrapLayout extends LinearLayout {

	public MaxWidthWrapLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MaxWidthWrapLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MaxWidthWrapLayout(Context context) {
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
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
		int maxWidth = (int) getResources().getDimension(R.dimen.top_menu_max_width);
		if (widthMeasureSpec > maxWidth) {
			widthMeasureSpec = maxWidth;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}