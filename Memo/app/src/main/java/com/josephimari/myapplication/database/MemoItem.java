package com.josephimari.myapplication.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Joseph on 5/30/2016.
 */
@Table(database = MemoDB.class) public class MemoItem extends BaseModel {

  public static final int TYPE_NOTE = 0;
  public static final int TYPE_TODO = 1;

  @Column @NotNull @PrimaryKey(autoincrement = true) public int memoId;
  @Column public long dateCreated;
  @Column public String title;
  @Column public String message;
  @Column public long lastModified;
  @Column public int type;
  @Column public boolean taskCompleted;

  public MemoItem() {
  }

  public MemoItem(String title, String message, long dateCreated, long lastModified, int type,
      boolean taskCompleted) {
    this.dateCreated = dateCreated;
    this.title = title;
    this.message = message;
    this.lastModified = lastModified;
    this.type = type;
    this.taskCompleted = taskCompleted;
  }

  public void setMemoId(int memoId) {
    this.memoId = memoId;
  }

  @Override public String toString() {
    return "MemoItem{" +
        "memoId='" + memoId + '\'' +
        '}';
  }
}
