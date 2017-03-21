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

package apijson.demo.client.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**自定义GridView，点击没有ChildView的空白处会把onTouchEvent传递到后面的View，相当于不会拦截触摸事件
 * @author Lemon
 */
public class EmptyEventGridView extends GridView {

	public EmptyEventGridView(Context context) {
		super(context);
	}

	public EmptyEventGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EmptyEventGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	} 

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return isClickable() || isLongClickable();
        }
		
		final int motionPosition = pointToPosition((int)event.getX(), (int)event.getY());//获取点击的位置
		if(motionPosition == INVALID_POSITION) {//touch事件转交给后面的View处理
			super.onTouchEvent(event);
			return false;
		}
		return super.onTouchEvent(event);
	}
}
