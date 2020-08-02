package apijson.demo.ui;

import android.view.MotionEvent;

public class TouchUtil {

    public static String getActionName(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "DOWN";
            case MotionEvent.ACTION_MOVE:
                return "MOVE";
            case MotionEvent.ACTION_SCROLL:
                return "SCROLL";
            case MotionEvent.ACTION_UP:
                return "UP";
            case MotionEvent.ACTION_MASK:
                return "MASK";
            case MotionEvent.ACTION_OUTSIDE:
                return "OUTSIDE";
            default:
                return "CANCEL";
        }
    }
}
