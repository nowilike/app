package com.xbtz.ilikenow;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class MoneyIntentService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    //private static final String ACTION_FOO = "com.xbtz.ilikenow.action.FOO";
//    private static final String ACTION_BAZ = "com.xbtz.ilikenow.action.BAZ";
//

//    private static final String EXTRA_PARAM1 = "com.xbtz.ilikenow.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.xbtz.ilikenow.extra.PARAM2";

    private NotificationManager nm;
    static final int NOTIFICATION_ID = 0x123;
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

//    public static void startActionFoo(Context context, float param1, float param2) {
//        Intent intent = new Intent(context, MoneyIntentService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, MoneyIntentService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }
    public MoneyIntentService() {
        super("MoneyIntentService");
    }

//    public void onCreate() {
//
//        super.onCreate();
//        //Bundle bundle = .getExtras();
//        startActionFoo(this, 0, 0);
//       // Log.v("MoneyIntentService", "OnCreate");
//    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        return START_STICKY;
//    }

//    public void onStart(Intent intent, int startId) {
//
//        new Thread()
//        {
//            @Override
//            public void run()
//            {
//            }
//        }.start();
//    }

//    public void onDestroy() {
//
//        super.onDestroy();
//        Log.v("MoneyIntentService", "onDestroy");
//    }
//
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            nm = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            //final String action = intent.getAction();
            Bundle bundle = intent.getExtras();
//            if (ACTION_FOO.equals(action)) {
            final float low = bundle.getFloat("low");
            final float high = bundle.getFloat("high");
            handleActionFoo(low, high);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(float low, float high) {

        int code;
        try {
            URL url = new URL(_MakeURL("http://op.juhe.cn/onebox/exchange/currency", new HashMap<String, Object>() {{
                put("key", "74d42f9b989fe5d7dab710839ec3fd9d");
                put("from", "EUR");
                put("to", "CNY");
            }}));
            // 这里网络请求使用的是类HttpURLConnection，另外一种可以选择使用类HttpClient。
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");//使用GET方法获取
            conn.setConnectTimeout(5000);
            code = conn.getResponseCode();
            //System.out.println(path+" "+code);
            if (code == 200) {
                //如果获取的code为200，则证明数据获取是正确的。
                InputStream is = conn.getInputStream();
                String result = HttpUtils.readMyInputStream(is);
                JSONAnalysis(result, low, high);
            } else {
                System.out.println("Service ==> 服务数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Service ==> 服务数据异常");
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
//    private void handleActionBaz(String param1, String param2) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
    @TargetApi(16)
    protected void JSONAnalysis(String string, float arg1, float arg2) {
        JSONObject object = null;
        try {
            object = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * 在你获取的string这个JSON对象中，提取你所需要的信息。
         */
        if (object != null) {
            JSONArray myArray = object.optJSONArray("result");
            JSONObject ObjectInfo = myArray.optJSONObject(0);
            String Result3 = "币种：" + ObjectInfo.optString("currencyF_Name")
                    + " 币种：" + ObjectInfo.optString("currencyT_Name")
                    + " 汇率：" + ObjectInfo.optString("result")
                    + " 时间：" + ObjectInfo.optString("updateTime");
            float result = Float.parseFloat(ObjectInfo.optString("result"));
            System.out.println(Result3 + " " + arg1 + " " + arg2);
            if ( arg1 > result || arg2 < result ) {
                System.out.println(Result3 + " " + arg1 + " " + arg2);
                if (Build.VERSION.SDK_INT >= 16) {
                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
                    String noti_str = arg1 > result ? "下降" : "上升";
                    Notification notify = new Notification.Builder(this)
                            // 设置打开该通知，该通知自动消失
                            .setAutoCancel(true)
                                    // 设置显示在状态栏的通知提示信息
                            .setTicker("有新消息")
                                    // 设置通知的图标
                            .setSmallIcon(R.mipmap.icon)
                                    // 设置通知内容的标题
                            .setContentTitle("汇率" + noti_str)
                                    // 设置通知内容
                            .setContentText("汇率" + ObjectInfo.optString("result") + noti_str + "过了预警值！")
                                    // 设置使用系统默认的声音、默认LED灯
                            .setDefaults(
                                    // Notification.DEFAULT_SOUND |
                                    Notification.DEFAULT_LIGHTS)
                                    // 设置通知的自定义声音
//                    .setSound(Uri.parse("android.resource://org.crazyit.ui/"
//                            + R.raw.msg))
                                    //.setWhen(System.currentTimeMillis())
                                    // 设改通知将要启动程序的Intent
                            .setContentIntent(pi)  // ①
                            .build();
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                    if (settings.getBoolean("notifications_alarmSound", false))
                        notify.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
                    // 发送通知
                    nm.notify(NOTIFICATION_ID, notify);
                }
            }
        }
    }

    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0)
            url.append('?');
        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }
        return url.toString().replace("?&", "?");
    }
}
