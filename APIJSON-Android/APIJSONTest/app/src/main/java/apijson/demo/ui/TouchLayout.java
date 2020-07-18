package apijson.demo.ui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.Calendar;

public class TouchLayout extends RelativeLayout {
    private static final String TAG = "TouchLayout";

    public TouchLayout(@NonNull Context context) {
        super(context);
    }

    public TouchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent  " + Calendar.getInstance().getTime().toLocaleString() +  " action:" + (ev.getAction()) + "; x:" + ev.getX() + "; y:" + ev.getY());

        super.dispatchTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent  " + Calendar.getInstance().getTime().toLocaleString() +  " action:" + (ev.getAction()) + "; x:" + ev.getX() + "; y:" + ev.getY());

        super.onTouchEvent(ev);
        return false;
    }
}