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

package apijson.demo.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.Headers;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.lang.reflect.Method;

import apijson.demo.R;
import apijson.demo.StringUtil;
import apijson.demo.application.DemoApplication;
import apijson.demo.server.MethodUtil;

/**自动单元测试，需要用 UnitAuto 发请求到这个设备
 * https://github.com/TommyLemon/UnitAuto
 * @author Lemon
 */
public class UnitAutoActivity extends Activity implements HttpServerRequestCallback {
    public static final String TAG = "UnitAutoActivity";
    private static final String KEY_PORT = "KEY_PORT";

    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, UnitAutoActivity.class);
    }


    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer mAsyncServer = new AsyncServer();

    private Activity context;
    private boolean isAlive;

    private TextView tvUnitRequest;
    private TextView tvUnitResponse;

    private TextView tvUnitOrient;
    private TextView etUnitPort;
    private View pbUnit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.unit_auto_activity);
        context = this;
        isAlive = true;


        tvUnitRequest = findViewById(R.id.tvUnitRequest);
        tvUnitResponse = findViewById(R.id.tvUnitResponse);

        tvUnitOrient = findViewById(R.id.tvUnitOrient);
        etUnitPort = findViewById(R.id.etUnitPort);
        pbUnit = findViewById(R.id.pbUnit);


        SharedPreferences sp = getSharedPreferences(TAG, Context.MODE_PRIVATE);
        port = sp.getString(KEY_PORT, "");

        etUnitPort.setText(port);
        pbUnit.setVisibility(View.GONE);


        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                onConfigurationChanged(getResources().getConfiguration());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        onConfigurationChanged(getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        tvUnitOrient.setText(
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? (getString(R.string.screen) + getString(R.string.horizontal))
                        : getString(R.string.vertical)
        );
        super.onConfigurationChanged(newConfig);
    }

    public void copy(View v) {
        StringUtil.copyText(context, StringUtil.getString((TextView) v));
    }

    public void orient(View v) {
        setRequestedOrientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                ? ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
    }


    private String port = "8080";
    public void start(View v) {
        v.setEnabled(false);
        port = StringUtil.getTrimedString(etUnitPort);

        try {
            startServer(StringUtil.isEmpty(port, true) ? 8080 : Integer.valueOf(port));

            etUnitPort.setEnabled(false);
            pbUnit.setVisibility(View.VISIBLE);

            Toast.makeText(context, R.string.please_send_request_with_unit_auto, Toast.LENGTH_LONG).show();
        } catch (Exception e) {  // FIXME 端口异常 catch 不到
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        v.setEnabled(true);
    }
    public void stop(View v) {
        v.setEnabled(false);

        try {
            server.stop();
            mAsyncServer.stop();

            etUnitPort.setEnabled(true);
            pbUnit.setVisibility(View.GONE);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        v.setEnabled(true);
    }


    private void startServer(int port) {
        server.addAction("OPTIONS", "[\\d\\D]*", this);
//        server.get("[\\d\\D]*", this);
//        server.post("[\\d\\D]*", this);
        server.get("/", this);
        server.post("/method/list", this);
        server.post("/method/invoke", this);
        server.listen(mAsyncServer, port);
    }

    @Override
    public void onRequest(final AsyncHttpServerRequest asyncHttpServerRequest, final AsyncHttpServerResponse asyncHttpServerResponse) {
        Headers allHeaders = asyncHttpServerResponse.getHeaders();
        Headers reqHeaders = asyncHttpServerRequest.getHeaders();

        String corsHeaders = reqHeaders.get("access-control-request-headers");
        String corsMethod = reqHeaders.get("access-control-request-method");

//      if ("OPTIONS".toLowerCase().equals(asyncHttpServerRequest.getMethod().toLowerCase())) {

        String origin = reqHeaders.get("origin");
        reqHeaders.remove("cookie");  // 用不上还很占显示面积 String cookie = reqHeaders.get("cookie");

        allHeaders.set("Access-Control-Allow-Origin", TextUtils.isEmpty(origin) ? "*" : origin);
        allHeaders.set("Access-Control-Allow-Credentials", "true");
        allHeaders.set("Access-Control-Allow-Headers", TextUtils.isEmpty(corsHeaders) ? "*" : corsHeaders);
        allHeaders.set("Access-Control-Allow-Methods", TextUtils.isEmpty(corsMethod) ? "*" : corsMethod);
        allHeaders.set("Access-Control-Max-Age", "86400");
//        if (TextUtils.isEmpty(cookie) == false) {
//            allHeaders.set("Set-Cookie", cookie + System.currentTimeMillis());
//        }
//    }

        final AsyncHttpRequestBody requestBody = asyncHttpServerRequest.getBody();
        final String request = requestBody == null || requestBody.get() == null ? null : requestBody.get().toString();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (isAlive) {  //TODO 改为 ListView 展示，保证每次请求都能对齐 Request 和 Response 的显示
                    tvUnitRequest.setText(StringUtil.getString(asyncHttpServerRequest) + "Content:\n" + zuo.biao.apijson.JSON.format(request) + "\n\n\n\n\n" + StringUtil.getString(tvUnitRequest));
                }
            }
        });


        try {
            if ("OPTIONS".toLowerCase().equals(asyncHttpServerRequest.getMethod().toLowerCase())) {
                send(asyncHttpServerResponse, "{}");
                return;
            }

            switch (asyncHttpServerRequest.getPath()) {
                case "/":
                    send(asyncHttpServerResponse, "ok");
                    break;
                case "/method/list":
                    asyncHttpServerResponse.send("application/json; charset=utf-8", MethodUtil.listMethod(request).toJSONString());
                    break;
                case "/method/invoke":
                    MethodUtil.Listener<JSONObject> listener = new MethodUtil.Listener<JSONObject>() {

                        @Override
                        public void complete(JSONObject data, Method method, MethodUtil.InterfaceProxy proxy, Object... extras) throws Exception {
                            if (! asyncHttpServerResponse.isOpen()) {
                                Log.w(TAG, "invokeMethod  listener.complete  ! asyncHttpServerResponse.isOpen() >> return;");
                                return;
                            }

                            send(asyncHttpServerResponse, data.toJSONString());
                        }
                    };

                    try {
                        JSONObject req = JSON.parseObject(request);

                        Object instance = null;
                        try {
                            String pkgName = req.getString("package");
                            String clsName = req.getString("class");
                            Class<?> clazz = Class.forName(pkgName.replaceAll("/", ".") + "." + clsName);
                            if (clazz.isAssignableFrom(Activity.class)) {
                                instance = UnitAutoActivity.this;
                            } else if (clazz.isAssignableFrom(Application.class)) {
                                instance = DemoApplication.getInstance();
                            } else {
                                instance = null;
                            }
                        }
                        catch (Exception e) {
                            Log.e(TAG, "invokeMethod  try { instance =  Class<?> clazz = Class.forName(pkgName.replaceAll(\"/\", \".\") + \".\" + clsName); ... } catch (Exception e) { \n" + e.getMessage());
                        }

                        MethodUtil.invokeMethod(req, instance, listener);
                    }
                    catch (Exception e) {
                        Log.e(TAG, "invokeMethod  try { JSONObject req = JSON.parseObject(request); ... } catch (Exception e) { \n" + e.getMessage());
                        try {
                            listener.complete(MethodUtil.CALLBACK.newErrorResult(e));
                        }
                        catch (Exception e1) {
                            e1.printStackTrace();
                            send(asyncHttpServerResponse, MethodUtil.CALLBACK.newErrorResult(e1).toJSONString());
                        }
                    }

                    break;
                default:
                    asyncHttpServerResponse.end();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            send(asyncHttpServerResponse, MethodUtil.CALLBACK.newErrorResult(e).toJSONString());
        }

    }

    private void send(AsyncHttpServerResponse asyncHttpServerResponse, String json) {
        asyncHttpServerResponse.send("application/json; charset=utf-8", json);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (isAlive) {
                    tvUnitResponse.setText(StringUtil.getString(asyncHttpServerResponse) + "Content:\n" + zuo.biao.apijson.JSON.format(json) + "\n\n\n\n\n" + StringUtil.getString(tvUnitResponse));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        stop(etUnitPort);

        getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().remove(KEY_PORT).putString(KEY_PORT, port).apply();

        super.onDestroy();
    }


}
