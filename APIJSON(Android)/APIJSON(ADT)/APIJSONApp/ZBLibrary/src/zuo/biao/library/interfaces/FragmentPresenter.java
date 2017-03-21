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

/**Fragment的逻辑接口
 * @author Lemon
 * @use implements FragmentPresenter
 * @warn 对象必须是Fragment
 */
public interface FragmentPresenter extends Presenter {

	/**
	 * 该Fragment在Activity添加的所有Fragment中的位置
	 */
	static final String ARGUMENT_POSITION = "ARGUMENT_POSITION";
	static final String ARGUMENT_ID = "ARGUMENT_ID";
	static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";
	
	static final int RESULT_OK = Activity.RESULT_OK;
	static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
}