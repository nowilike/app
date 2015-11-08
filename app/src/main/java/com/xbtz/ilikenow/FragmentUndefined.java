package com.xbtz.ilikenow;

//import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ilikenow on 2015/10/12.
 * Fragment 3 for the bottom navigation
 */
public class FragmentUndefined extends Fragment{

    Button visitWebBtn = null;
    Button downImgBtn = null;
    TextView showTextView = null;
    ImageView showImageView = null;
    EditText textURL = null;
    String resultStr = "";
    ProgressBar progressBar = null;
    ViewGroup viewGroup = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.find, container, false);
        initUI(rootView);
        visitWebBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showImageView.setVisibility(View.GONE);
                showTextView.setVisibility(View.VISIBLE);
                Thread visitBaiduThread = new Thread(new VisitWebRunnable());
                visitBaiduThread.start();
                try {
                    visitBaiduThread.join();
                    if (!resultStr.equals("")) {
                        showTextView.setText(resultStr);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        downImgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showImageView.setVisibility(View.VISIBLE);
                showTextView.setVisibility(View.GONE);
                String imgUrl = "http://www.shixiu.net/d/file/p/2bc22002a6a61a7c5694e7e641bf1e6e.jpg";
                new DownImgAsyncTask().execute(imgUrl);
            }
        });

        return rootView;
    }


    public void initUI(View view){
        showTextView = (TextView)view.findViewById(R.id.textview_show);
        showImageView = (ImageView)view.findViewById(R.id.imagview_show);
        downImgBtn = (Button)view.findViewById(R.id.btn_download_img);
        visitWebBtn = (Button)view.findViewById(R.id.btn_visit_web);
        showTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textURL = (EditText)view.findViewById(R.id.text_URL);
    }
    /**
     * 获取指定URL的响应字符串
     * @param urlString url from EditText
     * @return resultData
     */
    private String getURLResponse(String urlString){
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(urlString); //URL对象
            conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine;
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

//        } catch (MalformedURLException e) {
//            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }

        return resultData;
    }

    /**
     * 从指定URL获取图片
     * @param url url pre-defined
     * @return bitmap
     */
    private Bitmap getImageBitmap(String url){
        URL imgUrl;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }
    class VisitWebRunnable implements Runnable{
        @Override
        public void run() {
            //String data = getURLResponse("http://www.baidu.com/");
            //String data = getURLResponse("https://secure.globalblue.com/secure/traveller-services/refundtracker/");
            //String data = getURLResponse("http://finance.sina.com.cn/money/forex/hq/EURCNY.shtml");
            resultStr = getURLResponse(textURL.getText().toString());
        }
    }
    class DownImgAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showImageView.setImageBitmap(null);
            showProgressBar();//显示进度条提示框
        }

        @Override
        protected Bitmap doInBackground(String... params) {
                        return getImageBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result!=null){
                dismissProgressBar();
                showImageView.setImageBitmap(result);
            }
        }

    }
    /**
     * 在母布局中间显示进度条
     */
    private void showProgressBar(){
        progressBar = new ProgressBar(this.getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,  RelativeLayout.TRUE);
        progressBar.setVisibility(View.VISIBLE);
        //Context context = getActivity().getApplicationContext();
        viewGroup = (ViewGroup)this.getActivity().findViewById(R.id.parent_view);
        //      MainActivity.this.addContentView(progressBar, params);
        viewGroup.addView(progressBar, params);
    }
    /**
     * 隐藏进度条
     */
    private void dismissProgressBar(){
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
            viewGroup.removeView(progressBar);
            progressBar = null;
        }
    }
}
