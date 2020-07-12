package apijson.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.Headers;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import apijson.demo.R;
import apijson.demo.StringUtil;


public class UnitActivity extends Activity implements HttpServerRequestCallback {
    private static final String TAG = "UnitActivity";

    /**
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, UnitActivity.class);
    }


    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer mAsyncServer = new AsyncServer();

    private Activity context;
    private boolean isAlive;

    private TextView etUnitPort;
    private TextView tvUnitRequest;
    private TextView tvUnitResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_activity);
        context = this;
        isAlive = true;

        etUnitPort = findViewById(R.id.etUnitPort);
        tvUnitRequest = findViewById(R.id.tvUnitRequest);
        tvUnitResponse = findViewById(R.id.tvUnitResponse);
        etUnitPort.setText(port);
    }


    private String port = "8080";
    public void start(View v) {
        v.setEnabled(false);
        port = StringUtil.getString(etUnitPort);

        etUnitPort.setText(port + " is starting...");
        startServer();

        etUnitPort.setText(port + " is listening...");
        etUnitPort.setEnabled(false);
        v.setEnabled(true);
    }
    public void stop(View v) {
        v.setEnabled(false);
        server.stop();
        mAsyncServer.stop();

        etUnitPort.setText(port);
        etUnitPort.setEnabled(true);
        v.setEnabled(true);
    }


    public void copy(View v) {
        StringUtil.copyText(context, StringUtil.getString((TextView) v));
    }

    private void startServer() {
//        server.addAction("OPTIONS","*", this);
//        server.get("/test", new HttpServerRequestCallback() {
//            @Override
//            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
//                response.send("{\"hello\": \"world!\"}");
//            }
//        });

//        server.post("/get", this);
        server.addAction("OPTIONS", "[\\d\\D]*", this);
        server.get("[\\d\\D]*", this);
//        server.post("/get", this);
        server.post("[\\d\\D]*", this);
        server.listen(mAsyncServer, Integer.valueOf(port));

    }

    @Override
    public void onRequest(final AsyncHttpServerRequest asyncHttpServerRequest, final AsyncHttpServerResponse asyncHttpServerResponse) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (isAlive) {
                    tvUnitRequest.setText(StringUtil.getString(asyncHttpServerRequest) + "\n\n\n\n\n" + StringUtil.getString(tvUnitRequest));
                }
            }
        });

        AsyncHttpRequestBody requestBody = asyncHttpServerRequest.getBody();

        Headers allHeaders = asyncHttpServerResponse.getHeaders();
        Headers reqHeaders = asyncHttpServerRequest.getHeaders();

        String corsHeaders = reqHeaders.get("access-control-request-headers");
        String corsMethod = reqHeaders.get("access-control-request-method");

//      if ("OPTIONS".toLowerCase().equals(asyncHttpServerRequest.getMethod().toLowerCase())) {

        String origin = reqHeaders.get("origin");
        String cookie = reqHeaders.get("cookie");

        allHeaders.set("Access-Control-Allow-Origin", TextUtils.isEmpty(origin) ? "*" : origin);
        allHeaders.set("Access-Control-Allow-Credentials", "true");
        allHeaders.set("Access-Control-Allow-Headers", TextUtils.isEmpty(corsHeaders) ? "*" : corsHeaders);
        allHeaders.set("Access-Control-Allow-Methods", TextUtils.isEmpty(corsMethod) ? "*" : corsMethod);
        allHeaders.set("Access-Control-Max-Age", "86400");
        if (TextUtils.isEmpty(cookie) == false) {
            allHeaders.set("Set-Cookie", cookie + System.currentTimeMillis());
        }
//    }

        try {
            JSONObject obj = new JSONObject();
            obj.put("code", 200);
            obj.put("msg", "success");
            asyncHttpServerResponse.code(200);

            switch (asyncHttpServerRequest.getPath()) {
                case "/invokeMethod":
                    obj.put("api", "/invokeMethod");
                    break;
                case "/listMethod":
                    obj.put("api", "/listMethod");
                    break;
            }

            asyncHttpServerResponse.send("OPTIONS".toLowerCase().equals(asyncHttpServerRequest.getMethod().toLowerCase()) ? new JSONObject() : obj);
//            asyncHttpServerResponse.send("application/json; charset=utf-8", obj.toString());

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (isAlive) {
                        tvUnitResponse.setText(StringUtil.getString(asyncHttpServerResponse) + "\n\n\n\n\n" + StringUtil.getString(tvUnitResponse));
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        super.onDestroy();
    }


}
