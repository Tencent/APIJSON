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

import android.view.View;

/**到达（接触到）View的某个边界的监听回调
 * 一般用于一个（ViewGroup）parent内的（View）child接触到parent的事件监听
 * @author Lemon
 * @use implements OnReachViewBorderListener
 */
public interface OnReachViewBorderListener {
    static final int TYPE_TOP = 0;
    static final int TYPE_BOTTOM = 1;
    static final int TYPE_LEFT = 2;
    static final int TYPE_RIGHT = 3;

    /**到达（接触到）v的某个边界（type）
     * @param type 边界类型
     * @param v    目标View，一般为ViewGroup（ListView，GridView等）
     */
    void onReach(int type, View v);
}
