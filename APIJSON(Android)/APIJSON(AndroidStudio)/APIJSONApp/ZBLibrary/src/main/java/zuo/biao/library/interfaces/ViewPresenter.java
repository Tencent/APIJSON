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

import android.support.annotation.Nullable;

/**View的逻辑接口
 * @author Lemon
 * @use implements ViewPresenter
 */
public interface ViewPresenter {

	/**获取导航栏标题名
	 * @return null - View.GONE; "" - View.GONE; "xxx" - "xxx"
	 */
	@Nullable
	public String getTitleName();

	/**获取导航栏返回按钮名
	 * @return null - default; "" - default; "xxx" - "xxx"
	 */
	@Nullable
	public String getReturnName();
	
	/**获取导航栏前进按钮名
	 * @return null - default; "" - default; "xxx" - "xxx"
	 */
	@Nullable
	public String getForwardName();

}