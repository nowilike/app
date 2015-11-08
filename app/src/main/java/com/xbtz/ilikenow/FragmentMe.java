package com.xbtz.ilikenow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilikenow on 2015/10/12.
 * Fragment 4 for the bottom navigation
 */
public class FragmentMe extends Fragment {

    private Button button, button2;
    private TextView textView, textView2, textView3;
    private ToggleButton buttonAlarm;
    private EditText editText_alarmPeriod, editText_alarmLow, editText_alarmHigh;
    private Spinner mySpinner, mySpinner3;
    private int selected = 0, selected2 = 0, alarmPeriod = 20;
    private final int SUCCESS = 1, FAILURE = 0, ERROR_CODE = 2;
    protected String Result, Result3;

    static class MyHandler extends Handler {
        WeakReference<FragmentMe> mActivity;
        MyHandler(FragmentMe activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            FragmentMe fragmentMe = mActivity.get();
            switch (msg.what) {
                case 1:
                    // 获取信息成功后，对该信息进行JSON解析，得到所需要的信息，然后在textView上展示出来。
                    fragmentMe.JSONAnalysis(msg.obj.toString(), msg.arg1);
                    Toast.makeText(fragmentMe.getActivity(), "获取数据成功", Toast.LENGTH_SHORT).show();
                    break;
                //case fragmentMe.FAILURE:
                case 0:
                    Toast.makeText(fragmentMe.getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    System.out.println("获取数据失败 " + fragmentMe.FAILURE);
                    break;
                case 2:
                    Toast.makeText(fragmentMe.getActivity(), "获取的CODE码不为200！", Toast.LENGTH_SHORT).show();
                    System.out.println("获取的CODE码不为0！" + msg.what);
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.me, container, false);
        init(rootView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.moneyButton:
                        /**
                         * 点击按钮事件，在主线程中开启一个子线程进行网络请求
                         * （因为在4.0只有不支持主线程进行网络请求，所以一般情况下，建议另开启子线程进行网络请求等耗时操作）。
                         */
                        url_thread(_MakeURL("http://web.juhe.cn:8080/finance/exchange/rmbquot", new HashMap<String, Object>() {{
                            put("key", "58cf31f8f87358e3c825b85d69a0c1ec");
                            put("type", "0");
                        }}), 0);
                        break;
                    default:
                        break;
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.moneyButton2:
                        url_thread(_MakeURL("http://op.juhe.cn/onebox/exchange/query", new HashMap<String, Object>() {{
                            put("key", "74d42f9b989fe5d7dab710839ec3fd9d");
                        }}), 1);
                        url_thread(_MakeURL("http://op.juhe.cn/onebox/exchange/currency", new HashMap<String, Object>() {{
                            put("key", "74d42f9b989fe5d7dab710839ec3fd9d");
                            put("from", "EUR");
                            put("to", "CNY");
                        }}), 2);
                        break;

                    default:
                        break;
                }
            }
        });

        buttonAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                alarmPeriod = Integer.parseInt((editText_alarmPeriod.getText().toString()));
                alarmPeriod = alarmPeriod > 10 ? alarmPeriod : 10;
                Intent intent = new Intent(getActivity(), MyReceiver.class);
                intent.setAction("VIDEO_TIMER");
                Bundle bundle = new Bundle();
                bundle.putFloat("low", Float.parseFloat(editText_alarmLow.getText().toString()));
                bundle.putFloat("high", Float.parseFloat(editText_alarmHigh.getText().toString()));
                //System.out.println("==> " + bundle.getFloat("low") + " " + bundle.getFloat("high") );
                intent.putExtras(bundle); //为Intent追加额外的数据
                PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (isChecked) {
                    editText_alarmHigh.setEnabled(false);
                    editText_alarmLow.setEnabled(false);
                    editText_alarmPeriod.setEnabled(false);
                    AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), alarmPeriod * 1000, sender);
                } else {
                    editText_alarmHigh.setEnabled(true);
                    editText_alarmLow.setEnabled(true);
                    editText_alarmPeriod.setEnabled(true);
                    AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    am.cancel(sender);
                }
            }
        });

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 第position项被选中时激发该方法
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //System.out.println(position + "被选中了");
                selected = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mySpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 第position项被选中时激发该方法
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //System.out.println(position + "被选中了");
                selected2 = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }

    private void url_thread(final String path, final int arg1) {
        new Thread() {
            public void run() {
                int code;
                try {
                    URL url = new URL(path);
                    /**
                     * 这里网络请求使用的是类HttpURLConnection，另外一种可以选择使用类HttpClient。
                     */
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");//使用GET方法获取
                    conn.setConnectTimeout(5000);
                    code = conn.getResponseCode();
                    //System.out.println(path+" "+code);
                    if (code == 200) {
                        /**
                         * 如果获取的code为200，则证明数据获取是正确的。
                         */
                        InputStream is = conn.getInputStream();
                        String result = HttpUtils.readMyInputStream(is);
                        /**
                         * 子线程发送消息到主线程，并将获取的结果带到主线程，让主线程来更新UI。
                         */
                        Message msg = new Message();
                        msg.obj = result;
                        msg.arg1 = arg1;
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = ERROR_CODE;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    /**
                     * 如果获取失败，或出现异常，那么子线程发送失败的消息（FAILURE）到主线程，主线程显示Toast，来告诉使用者，数据获取是失败。
                     */
                    Message msg = new Message();
                    msg.what = FAILURE;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void init(View view) {
        button = (Button) view.findViewById(R.id.moneyButton);
        textView = (TextView) view.findViewById(R.id.moneyTextView);
        button2 = (Button) view.findViewById(R.id.moneyButton2);
        buttonAlarm = (ToggleButton) view.findViewById(R.id.moneyButtonAlarm);
        textView2 = (TextView) view.findViewById(R.id.moneyTextView2);
        textView3 = (TextView) view.findViewById(R.id.moneyTextView3);
        editText_alarmPeriod = (EditText) view.findViewById(R.id.textAlarmPeriod);
        editText_alarmLow = (EditText) view.findViewById(R.id.textAlarmValueLow);
        editText_alarmHigh = (EditText) view.findViewById(R.id.textAlarmValueHigh);
        mySpinner = (Spinner) view.findViewById(R.id.moneySpinner1);
        mySpinner3 = (Spinner) view.findViewById(R.id.moneySpinner3);
    }

    protected void JSONAnalysis(String string, int arg) {
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
            if (arg == 0) {
                JSONArray myArray = object.optJSONArray("result");
                JSONObject array0 = (JSONObject) myArray.opt(0);
                String myString = mySpinner.getItemAtPosition(selected).toString();
                int i;
                for (i = 0; i < array0.length() - 1; ++i)
                    if (myString.equals(array0.optJSONObject("data" + String.valueOf(i + 1)).optString("name")))
                        break;
                JSONObject ObjectInfo = array0.optJSONObject("data" + String.valueOf(i + 1));
                String city = ObjectInfo.optString("name");
                String date = ObjectInfo.optString("fBuyPri");
                String week = ObjectInfo.optString("mBuyPri");
                String temp = ObjectInfo.optString("fSellPri");
                String weather = ObjectInfo.optString("mSellPri");
                String index = ObjectInfo.optString("bankConversionPri");
                String the_date = ObjectInfo.optString("date");
                String the_time = ObjectInfo.optString("time");
                Result = "币种：            " + city + "\n现汇买入：    " + date + "\n现钞买入：    " + week
                        + "\n现汇卖出：    " + temp + "\n现钞卖出：    " + weather + "\n银行中间价：" + index
                        + "\n更新日期：    " + the_date + "\n更新时间：    " + the_time;

                textView.setText(Result);
            } else if (arg == 1) {
                JSONObject myArray = object.optJSONObject("result");
                JSONArray array0 = myArray.optJSONArray("list");
                String myString = mySpinner3.getItemAtPosition(selected2).toString();
                int i;
                for (i = 0; i < array0.length(); ++i) {
                    if (myString.equals(array0.optJSONArray(i).optString(0).substring(0, 2)))
                        break;
                }
                JSONArray ObjectInfo = array0.optJSONArray(i);
                String the_time = myArray.optString("update");
                if (ObjectInfo != null) {
                    Result = "币种：            " + ObjectInfo.optString(0) + "\n现汇买入：    " + ObjectInfo.optString(3) + "\n现钞买入：    " + ObjectInfo.optString(4)
                            + "\n现汇卖出：    " + ObjectInfo.optString(5) + "\n现钞卖出：    " + ObjectInfo.optString(5) + "\n银行中间价：" + ObjectInfo.optString(2)
                            + "\n更新日期：    " + the_time.substring(0, 10) + "\n更新时间：    " + the_time.substring(11);
                    textView2.setText(Result);
                }
            } else {
                JSONArray myArray = object.optJSONArray("result");
                JSONObject ObjectInfo = myArray.optJSONObject(0);

                Result3 = "\n币种：    " + ObjectInfo.optString("currencyF_Name")
                        + "\n币种：    " + ObjectInfo.optString("currencyT_Name")
                        + "\n汇率：    " + ObjectInfo.optString("result")
                        + "\n时间：    " + ObjectInfo.optString("updateTime");

                textView3.setText(Result3);
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
