package com.dictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dictionary.R;
import com.dictionary.model.WordModel;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */

public class SearchAdapter extends BaseAdapter {
    List<WordModel> list;
    Context context;

    public SearchAdapter(Context context, List<WordModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        WordModel m = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, parentView, false);
            holder.words = (TextView) convertView.findViewById(R.id.item_search_words);
            holder.meaning = (TextView) convertView.findViewById(R.id.item_search_meaning);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.words.setText(m.getWords());
        holder.meaning.setText(m.getMeaning());
        return convertView;
    }


    private class ViewHolder {
        TextView words, meaning;
    }
}
