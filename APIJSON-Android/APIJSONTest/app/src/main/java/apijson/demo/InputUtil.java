package apijson.demo;

import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class InputUtil {

    public static String getActionName(int action) {
        String s = StringUtil.getTrimedString(MotionEvent.actionToString(action));
        return s.startsWith("ACTION_") ? s.substring("ACTION_".length()) : s;
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                return "DOWN";
//            case MotionEvent.ACTION_MOVE:
//                return "MOVE";
//            case MotionEvent.ACTION_SCROLL:
//                return "SCROLL";
//            case MotionEvent.ACTION_UP:
//                return "UP";
//            case MotionEvent.ACTION_MASK:
//                return "MASK";
//            case MotionEvent.ACTION_OUTSIDE:
//                return "OUTSIDE";
//            default:
//                return "CANCEL";
//        }
    }

    public static String getOrientationName(int orientation) {
        return orientation == Configuration.ORIENTATION_LANDSCAPE ? "HORIZONTAL" : "VERTICAL";
    }

    public static String getKeyCodeName(int keyCode) {
        return KeyEvent.keyCodeToString(keyCode);
    }

    public static String getScanCodeName(int scanCode) {
        return "" + scanCode;  //它是 hardware key id  KeyEvent.keyCodeToString(scanCode);
    }
}
