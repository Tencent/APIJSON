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

package zuo.biao.library.interfaces;

import android.app.Activity;
import android.view.View;

/**Activity的逻辑接口
 * @author Lemon
 * @use implements ActivityPresenter
 * @warn 对象必须是Activity
 */
public interface ActivityPresenter extends Presenter {

	/**获取Activity
	 * @must 在非抽象Activity中 return this;
	 */
	public Activity getActivity();//无public导致有时自动生成的getActivity方法会缺少public且对此报错

	/**返回按钮被点击
	 * *Activity的返回按钮和底部弹窗的取消按钮几乎是必备，正好原生支持反射；而其它比如Fragment极少用到，也不支持反射
	 * @param v
	 */
	public void onReturnClick(View v);
	
	/**前进按钮被点击
	 * *Activity常用导航栏右边按钮，而且底部弹窗BottomWindow的确定按钮是必备；而其它比如Fragment极少用到，也不支持反射
	 * @param v
	 */
	public void onForwardClick(View v);
	
}