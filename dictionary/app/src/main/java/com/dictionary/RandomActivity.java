package com.dictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dictionary.adapter.SpaceItemDecoration;
import com.dictionary.adapter.WQRecycleAdapter;
import com.dictionary.adapter.WQViewHolder;
import com.dictionary.db.SQLTools;
import com.dictionary.model.Search;
import com.dictionary.model.WordModel;
import com.dictionary.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class RandomActivity extends AppCompatActivity {
    public static String SQL = "select * from dictionary order by random() limit 100";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    WQRecycleAdapter recycleAdapter;
    List<Search> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        toolbar = (Toolbar) findViewById(R.id.random_toolbar);
        toolbar.setTitle("随机练习");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.random_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration(this, SpaceItemDecoration.HORIZONTAL_LIST, 5));


        recycleAdapter = new WQRecycleAdapter(this, R.layout.item_words, list);
        recyclerView.setAdapter(recycleAdapter);

        recycleAdapter.setCallBack(new WQRecycleAdapter.CallBack() {
            @Override
            public <T> void convert(WQViewHolder holder, T bean, int position) {
                Search m = (Search) bean;
                holder.setText(R.id.item_words, m.getWords());
                holder.setText(R.id.item_meaning, m.getMeaning());
            }
        });

        recycleAdapter.setOnItemClickListner(new WQRecycleAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                Intent intent = new Intent(RandomActivity.this, MeaningActivity.class);
                intent.putExtra("words", list.get(position));
                startActivity(intent);
            }
        });
        initData();
    }

    private void initData() {

        SQLiteDatabase database = SQLTools.opendatabase(RandomActivity.this);
        Cursor cursor = database.rawQuery(SQL, null);
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
