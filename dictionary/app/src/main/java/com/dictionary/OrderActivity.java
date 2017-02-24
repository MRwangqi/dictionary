package com.dictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dictionary.adapter.SpaceItemDecoration;
import com.dictionary.adapter.WQRecycleAdapter;
import com.dictionary.adapter.WQViewHolder;
import com.dictionary.db.SQLTools;
import com.dictionary.model.Search;
import com.dictionary.model.WordModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {


    private Toolbar toolbar;
    int page = 0;
    private XRecyclerView xRecyclerView;
    WQRecycleAdapter recycleAdapter;

    List<Search> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        toolbar = (Toolbar) findViewById(R.id.order_toolbar);
        toolbar.setTitle("顺序练习");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        xRecyclerView = (XRecyclerView) findViewById(R.id.order_recycle);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        xRecyclerView.addItemDecoration(new SpaceItemDecoration(this, SpaceItemDecoration.HORIZONTAL_LIST, 5));

        recycleAdapter = new WQRecycleAdapter(this, R.layout.item_words, list);
        initData();
        xRecyclerView.setAdapter(recycleAdapter);

        xRecyclerView.setPullRefreshEnabled(false);

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
                Intent intent = new Intent(OrderActivity.this, MeaningActivity.class);
                intent.putExtra("words", list.get(position - 1));
                startActivity(intent);
            }
        });

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                initData();
                xRecyclerView.loadMoreComplete();
            }
        });
    }


    private void initData() {
        String SQL = "select * from dictionary  limit ";
        SQLiteDatabase database = SQLTools.opendatabase(OrderActivity.this);
        Cursor cursor = database.rawQuery(SQL + (page * 10) + ",10", null);
        int words = cursor.getColumnIndex("words");
        int meaning = cursor.getColumnIndex("meaning");
        int lx = cursor.getColumnIndex("lx");
        int id = cursor.getColumnIndex("id");
        while (cursor.moveToNext()) {
            if (cursor.getString(words) == null) {
                continue;
            }
            Search m = new Search();
            m.setId(cursor.getString(id));
            m.setLx(cursor.getString(lx));
            m.setMeaning(cursor.getString(meaning));
            m.setWords(cursor.getString(words));
            list.add(m);
        }
        cursor.close();
        database.close();
        page++;
        recycleAdapter.notifyDataSetChanged();
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
