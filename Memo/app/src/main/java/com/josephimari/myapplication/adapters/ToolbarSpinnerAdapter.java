package com.josephimari.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.josephimari.myapplication.R;

/**
 * Created by Joseph on 5/30/2016.
 */
public class ToolbarSpinnerAdapter extends BaseAdapter {

  Context context;
  String[] items;

  public ToolbarSpinnerAdapter(Context context, String[] items) {
    this.context = context;
    this.items = items;
  }

  @Override public int getCount() {
    return items.length;
  }

  @Override public String getItem(int position) {
    return items[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getDropDownView(int position, View view, ViewGroup parent) {
    if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
      view = ((Activity) context).getLayoutInflater()
          .inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
      view.setTag("DROPDOWN");
    }

    TextView textView = (TextView) view.findViewById(android.R.id.text1);
    textView.setText(getTitle(position));

    return view;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
      view = ((Activity) context).getLayoutInflater().inflate(R.layout.
          toolbar_spinner_item_actionbar, parent, false);
      view.setTag("NON_DROPDOWN");
    }
    TextView textView = (TextView) view.findViewById(android.R.id.text1);
    textView.setText(getTitle(position));
    return view;
  }

  private String getTitle(int position) {
    return position >= 0 && position < items.length ? items[position] : "";
  }
}
