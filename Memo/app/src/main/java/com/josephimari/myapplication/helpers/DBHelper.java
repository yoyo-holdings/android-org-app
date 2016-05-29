package com.josephimari.myapplication.helpers;

import com.josephimari.myapplication.database.MemoItem;
import com.josephimari.myapplication.database.MemoItem_Table;
import com.raizlabs.android.dbflow.annotation.Collate;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.List;

/**
 * Created by Joseph on 5/30/2016.
 */
public class DBHelper {

  private static final String TAG = DBHelper.class.getSimpleName();

  public static void purge() {
    Delete.tables(MemoItem.class);
  }

  public static void clearMemoItems() {
    new Delete().from(MemoItem.class).query();
  }

  public static List<MemoItem> getAllMemoItemsFromDb() {
    return new Select().from(MemoItem.class).queryList();
  }

  public static List<MemoItem> getNotesFromDb() {
    return new Select().from(MemoItem.class)
        .where(Condition.column(MemoItem_Table.type.getNameAlias()).eq(MemoItem.TYPE_NOTE))
        .orderBy(OrderBy.fromNameAlias(MemoItem_Table.lastModified.getNameAlias())
            .descending()
            .collate(Collate.NOCASE))
        .queryList();
  }

  public static List<MemoItem> getTodosFromDb() {
    return new Select().from(MemoItem.class)
        .where(Condition.column(MemoItem_Table.type.getNameAlias()).eq(MemoItem.TYPE_TODO))
        .orderBy(OrderBy.fromNameAlias(MemoItem_Table.lastModified.getNameAlias())
            .descending()
            .collate(Collate.NOCASE))
        .queryList();
  }

  public static MemoItem getMemoItemById(int memoId) {
    List<MemoItem> memoItems = getAllMemoItemsFromDb();
    for (MemoItem memoItem : memoItems) {
      if (memoId == (memoItem.memoId)) {
        return memoItem;
      }
    }
    return null;
  }

  public static void deleteMemoFromMemoItem(int memoId) {
    new Delete().from(MemoItem.class)
        .where(Condition.column(MemoItem_Table.memoId.getNameAlias()).eq(memoId))
        .query();
  }
}
