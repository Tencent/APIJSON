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
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;

import java.util.Calendar;

import apijson.demo.R;
import apijson.demo.application.DemoApplication;
import zuo.biao.apijson.JSON;


/**自动 UI 测试，需要用 UIAuto 发请求到这个设备
 * https://github.com/TommyLemon/UIAuto
 * @author Lemon
 */
public class UIAutoActivity extends Activity {
    private static final String TAG = "UIAutoActivity";
    private static final String DIVIDER_Y = "DIVIDER_Y";
    private static final String DIVIDER_HEIGHT = "DIVIDER_HEIGHT";
    private static final String DIVIDER_COLOR = "DIVIDER_COLOR";

    private static final String INTENT_FLOW_ID = "INTENT_FLOW_ID";
    private static final String INTENT_TOUCH_LIST = "INTENT_TOUCH_LIST";

    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, UIAutoActivity.class); //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String list) {
        return new Intent(context, UIAutoActivity.class).putExtra(INTENT_TOUCH_LIST, list);
    }


    private boolean isRecovering = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (isRecovering) {
                //通过遍历数组来实现
//                if (lastCurTime >= System.currentTimeMillis()) {
//                    isRecovering = false;
//                    pbUIAutoDivider.setVisibility(View.GONE);
//                }
//
//                MotionEvent event = (MotionEvent) msg.obj;
//                dispatchEventToCurrentActivity(event);


                //根据递归链表来实现，能精准地实现两个事件之间的间隔，不受处理时间不一致，甚至卡顿等影响。还能及时终止
                Node<InputEvent> eventNode = ( Node<InputEvent>) msg.obj;
                dispatchEventToCurrentActivity(eventNode.item);

                if (eventNode.next == null) {
                    isRecovering = false;
                    pbUIAutoDivider.setVisibility(View.GONE);
                    return;
                }

                msg = Message.obtain();
                msg.obj = eventNode.next;
                sendMessageDelayed(msg, eventNode.next.item.getEventTime() - eventNode.item.getEventTime());
            }
        }
    };

    private Activity context;
    int screenWidth;
    int screenHeight;

    int windowWidth;
    int windowHeight;
    int windowY;

    ViewGroup cover;
    ViewGroup divider;
    View rlUIAutoDivider;
    View vUIAutoDivider;
    View ivUIAutoDivider;
    View pbUIAutoDivider;

    private float dividerY;
    private float dividerHeight;
    private boolean moved = false;

    private JSONArray touchList;

    SharedPreferences cache;
    private long flowId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //反而让 cover 与底部差一个导航栏高度 requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_auto_activity);
        Window window = getWindow();
        //反而让 cover 与底部差一个导航栏高度 window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;

        flowId = getIntent().getLongExtra(INTENT_FLOW_ID, flowId);
        touchList = JSON.parseArray(getIntent().getStringExtra(INTENT_TOUCH_LIST));


        DisplayMetrics outMetrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();

        windowWidth = display.getWidth();
        windowHeight = display.getHeight();
        windowY = getWindowY(this);

        display.getRealMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        cache = getSharedPreferences(TAG, Context.MODE_PRIVATE);

        dividerY = cache.getFloat(DIVIDER_Y, 0);
        dividerHeight = cache.getFloat(DIVIDER_HEIGHT, dip2px(24));

        if (dividerY <= dividerHeight || dividerY >= windowHeight - dividerHeight) {
            dividerY = windowHeight - dividerHeight - dip2px(30);
        }

        if (touchList != null && touchList.isEmpty() == false) { //TODO 回放操作
//            recover(touchList);
            startActivityForResult(UIAutoListActivity.createIntent(DemoApplication.getInstance(), touchList == null ? null : touchList.toJSONString()), REQUEST_UI_AUTO_LIST);
//            return;
        }


        ViewGroup root = (ViewGroup) getWindow().getDecorView();
        cover = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_auto_cover_layout, null);
        divider = (ViewGroup) getLayoutInflater().inflate(R.layout.ui_auto_divider_layout, null);

        rlUIAutoDivider = divider.findViewById(R.id.rlUIAutoDivider);
        vUIAutoDivider = divider.findViewById(R.id.vUIAutoDivider);
        ivUIAutoDivider = divider.findViewById(R.id.ivUIAutoDivider);
        pbUIAutoDivider = divider.findViewById(R.id.pbUIAutoDivider);
        pbUIAutoDivider.setVisibility(View.GONE);

        ViewGroup.LayoutParams dividerLp = rlUIAutoDivider.getLayoutParams();
        if (dividerLp == null) {
            dividerLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dividerHeight);
        } else {
            dividerLp.height = (int) dividerHeight;
        }
        rlUIAutoDivider.setLayoutParams(dividerLp);

//        cover.addView(divider);

//        rlUIAutoDivider.post(new Runnable() {
//            @Override
//            public void run() {
//                rlUIAutoDivider.setY(dividerY - rlUIAutoDivider.getHeight()/2);
//                cover.setVisibility(View.GONE);
//            }
//        });

        vUIAutoDivider.setBackgroundColor(Color.parseColor(cache.getString(DIVIDER_COLOR, "#10000000")));

        ViewGroup.LayoutParams lineLp = ivUIAutoDivider.getLayoutParams();
        if (lineLp == null) {
            lineLp = new RelativeLayout.LayoutParams((int) dividerHeight, (int) dividerHeight);
        } else {
            lineLp.width = lineLp.height = (int) dividerHeight;
        }
        ivUIAutoDivider.setLayoutParams(lineLp);

        ivUIAutoDivider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecovering = false;
//                ((ViewGroup) v.getParent()).removeView(v);

                String cacheKey = UIAutoListActivity.CACHE_TOUCH;
                SharedPreferences cache = getSharedPreferences(UIAutoActivity.TAG, Context.MODE_PRIVATE);
                JSONArray allList = JSON.parseArray(cache.getString(cacheKey, null));

                if (allList == null || allList.isEmpty()) {
                    allList = touchList;
                }
                else {
                    allList.addAll(touchList);
                }
                cache.edit().remove(cacheKey).putString(cacheKey, JSON.toJSONString(allList)).commit();

//                startActivity(UIAutoListActivity.createIntent(DemoApplication.getInstance(), flowId));  // touchList == null ? null : touchList.toJSONString()));
//                startActivityForResult(UIAutoListActivity.createIntent(DemoApplication.getInstance(), touchList == null ? null : touchList.toJSONString()), REQUEST_UI_AUTO_LIST);
                startActivity(UIAutoActivity.createIntent(DemoApplication.getInstance(), touchList == null ? null : touchList.toJSONString()));

                floatCover = null;
                floatDivider = null;
                FloatWindow.destroy("v");
                FloatWindow.destroy("v_ball");
            }
        });
        rlUIAutoDivider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                都不动了 if (event.getY() - event.getRawY() >= 10) {
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    moved = true;
                    divider.setY(event.getY());
//                    divider.invalidate();
                } else {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        moved = false;
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (! moved) {
                            ivUIAutoDivider.performClick();
                        }
                    }
                }
//                }
                return true;
            }
        });

//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        root.addView(cover, lp);


        final ScrollView svTouch = findViewById(R.id.svTouch);
        final TextView tvTouch = findViewById(R.id.tvTouch);

        final View tvButton = findViewById(R.id.tvButton);
        final View btnButton = findViewById(R.id.btnButton);

        final View vTouch = findViewById(R.id.vTouch);
        final View llTouch = findViewById(R.id.llTouch);
        final View rlRoot = findViewById(R.id.rlRoot);
        final View vDispatchTouch = findViewById(R.id.vDispatchTouch);


        View.OnTouchListener listener = new View.OnTouchListener() {
            @RequiresApi(api = 29)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouchEvent  " + Calendar.getInstance().getTime().toLocaleString() +  " action:" + (event.getAction()) + "; x:" + event.getX() + "; y:" + event.getY());

                dispatchEventToCurrentActivity(event);

                if (isFinishing() || isDestroyed()) {
//                    ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                    Activity runningActivity = activityManager.getRunningTasks(1).get(0);
//                    getWindow().getDecorView().dispatchTouchEvent(event);

                    return true;
                }

                vDispatchTouch.dispatchTouchEvent(event);

//                vTouch.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return false;
//                    }
//                });
//
                CharSequence s = tvTouch.getText();
                if (s == null) {
                    s = "";
                }

                float dividerY = floatDivider.getY() + rlUIAutoDivider.getHeight()/2;
                float relativeY = event.getY() <= dividerY ? event.getY() : (event.getY() - screenHeight);

                tvTouch.setText(Calendar.getInstance().getTime().toLocaleString() +  "   " + TouchUtil.getActionName(event.getAction()) + "\nx:" + event.getX() + "; y:" + event.getY() + "; relativeY: " + relativeY + "; pointerCount: " + event.getPointerCount() + "\n" + s);
//                Toast.makeText(context, "vTouch.action:" + (event.getAction()) + "; x:" + event.getX() + "; y:" + event.getY(), Toast.LENGTH_SHORT).show();

//死循环                llTouch.dispatchTouchEvent(event);
//                vDispatchTouch.dispatchTouchEvent(event);
//                vDispatchTouch.dispatchTouchEvent(event);
                //onTouchEvent 不能处理事件 vDispatchTouch.onTouchEvent(event);
//                vTouch.setOnTouchListener(this);
                return true;  //连续记录只能 return true
            }
        };
//        vTouch.setOnTouchListener(listener);

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "tvButton.onClick", Toast.LENGTH_SHORT).show();
            }
        });
        tvButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(context, "tvButton.action:" + (event.getAction()) + "; x:" + event.getX() + "; y:" + event.getY(), Toast.LENGTH_SHORT).show();
                return false;  //连续记录只能 return true
            }
        });


        btnButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "btnMain.onLongClick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                MotionEvent e = MotionEvent.obtain(1000, 1000,
                        MotionEvent.ACTION_DOWN, screenWidth/2, screenHeight/2, 0);

                //TODO onTouchEvent ?
                vTouch.dispatchTouchEvent(e);
                e.setAction(MotionEvent.ACTION_UP);
                vTouch.dispatchTouchEvent(e);

            }
        }, 1000);

        cover.setOnTouchListener(listener);

    }


    public void onClick(View v) {
        Toast.makeText(context, "onClick BUTTON", Toast.LENGTH_SHORT).show();
        record(v);
//        finish();
    }

    public void toRemote(View v) {
        startActivityForResult(UIAutoListActivity.createIntent(context, false), REQUEST_UI_AUTO_LIST);
    }

    public void toLocal(View v) {
        startActivityForResult(UIAutoListActivity.createIntent(context, true), REQUEST_UI_AUTO_LIST);
    }

    public void record(View v) {
        flowId = - System.currentTimeMillis();

        cover.setVisibility(View.VISIBLE);
        showCover(true, context);

//        finish();
    }




    private IFloatWindow floatCover;
    private IFloatWindow floatDivider;

    private void showCover(boolean show, Activity activity) {
        //TODO 为纵屏、横屏分别加两套，判断屏幕方向来显示对应的一套
//        rlUIAutoDivider.setVisibility(show ? View.VISIBLE : View.GONE);
//        rlUIAutoDivider.setY(dividerY + dividerHeight/2);

        floatCover = FloatWindow.get("v");
        if (floatCover == null) {
            FloatWindow
                    .with(getApplicationContext())
                    .setTag("v")
                    .setView(cover)
                    .setWidth(windowWidth)                               //设置控件宽高
                    .setHeight(windowHeight)
                    .setX(0)                                   //设置控件初始位置
                    .setY(windowY)
                    .setMoveType(MoveType.inactive)
                    .setDesktopShow(true) //必须为 true，否则切换 Activity 就会自动隐藏                        //桌面显示
//                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
//                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                    .build();

            floatCover = FloatWindow.get("v");
        }
        floatCover.show();


        floatDivider = FloatWindow.get("v_ball");
        if (floatDivider == null) {
            FloatWindow
                    .with(getApplicationContext())
                    .setTag("v_ball")
                    .setView(divider)
                    .setWidth(windowWidth)                               //设置控件宽高
                    .setHeight((int) dividerHeight)
                    .setX(0)                                   //设置控件初始位置
                    .setY((int) (windowY + dividerY - dividerHeight/2))
//                    .setY(screenHeight/2)
                    .setMoveType(MoveType.slide)
                    .setDesktopShow(true) //必须为 true，否则切换 Activity 就会自动隐藏                       //桌面显示
//                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
//                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                    .build();

            floatDivider = FloatWindow.get("v_ball");
        }
        floatDivider.show();

        //TODO 新建一个  have already added to window manager

//        if (FloatWindow.get("h")== null) {
//            FloatWindow
//                    .with(getApplicationContext())
//                    .setTag("h")
//                    .setView(cover)
//                    .setWidth(screenWidth)                               //设置控件宽高
//                    .setHeight(screenHeight)
//                    .setX(0)                                   //设置控件初始位置
//                    .setY(0)
//                    .setMoveType(MoveType.inactive)
//                    .setDesktopShow(true)                        //桌面显示
////                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
////                .setPermissionListener(mPermissionListener)  //监听权限申请结果
//                    .build();
//        }
//
//        if (FloatWindow.get("h_ball") == null) {
//            FloatWindow
//                    .with(getApplicationContext())
//                    .setTag("h_ball")
//                    .setView(divider)
//                    .setWidth(screenWidth)                               //设置控件宽高
//                    .setHeight((int) dividerHeight)
//                    .setX(0)                                   //设置控件初始位置
//                    .setY((int) (dividerY + dividerHeight/2))
////                    .setY(screenHeight/2)
//                    .setMoveType(MoveType.slide)
//                    .setDesktopShow(true)                        //桌面显示
////                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
////                .setPermissionListener(mPermissionListener)  //监听权限申请结果
//                    .build();
//        }

//        FloatWindow.get("v").hide();
////        FloatWindow.get("v_ball").hide();
////        FloatWindow.get("h").hide();
////        FloatWindow.get("h_ball").hide();
//        if (show) {
//            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                FloatWindow.get("v").show();
//                FloatWindow.get("v_ball").show();
//            } else {
////                FloatWindow.get("h").show();
////                FloatWindow.get("h_ball").show();
//            }
//        }
    }


    public int getWindowY(Activity a) {
        View decorView = a.getWindow().getDecorView();

        Rect rectangle= new Rect();
        decorView.getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    public boolean dispatchEventToCurrentActivity(InputEvent ie) {
        Activity activity = DemoApplication.getInstance().getCurrentActivity();
        if (activity != null) {
//            event.offsetLocation(0, a.getWindow().getDecorView().findViewById(android.R.id.content).getTop());
//
//            float y = decorView.getY();
//            float top = decorView.getTop();
//            event.offsetLocation(0, decorView.getTop());
//            View content = decorView.findViewById(android.R.id.content);
//            float cy = content.getY();
//            float ctop = content.getTop();

            if (ie instanceof MotionEvent) {
                MotionEvent event = (MotionEvent) ie;
                int windowY = getWindowY(activity);

                if (windowY > 0) {
                    event = MotionEvent.obtain(event);
                    event.offsetLocation(0, windowY);
                }
                activity.dispatchTouchEvent(event);
            }
            else if (ie instanceof KeyEvent) {
                KeyEvent event = (KeyEvent) ie;
                activity.dispatchKeyEvent(event);
            }

        }


//        float dividerY = rlUIAutoDivider.getY() + rlUIAutoDivider.getHeight()/2;
//        float dividerY = floatDivider.getY() + rlUIAutoDivider.getHeight()/2;
//        float relativeY = event.getY() <= dividerY ? event.getY() : (event.getY() - screenHeight);
//
//        JSONObject obj = new JSONObject(true);
//        obj.put("id", - System.currentTimeMillis());
//        obj.put("flowId", flowId);
//        obj.put("action", event.getAction());
//        obj.put("x", (int) event.getX());
//        obj.put("y", (int) relativeY);
//        obj.put("dividerY", (int) dividerY);
//        obj.put("rawX", (int) event.getRawX());
//        obj.put("rawY", (int) event.getRawY());
//        obj.put("time", System.currentTimeMillis());
//        obj.put("downTime", event.getDownTime());
//        obj.put("eventTime", event.getEventTime());
//        obj.put("metaState", event.getMetaState());
//        obj.put("size", event.getSize());
//        obj.put("source", event.getSource());
//        obj.put("pressure", event.getPressure());
//        obj.put("deviceId", event.getDeviceId());
//        obj.put("xPrecision", event.getXPrecision());
//        obj.put("yPrecision", event.getYPrecision());
//        obj.put("pointerCount", event.getPointerCount());
//        obj.put("edgeFlags", event.getEdgeFlags());

        addInputEvent(ie, activity);

        return activity != null;
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        cache.edit().remove(DIVIDER_Y).putFloat(DIVIDER_Y, rlUIAutoDivider.getY() + rlUIAutoDivider.getHeight()/2).apply();
        super.onDestroy();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        super.dispatchTouchEvent(ev);
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//        return false;
//    }


    private Node<InputEvent> firstEventNode;
    private Node<InputEvent> eventNode;

    private long firstTime = 0;
    private long lastTime = 0;
    private long firstCurTime = 0;
    private long lastCurTime = 0;
    public void recover(JSONArray touchList) {
        isRecovering = true;

//        List<InputEvent> list = new LinkedList<>();

        showCover(true, DemoApplication.getInstance().getCurrentActivity());

        JSONObject first = touchList == null || touchList.isEmpty() ? null : touchList.getJSONObject(0);
        firstTime = first == null ? 0 : first.getLongValue("time");

        firstCurTime = 0;
        if (firstTime > 0) {
            firstCurTime = System.currentTimeMillis();
            pbUIAutoDivider.setVisibility(View.VISIBLE);

            for (int i = 0; i < touchList.size(); i++) {
                JSONObject obj = touchList.getJSONObject(i);

                InputEvent event;
                if (obj.getIntValue("type") == 1) {
                    /**
                     public KeyEvent(long downTime, long eventTime, int action,
                     int code, int repeat, int metaState,
                     int deviceId, int scancode, int flags, int source) {
                     mDownTime = downTime;
                     mEventTime = eventTime;
                     mAction = action;
                     mKeyCode = code;
                     mRepeatCount = repeat;
                     mMetaState = metaState;
                     mDeviceId = deviceId;
                     mScanCode = scancode;
                     mFlags = flags;
                     mSource = source;
                     mDisplayId = INVALID_DISPLAY;
                     }
                     */
                    event = new KeyEvent(
                            obj.getLongValue("downTime"),
                            obj.getLongValue("eventTime"),
                            obj.getIntValue("action"),
                            obj.getIntValue("keyCode"),
                            obj.getIntValue("repeatCount"),
                            obj.getIntValue("metaState"),
                            obj.getIntValue("deviceId"),
                            obj.getIntValue("scanCode"),
                            obj.getIntValue("flags"),
                            obj.getIntValue("source")
                    );
                }
                else {
                    /**
                     public static MotionEvent obtain(long downTime, long eventTime, int action,
                     float x, float y, float pressure, float size, int metaState,
                     float xPrecision, float yPrecision, int deviceId, int edgeFlags, int source,
                     int displayId)
                     */

                    //居然编译报错，和
                    // static public MotionEvent obtain(long downTime, long eventTime,
                    //    int action, int pointerCount, PointerProperties[] pointerProperties,
                    //    PointerCoords[] pointerCoords, int metaState, int buttonState,
                    //    float xPrecision, float yPrecision, int deviceId,
                    //    int edgeFlags, int source, int displayId, int flags)
                    //冲突，实际上类型没传错

                    //                    event = MotionEvent.obtain(obj.getLongValue("downTime"),  obj.getLongValue("eventTime"),  obj.getIntValue("action"),
                    //                    obj.getFloatValue("x"),  obj.getFloatValue("y"),  obj.getFloatValue("pressure"),  obj.getFloatValue("size"),  obj.getIntValue("metaState"),
                    //                    obj.getFloatValue("xPrecision"),  obj.getFloatValue("yPrecision"),  obj.getIntValue("deviceId"),  obj.getIntValue("edgeFlags"),  obj.getIntValue("source"),
                    //                    obj.getIntValue("displayId"));

                    event = MotionEvent.obtain(
                            obj.getLongValue("downTime"),
                            obj.getLongValue("eventTime"),
                            obj.getIntValue("action"),
//                            obj.getIntValue("pointerCount"),
                            obj.getFloatValue("x"),
                            obj.getFloatValue("y"),
                            obj.getFloatValue("pressure"),
                            obj.getFloatValue("size"),
                            obj.getIntValue("metaState"),
                            obj.getFloatValue("xPrecision"),
                            obj.getFloatValue("yPrecision"),
                            obj.getIntValue("deviceId"),
                            obj.getIntValue("edgeFlags")
//                            obj.getIntValue("source"),
//                            obj.getIntValue("displayId")
                    );
                    ((MotionEvent) event).setSource(obj.getIntValue("source"));
//                    ((MotionEvent) event).setEdgeFlags(obj.getIntValue("edgeFlags"));

                }


//                list.add(event);

                long time = obj.getIntValue("time");
                if (i <= 0) {
                    firstEventNode = new Node<>(null, event, null);
                    eventNode = firstEventNode;
                }
                else if (i >= touchList.size() - 1) {
                    lastTime = time;
                    lastCurTime = firstCurTime + lastTime - firstTime;
                }

                eventNode.next = new Node<>(eventNode, event, null);
                eventNode = eventNode.next;

                //通过遍历数组来实现
//                Message msg = handler.obtainMessage();
//                msg.obj = event;
//                handler.sendMessageDelayed(msg, i <= 0 ? 0 : time - firstTime);
            }

            //通过递归链表来实现
            Message msg = handler.obtainMessage();
            msg.obj = firstEventNode;
            handler.sendMessage(msg);

        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        addInputEvent(event, this);
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        addInputEvent(event, this);
        return super.onKeyUp(keyCode, event);
    }

    private JSONArray addInputEvent(InputEvent ie, Activity activity) {
        int dividerY = floatDivider.getY() + rlUIAutoDivider.getHeight()/2;
        int orientation = activity == null ? Configuration.ORIENTATION_PORTRAIT : activity.getResources().getConfiguration().orientation;

        JSONObject obj = new JSONObject(true);
        obj.put("id", - System.currentTimeMillis());
        obj.put("flowId", flowId);
        obj.put("time", System.currentTimeMillis());
        obj.put("orientation", orientation);
        obj.put("dividerY", dividerY);

        if (ie instanceof KeyEvent) {
            KeyEvent event = (KeyEvent) ie;
            obj.put("type", 1);

            //虽然 KeyEvent 和 MotionEvent 都有，但都不在父类 InputEvent 中 <<<<<<<<<<<<<<<<<<
            obj.put("action", event.getAction());
            obj.put("downTime", event.getDownTime());
            obj.put("eventTime", event.getEventTime());
            obj.put("metaState", event.getMetaState());
            obj.put("source", event.getSource());
            obj.put("deviceId", event.getDeviceId());
            //虽然 KeyEvent 和 MotionEvent 都有，但都不在父类 InputEvent 中 >>>>>>>>>>>>>>>>>>

            obj.put("keyCode", event.getKeyCode());
            obj.put("scanCode", event.getScanCode());
            obj.put("repeatCount", event.getRepeatCount());
            //通过 keyCode 获取的            obj.put("number", event.getNumber());
            obj.put("flags", event.getFlags());
            //通过 mMetaState 获取的 obj.put("modifiers", event.getModifiers());
            //通过 mKeyCode 获取的 obj.put("displayLabel", event.getDisplayLabel());
            //通过 mMetaState 获取的 obj.put("unicodeChar", event.getUnicodeChar());
        }
        else if (ie instanceof MotionEvent) {
            MotionEvent event = (MotionEvent) ie;
            obj.put("type", 0);

            //虽然 KeyEvent 和 MotionEvent 都有，但都不在父类 InputEvent 中 <<<<<<<<<<<<<<<<<<
            obj.put("action", event.getAction());
            obj.put("downTime", event.getDownTime());
            obj.put("eventTime", event.getEventTime());
            obj.put("metaState", event.getMetaState());
            obj.put("source", event.getSource());
            obj.put("deviceId", event.getDeviceId());
            //虽然 KeyEvent 和 MotionEvent 都有，但都不在父类 InputEvent 中 >>>>>>>>>>>>>>>>>>

            float relativeY = event.getY() <= dividerY ? event.getY() : (event.getY() - screenHeight);

            obj.put("x", (int) event.getX());
            obj.put("y", (int) relativeY);
            obj.put("rawX", (int) event.getRawX());
            obj.put("rawY", (int) event.getRawY());
            obj.put("size", event.getSize());
            obj.put("pressure", event.getPressure());
            obj.put("xPrecision", event.getXPrecision());
            obj.put("yPrecision", event.getYPrecision());
            obj.put("pointerCount", event.getPointerCount());
            obj.put("edgeFlags", event.getEdgeFlags());
        }

        if (touchList == null) {
            touchList = new JSONArray();
        }
        touchList.add(obj);

        return touchList;
    }



    public static final int REQUEST_UI_AUTO_LIST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_UI_AUTO_LIST) {
            JSONArray array = data == null ? null : JSON.parseArray(data.getStringExtra(UIAutoListActivity.RESULT_LIST));
            finish();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    touchList = new JSONArray();
                    recover(array);
                }
            }, 1000);
        }

    }



    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

}

