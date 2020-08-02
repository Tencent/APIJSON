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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apijson.demo.HttpManager;
import apijson.demo.R;
import apijson.demo.StringUtil;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;


/** 操作流程 Flow /操作步骤 Touch 列表
 * https://github.com/TommyLemon/UIAuto
 * @author Lemon
 */
public class UIAutoListActivity extends Activity implements HttpManager.OnHttpResponseListener {
    public static final String TAG = "UIAutoListActivity";

    public static final String INTENT_IS_LOCAL = "INTENT_IS_LOCAL";
    public static final String INTENT_FLOW_ID = "INTENT_FLOW_ID";
    public static final String INTENT_TOUCH_LIST = "INTENT_TOUCH_LIST";

    public static final String RESULT_LIST = "RESULT_LIST";

    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, boolean isLocal) {
        return new Intent(context, UIAutoListActivity.class).putExtra(INTENT_IS_LOCAL, isLocal);
    }

    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, long flowId) {
        return new Intent(context, UIAutoListActivity.class).putExtra(INTENT_FLOW_ID, flowId);
    }

    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String touchList) {
        return createIntent(context, true).putExtra(INTENT_TOUCH_LIST, touchList);
    }

    public static final String CACHE_FLOW = "CACHE_FLOW";
    public static final String CACHE_TOUCH = "KEY_TOUCH";


    private Activity context;

    private long flowId = 0;
    private boolean isTouch = false;
    private boolean isLocal = false;
    private boolean hasTempTouchList = false;
    private JSONArray touchList = null;

    private EditText etUIAutoListName;
    private ListView lvUIAutoList;
    private View llUIAutoListBar;

    private View btnUIAutoListRecover;
    private ProgressBar pbUIAutoList;
    private EditText etUIAutoListUrl;
    private Button btnUIAutoListGet;

    SharedPreferences cache;
    String cacheKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_auto_list_activity);

        context = this;

        isLocal = getIntent().getBooleanExtra(INTENT_IS_LOCAL, isLocal);
        flowId = getIntent().getLongExtra(INTENT_FLOW_ID, flowId);
        touchList = JSON.parseArray(getIntent().getStringExtra(INTENT_TOUCH_LIST));
        hasTempTouchList = touchList != null && touchList.isEmpty() == false;
        isTouch = flowId > 0 || hasTempTouchList;

        cache = getSharedPreferences(TAG, Context.MODE_PRIVATE);
        cacheKey = isTouch ? CACHE_TOUCH : CACHE_FLOW;
        if (isLocal) {
            JSONArray allList = JSON.parseArray(cache.getString(cacheKey, null));

            if (hasTempTouchList) {
                if (allList == null || allList.isEmpty()) {
                    allList = touchList;
                }
                else {
                    allList.addAll(touchList);
                }
                cache.edit().remove(cacheKey).putString(cacheKey, JSON.toJSONString(allList)).apply();
            }
            else {
                hasTempTouchList = true;
                if (flowId == 0) {
                    touchList = allList;
                } else {
                    touchList = new JSONArray();
                    if (allList != null) {
                        for (int i = 0; i < allList.size(); i++) {
                            JSONObject obj = allList.getJSONObject(i);
                            if (obj != null && obj.getLongValue("flowId") == flowId) {
                                touchList.add(obj);
                            }
                        }
                    }
                }
            }
        }


        etUIAutoListName = findViewById(R.id.etUIAutoListName);
        lvUIAutoList = findViewById(R.id.lvUIAutoList);
        llUIAutoListBar = findViewById(R.id.llUIAutoListBar);

        btnUIAutoListRecover = findViewById(R.id.btnUIAutoListRecover);
        pbUIAutoList = findViewById(R.id.pbUIAutoList);
        etUIAutoListUrl = findViewById(R.id.etUIAutoListUrl);
        btnUIAutoListGet = findViewById(R.id.btnUIAutoListGet);

        btnUIAutoListRecover.setVisibility(isTouch ? View.VISIBLE : View.GONE);
        etUIAutoListName.setVisibility(isTouch ? View.VISIBLE : View.GONE);
        etUIAutoListName.setEnabled(isLocal || hasTempTouchList);
//        llUIAutoListBar.setVisibility(isLocal ? View.GONE : View.VISIBLE);
        btnUIAutoListGet.setText(isLocal ? R.string.post : R.string.get);

        lvUIAutoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (array != null) {
                    JSONObject obj = array.getJSONObject(position);
                    if (isTouch) {
//                        setResult(RESULT_OK, new Intent().putExtra(RESULT_LIST, JSON.toJSONString(obj)));
//                        finish();
                    }
                    else {
                        startActivityForResult(UIAutoListActivity.createIntent(context, obj == null ? 0 : obj.getLongValue("id")), REQUEST_TOUCH_LIST);
                    }
                }
            }
        });



        if (hasTempTouchList) {
            showList(touchList);
        } else {
            send(btnUIAutoListGet);
        }
    }


    private ArrayAdapter<String> adapter;
    /** 示例方法 ：显示列表内容
     * @param list
     */
    private void setList(List<String> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbUIAutoList.setVisibility(View.GONE);
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
                lvUIAutoList.setAdapter(adapter);
            }
        });
    }

    private void showList(JSONArray array) {
        this.array = array;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                if (array != null) {
                    for (int i = 0; i < array.size(); i++) {

                        JSONObject obj = array.getJSONObject(i);
                        if (obj == null) {
                            obj = new JSONObject();
                        }

                        String state = statueList.get(obj);
                        if (StringUtil.isEmpty(state, true)) {
                            state = "Local";
                        }

                        if (isTouch) {
                            list.add("[" + state + "]  " + new Date(obj.getLongValue("time")).toLocaleString() + "    " + TouchUtil.getActionName(obj.getIntValue("action"))
                                    + "\nx: " + obj.getString("x") + ",  y: " + obj.getString("y") + ",  dividerY: " + obj.getString("dividerY") + ", pointerCount: " + obj.getString("pointerCount"));
                        } else {
                            list.add("[" + state + "]" + " name: " + obj.getString("name") + ",  time: " + new Date(obj.getLongValue("time")).toLocaleString());
                        }
                    }
                }

                setList(list);
            }
        }).start();
    }



    private Map<JSONObject, String> statueList = new HashMap<JSONObject, String>();
    public void send(View v) {
        final String fullUrl = StringUtil.getTrimedString(etUIAutoListUrl) + StringUtil.getString((TextView) v).toLowerCase();

        pbUIAutoList.setVisibility(View.VISIBLE);

        if (hasTempTouchList == false) {
            hasTempTouchList = true;
            cache.edit().remove(cacheKey).putString(cacheKey, JSON.toJSONString(touchList)).apply();
        }

        if (isLocal) {
            statueList = new HashMap<>();

            if (touchList != null) {
                for (int i = 0; i < touchList.size(); i++) {
                    JSONObject touch = touchList.getJSONObject(i);
                    String state = statueList.get(touch);
                    if ("Remote".equals(state) || "Uploading".equals(state)) {
                        return;
                    }

                    statueList.put(touch, "Uploading");

                    JSONRequest request = new JSONRequest();
                    {   // Touch <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        request.put("Touch", touch);
                    }   // Touch >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    request.setTag("Touch");

                    pbUIAutoList.setVisibility(View.VISIBLE);
                    HttpManager.getInstance().post(fullUrl, request.toString(), new HttpManager.OnHttpResponseListener() {
                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                            JSONResponse response = new JSONResponse(resultJson);
                            if (response.isSuccess()) {
                                statueList.put(touch, "Remote");
                            }
                            else {
                                statueList.put(touch, "Local");
                            }
                            showList(array);
                        }
                    });
                }

            }
        }
        else {
            JSONRequest request = new JSONRequest();

            if (isTouch) {
                {   // Touch[] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONRequest touchItem = new JSONRequest();
                    {   // Touch <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        JSONRequest touch = new JSONRequest();
                        touch.put("flowId", flowId);
                        touchItem.put("Touch", touch);
                    }   // Touch >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    request.putAll(touchItem.toArray(0, 0, "Touch"));
                }   // Touch[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            }
            else {
                {   // Flow[] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONRequest flowItem = new JSONRequest();
                    {   // Flow <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        JSONRequest flow = new JSONRequest();
                        flowItem.put("Flow", flow);
                    }   // Flow >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    request.putAll(flowItem.toArray(0, 0, "Flow"));
                }   // Flow[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            }

            HttpManager.getInstance().post(fullUrl, request.toString(), this);
        }

    }

    public void recover(View v) {
        setResult(RESULT_OK, new Intent().putExtra(RESULT_LIST, JSON.toJSONString(array)));
        finish();
    }

    private JSONArray array;
    @Override
    public void onHttpResponse(int requestCode, String resultJson, Exception e) {
        Log.d(TAG, "onHttpResponse  resultJson = " + resultJson);
        if (e != null) {
            Log.e(TAG, "onHttpResponse e = " + e.getMessage());
        }
        JSONResponse response = new JSONResponse(resultJson);
        array = response.getJSONArray(isTouch ? "Touch[]" : "Flow[]");
        if (array == null) {
            array = new JSONArray();
        }
        statueList = new HashMap<>();
        for (int i = 0; i < array.size(); i++) {
            statueList.put(array.getJSONObject(i), "Remote");
        }

        showList(array);
    }



    private static final int REQUEST_TOUCH_LIST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_TOUCH_LIST) {
            setResult(RESULT_OK, data);
            finish();
        }

    }
}
