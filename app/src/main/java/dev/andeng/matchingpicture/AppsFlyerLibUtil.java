package dev.andeng.matchingpicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppsFlyerLibUtil {

    private static final String TAG = "AppsFlyerLibUtility";

    private static final int SPLASH_TIME_OUT = 4000; // 2 seconds
    private static String gameURL = "";
    private static String appStatus = "";
    private static String apiResponse = "";

    public static void init(Context context) {
        // app flay初始化
        AppsFlyerLib.getInstance().start(context, "LQ4sUsSSSLf8FomYUjFMZ8", new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Launch sent successfully, got 200 response code from server");
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.e(TAG, "Launch failed to be sent:\n" + "Error code: " + i + "\n" + "Error description: " + s);
            }
        });
         AppsFlyerLib.getInstance().setDebugLog(true);
         AppsFlyerLib.getInstance().setLogLevel(AFLogger.LogLevel.DEBUG);

    }


    public static void event(Activity context, String name, String data) {
        Map<String, Object> eventValue = new HashMap<String, Object>();


        /***
         * 开启新窗口跳转
         */
        if ("openWindow".equals(name)) {
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra("url", data);
            context.startActivityForResult(intent, 1);
        } else if ("firstrecharge".equals(name) || "recharge".equals(name)) {
            try {
                Map maps = (Map) JSON.parse(data);
                for (Object map : maps.entrySet()) {
                    String key = ((Map.Entry) map).getKey().toString();
                    if ("amount".equals(key)) {
                        eventValue.put(AFInAppEventParameterName.REVENUE, ((Map.Entry) map).getValue());
                    } else if ("currency".equals(key)) {
                        eventValue.put(AFInAppEventParameterName.CURRENCY, ((Map.Entry) map).getValue());
                    }
                }
            } catch (Exception e) {

            }
        } else if ("withdrawOrderSuccess".equals(name)) {
            // 提现成功
            try {
                Map maps = (Map) JSON.parse(data);
                for (Object map : maps.entrySet()) {
                    String key = ((Map.Entry) map).getKey().toString();
                    if ("amount".equals(key)) {
                        float revenue = 0;
                        String value = ((Map.Entry) map).getValue().toString();
                        if (!TextUtils.isEmpty(value)) {
                            revenue = Float.valueOf(value);
                            revenue = -revenue;
                        }
                        eventValue.put(AFInAppEventParameterName.REVENUE, revenue);

                    } else if ("currency".equals(key)) {
                        eventValue.put(AFInAppEventParameterName.CURRENCY, ((Map.Entry) map).getValue());
                    }
                }
            } catch (Exception e) {

            }
        } else {
            eventValue.put(name, data);
        }
        AppsFlyerLib.getInstance().logEvent(context, name, eventValue);

        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
    }
}