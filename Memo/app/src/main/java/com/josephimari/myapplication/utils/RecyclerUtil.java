package com.josephimari.myapplication.utils;

import android.view.View;

/**
 * Created by Joseph on 5/30/2016.
 */
public class RecyclerUtil {

  public interface ClickListener {
    void onItemClick(int position, View v);
  }

  public interface TagClickListener {
    void onItemClick(String payload, View view);
  }

  public interface ObjectClickListener {
    void onItemClick(Object object, View view);
  }

  public interface LongPressListener {
    void onItemLongPressed(int position, View view);
  }

  public interface LongPressTagListener {
    void onItemLongPressed(String payload, View view);
  }

  public interface ObjectLongPressListener {
    void onObjectLongPress(Object object, View view);
  }
}
