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
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import apijson.demo.R;


/**自动 UI 测试，需要用 UIAuto 发请求到这个设备
 * https://github.com/TommyLemon/UIAuto
 * @author Lemon
 */
public class UIAutoActivity extends Activity {
    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, UIAutoActivity.class);
    }

    private Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_auto_activity);
        context = this;

        final Display d = getWindowManager().getDefaultDisplay();

        final ScrollView svTouch = findViewById(R.id.svTouch);
        final TextView tvTouch = findViewById(R.id.tvTouch);

        final View tvButton = findViewById(R.id.tvButton);
        final View btnButton = findViewById(R.id.btnButton);

        final View vTouch = findViewById(R.id.vTouch);
        final View llTouch = findViewById(R.id.llTouch);
        final View rlRoot = findViewById(R.id.rlRoot);
        final View vDispatchTouch = findViewById(R.id.vDispatchTouch);


        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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

                tvTouch.setText(Calendar.getInstance().getTime().toLocaleString() +  " action:" + (event.getAction()) + "; x:" + event.getX() + "; y:" + event.getY() + "\n" + s);
//                Toast.makeText(context, "vTouch.action:" + (event.getAction()) + "; x:" + event.getX() + "; y:" + event.getY(), Toast.LENGTH_SHORT).show();

//死循环                llTouch.dispatchTouchEvent(event);
//                vDispatchTouch.dispatchTouchEvent(event);
//                vDispatchTouch.dispatchTouchEvent(event);
               //onTouchEvent 不能处理事件 vDispatchTouch.onTouchEvent(event);
//                vTouch.setOnTouchListener(this);
                return true;  //连续记录只能 return true
            }
        };
        vTouch.setOnTouchListener(listener);

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
                        MotionEvent.ACTION_DOWN, d.getWidth()/2, d.getHeight()/2, 0);

                //TODO onTouchEvent ?
                vTouch.dispatchTouchEvent(e);
                e.setAction(MotionEvent.ACTION_UP);
                vTouch.dispatchTouchEvent(e);

            }
        }, 1000);
    }


    public void onClick(View v) {
        Toast.makeText(context, "onClick BUTTON", Toast.LENGTH_SHORT).show();
    }
}
