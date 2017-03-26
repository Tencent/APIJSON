/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo.client.manager;

import apijson.demo.client.activity_fragment.UserActivity;
import apijson.demo.client.interfaces.OnHttpResponseListener;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.library.util.Log;

/**Http请求结果解析类
 * *适合APIJSON JSONResponse的json格式
 * @author Lemon
 * @param <T>
 * @see UserActivity#initData()
 * @use 把请求中的listener替换成new OnHttpResponseListenerImpl(listener)
 */
public class OnHttpResponseListenerImpl implements OnHttpResponseListener
, zuo.biao.library.manager.HttpManager.OnHttpResponseListener {
	private static final String TAG = "OnHttpResponseListenerImpl";


	OnHttpResponseListener listener;
	public OnHttpResponseListenerImpl(RequestMethod method, OnHttpResponseListener listener) {
		this.listener = listener;
	}

	@Override
	public void onHttpResponse(int requestCode, String resultJson, Exception e) {
		Log.i(TAG, "onHttpResponse  requestCode = " + requestCode + "; resultJson = " + resultJson
				+ "; \n\ne = " + (e == null ? null : e.getMessage()));

		if (listener == null) {
			listener = this;
		}
		listener.onHttpResponse(requestCode, new JSONResponse(resultJson), e);
	}

	@Override
	public void onHttpResponse(int requestCode, JSONResponse response, Exception e) {
		Log.i(TAG, "onHttpResponse  requestCode = " + requestCode + "; response = " + JSON.toJSONString(response));
	}

}
