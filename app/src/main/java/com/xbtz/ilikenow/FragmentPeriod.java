package com.xbtz.ilikenow;

//import android.app.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ilikenow on 2015/10/12.
 * Fragment 2 for the bottom navigation
 */
public class FragmentPeriod extends Fragment implements View.OnClickListener {
    EditText url;
    WebView show;
    Button button, button_settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.address, null);
        View rootView = inflater.inflate(R.layout.address, container, false);
        url = (EditText) rootView.findViewById(R.id.et_u_rl);
        show = (WebView) rootView.findViewById(R.id.show);
        WebSettings webSettings = show.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        //webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //show.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        button = (Button) rootView.findViewById(R.id.web_button);
        button_settings = (Button) rootView.findViewById(R.id.settings_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlStr = url.getText().toString();
                System.out.println(urlStr + "被访问了");
                //    加载、并显示urlStr对应的网页
                show.loadUrl(urlStr);
            }
        });
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        show.setWebViewClient(new WebViewClient()
//        {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url)
//            {
//                view.loadUrl(url); // 在当前的webview中跳转到新的url
//                return true;
//            }
        );
        return rootView;
    }


    @Override
    public void onClick(View view) {//to do
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_MENU)
//        {
//            String urlStr = url.getText().toString();
//            // 加载、并显示urlStr对应的网页
//            show.loadUrl(urlStr);
//            return true;
//        }
//        return false;
//    }

//    //Web视图
//    private class webViewClient extends WebViewClient {
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }

}
