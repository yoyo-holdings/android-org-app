package com.yoyotest.h2owl.h2wltestapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yoyotest.h2owl.h2wltestapp.model.MyNote;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by h2owl on 16/04/25.
 */
public class MyAdapter extends RealmBaseAdapter<MyNote> implements ListAdapter {

    private static class ViewHolder {
        TextView noteTitle;
    }

    public MyAdapter(Context context, OrderedRealmCollection<MyNote> realmResults) {
        super(context, realmResults, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.noteTitle = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyNote item = adapterData.get(position);
        viewHolder.noteTitle.setText(item.title);
        return convertView;
    }

    public OrderedRealmCollection<MyNote> getAdapterData() {
        return adapterData;
    }
}
