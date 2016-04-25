package com.yoyotest.h2owl.h2wltestapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
        CheckBox noteCheckBox;
        TextView noteTitle;
        TextView noteId;
    }

    public MyAdapter(Context context, OrderedRealmCollection<MyNote> realmResults) {
        super(context, realmResults, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.noteId = (TextView) convertView.findViewById(R.id.note_id);
            viewHolder.noteTitle = (TextView) convertView.findViewById(R.id.note_title);
            viewHolder.noteCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_done);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyNote item = adapterData.get(position);
        viewHolder.noteId.setText(String.valueOf(item.id));
        viewHolder.noteTitle.setText(item.title);
        viewHolder.noteCheckBox.setVisibility(item.type == 1 ? View.VISIBLE : View.GONE);
        viewHolder.noteCheckBox.setChecked(item.state == 1);
        return convertView;
    }

    public OrderedRealmCollection<MyNote> getAdapterData() {
        return adapterData;
    }
}
