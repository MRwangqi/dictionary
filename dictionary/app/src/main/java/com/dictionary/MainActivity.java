package com.dictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.db.SQLTools;
import com.dictionary.model.WordModel;
import com.dictionary.utils.SPUtils;
import com.dictionary.widget.CirclrProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CirclrProgressBar progressBar;

    private Typeface typeface;
    private TextView hi;
    private TextView completeWords;
    private TextView allWords;
    private TextView otherWords;
    private Button startLearn;
    private LinearLayout searchLayout;
    private LinearLayout orderLayout;
    private LinearLayout randomLayout;
    private LinearLayout netLayout;
    public static String SQL = "select * from dictionary order by random() limit 100";

    public static List<WordModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (CirclrProgressBar) findViewById(R.id.circlrProgressBar);
        typeface = Typeface.createFromAsset(getAssets(), "ziti.ttf");

        hi = (TextView) findViewById(R.id.hi);
        completeWords = (TextView) findViewById(R.id.complete_words);
        allWords = (TextView) findViewById(R.id.all_words);
        otherWords = (TextView) findViewById(R.id.other_words);
        startLearn = (Button) findViewById(R.id.start_lean);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        orderLayout = (LinearLayout) findViewById(R.id.main_order);
        randomLayout = (LinearLayout) findViewById(R.id.main_random);
        netLayout = (LinearLayout) findViewById(R.id.main_search);
        hi.setTypeface(typeface);
        completeWords.setTypeface(typeface);
        allWords.setTypeface(typeface);

        startLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startlearn();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        //顺序练习
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
            }
        });
        //随机练习
        randomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RandomActivity.class));
            }
        });
        //网络查找
        netLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NetSearchActivity.class));
            }
        });


    }


    public void startlearn() {
        Intent intent = new Intent(this, WordsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        float count;
        List<WordModel> modelList = SPUtils.popWords(this);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String day = sdf.format(new Date());

        //判断存储的日期是否是今天，不是的话就要重新去数据库取数据
        if (modelList == null || !SPUtils.popDay(this).equals(day)) {
            //如果取出的数据为空，并且还是今天的话，就说明数据已经被读取完了
            if (modelList == null && SPUtils.popDay(this).equals(day)) {
                Toast.makeText(this, "今日联系已完成", Toast.LENGTH_SHORT).show();
            } else {
                initData(day);
            }
        } else {
            int size = modelList.size();
            count = 100.0f - size;
            completeWords.setText((100 - size) + "");
            if (size == 0) {
                startLearn.setText("已完成今日的练习");
                otherWords.setVisibility(View.GONE);
            } else {
                otherWords.setText("还剩" + size + "个单词");
            }
            progressBar.setBili(count / 100.0f);
            progressBar.start();

            list.clear();
            list.addAll(modelList);
        }
        super.onResume();
    }

    private void initData(String day) {

        SQLiteDatabase database = SQLTools.opendatabase(MainActivity.this);
        Cursor cursor = database.rawQuery(SQL, null);
        int words = cursor.getColumnIndex("words");
        int meaning = cursor.getColumnIndex("meaning");
        int lx = cursor.getColumnIndex("lx");
        int id = cursor.getColumnIndex("id");
        while (cursor.moveToNext()) {
            if (cursor.getString(words) == null) {
                continue;
            }
            WordModel m = new WordModel();
            m.setId(cursor.getString(id));
            m.setLx(cursor.getString(lx));
            m.setMeaning(cursor.getString(meaning));
            m.setWords(cursor.getString(words));
            list.add(m);
        }
        //存储当前日期
        SPUtils.pushDay(this, day);
        //存储list数据
        SPUtils.pushWords(this, list);
        cursor.close();
        database.close();
    }
}
