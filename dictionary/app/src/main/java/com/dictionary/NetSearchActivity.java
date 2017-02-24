package com.dictionary;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.http.HttpUtls;
import com.dictionary.utils.NetWorkOK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;

public class NetSearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText words;
    private TextView result;
    private Button translation;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netsearch);
        toolbar = (Toolbar) findViewById(R.id.net_toolbar);
        toolbar.setTitle("查找");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        words = (EditText) findViewById(R.id.net_words);
        result = (TextView) findViewById(R.id.net_result);
        translation = (Button) findViewById(R.id.net_translat);

        translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler();
            }
        });

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    String text;

    public void handler() {
        if (!NetWorkOK.isOK(this)) {
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
        }
        text = words.getText().toString().trim();
        if (text.equals("")) {
            Toast.makeText(this, "文本内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        new Thread() {
            @Override
            public void run() {
                text = HttpUtls.getResult(HttpUtls.YOUDAO + URLEncoder.encode(text));
                Log.i("result", text);
                try {
                    JSONObject obj = new JSONObject(text);
                    JSONArray arr = obj.getJSONArray("translation");
                    Message msg = handler.obtainMessage();
                    msg.obj = arr.getString(0);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String r = (String) msg.obj;
            result.setText(r);
        }
    };


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
