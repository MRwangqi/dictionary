package com.dictionary;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.http.HttpUtls;
import com.dictionary.model.WordModel;
import com.dictionary.utils.NetWorkOK;
import com.dictionary.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MeaningActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView words, lx, meaning, quary;
    private WebView webView;
    WordModel m;
    private ProgressDialog progressDialog;
    private LinearLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning);
        m = (WordModel) getIntent().getSerializableExtra("words");

        toolbar = (Toolbar) findViewById(R.id.meaning_toolbar);
        toolbar.setTitle(m.getWords());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        words = (TextView) findViewById(R.id.meaning_words);
        lx = (TextView) findViewById(R.id.meaning_lx);
        meaning = (TextView) findViewById(R.id.meaning_meaning);
        quary = (TextView) findViewById(R.id.meaning_quary);
        webView = (WebView) findViewById(R.id.meaning_webview);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);

        initWebView();
        words.setText(m.getWords());
        meaning.setText(Html.fromHtml(m.getMeaning()));
        String arr[];
        String str = "";
        if (m.getLx().contains("/r/n")) {
            arr = m.getLx().split("/r/n");
            for (String a : arr) {
                str += a.trim() + "\r\n\r\n";
            }
        }
        if (m.getLx().equals("")) {
            lx.setText("无,可以联网查询");
        } else {
            lx.setText(str);
        }

        quary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!NetWorkOK.isOK(MeaningActivity.this)) {
                    Toast.makeText(MeaningActivity.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                loadNetData();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载...");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            webView.loadUrl((String) msg.obj);
            progressDialog.dismiss();
        }
    };

    public void loadNetData() {
        new Thread() {
            @Override
            public void run() {
                String result = HttpUtls.getResult(HttpUtls.WORDS+m.getWords());
                Log.i("res", result);
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject data = obj.getJSONObject("data");
                    String url = data.getString("url");
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = url;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                }

            }
        }.start();
    }


    private void initWebView() {
        // 设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        webView.getSettings().setTextZoom(70);
        // 设置背景颜色 透明
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置缓存模式
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // //添加Javascript调用java对象
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        // 扩大比例的缩放设置此属性，可任意比例缩放。
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBlockNetworkImage(false);
        // 不启用硬件加速
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 自适应屏幕
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
