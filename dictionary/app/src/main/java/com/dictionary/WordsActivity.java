package com.dictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dictionary.adapter.SpaceItemDecoration;
import com.dictionary.adapter.WQRecycleAdapter;
import com.dictionary.adapter.WQViewHolder;
import com.dictionary.db.SQLTools;
import com.dictionary.model.WordModel;
import com.dictionary.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WordsActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    WQRecycleAdapter recycleAdapter;
    Toolbar toolbar;

    RelativeLayout over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        toolbar = (Toolbar) findViewById(R.id.words_toolbar);
        toolbar.setTitle("单词列表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        over = (RelativeLayout) findViewById(R.id.words_over);
        recyclerView = (RecyclerView) findViewById(R.id.words_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration(this, SpaceItemDecoration.HORIZONTAL_LIST, 5));


        recycleAdapter = new WQRecycleAdapter(this, R.layout.item_words, MainActivity.list);
        recyclerView.setAdapter(recycleAdapter);

        recycleAdapter.setCallBack(new WQRecycleAdapter.CallBack() {
            @Override
            public <T> void convert(WQViewHolder holder, T bean, int position) {
                WordModel m = (WordModel) bean;
                holder.setText(R.id.item_words, m.getWords());
                holder.setText(R.id.item_meaning, m.getMeaning());
            }
        });

        recycleAdapter.setOnItemClickListner(new WQRecycleAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                Intent intent = new Intent(WordsActivity.this, MeaningActivity.class);
                intent.putExtra("words", MainActivity.list.get(position));
                startActivity(intent);
            }
        });

        recycleAdapter.setOnItemLongClickListner(new WQRecycleAdapter.OnItemLongClickListner() {
            @Override
            public void onItemLongClickListner(View v, int position) {
                MainActivity.list.remove(position);
                recycleAdapter.notifyItemRemoved(position);
                SPUtils.pushWords(WordsActivity.this, MainActivity.list);
                visible();
            }
        });
        visible();
        if (MainActivity.list.size() != 0)
            Snackbar.make(recyclerView, "长按选择记住单词", Snackbar.LENGTH_LONG).show();
    }


    public void visible() {
        if (MainActivity.list.size() == 0) {
            over.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            over.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
