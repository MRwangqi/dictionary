package com.dictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dictionary.adapter.SearchAdapter;
import com.dictionary.adapter.WQRecycleAdapter;
import com.dictionary.adapter.WQViewHolder;
import com.dictionary.db.SQLTools;
import com.dictionary.model.Search;
import com.dictionary.model.WordModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<Search> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText search;
    private ImageView delete;
    WQRecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.search_recycle);
        search = (EditText) findViewById(R.id.search_search);
        delete = (ImageView) findViewById(R.id.search_delete);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycleAdapter = new WQRecycleAdapter(this, R.layout.item_search, list);
        recyclerView.setAdapter(recycleAdapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
            }
        });
        recycleAdapter.setCallBack(new WQRecycleAdapter.CallBack() {
            @Override
            public <T> void convert(WQViewHolder holder, T bean, int position) {
                Search m = (Search) bean;
                holder.setText(R.id.item_search_words, m.getWords());
                holder.setText(R.id.item_search_meaning, m.getMeaning());
            }
        });

        recycleAdapter.setOnItemClickListner(new WQRecycleAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                Intent intent = new Intent(SearchActivity.this, MeaningActivity.class);
                intent.putExtra("words", list.get(position));
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")) {
                    list.clear();
                    recycleAdapter.notifyDataSetChanged();
                    return;
                } else {
//                    Toast.makeText(SearchActivity.this, "" + charSequence, Toast.LENGTH_SHORT).show();
                    quaryLike(charSequence + "");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void quaryLike(String str) {


        String sql = "select * from dictionary where words like " + "\'" + str + "%\' limit 10";
        list.clear();
        recycleAdapter.notifyDataSetChanged();

        if (str.equals("")) return;

        SQLiteDatabase database = SQLTools.opendatabase(this);
        Cursor cursor = database.rawQuery(sql, null);
        int words = cursor.getColumnIndex("words");
        int meaning = cursor.getColumnIndex("meaning");
        int lx = cursor.getColumnIndex("lx");
        int id = cursor.getColumnIndex("id");
        while (cursor.moveToNext()) {
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
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
