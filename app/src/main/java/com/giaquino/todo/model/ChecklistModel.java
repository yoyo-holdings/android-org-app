package com.giaquino.todo.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.giaquino.todo.model.db.Database;
import com.giaquino.todo.model.entity.Checklist;
import com.squareup.sqlbrite.QueryObservable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
public class ChecklistModel {

    private Database database;
    private QueryObservable checklists;

    public ChecklistModel(@NonNull Database database) {
        this.database = database;
        this.checklists = database.query(Checklist.TABLE_NAME, Checklist.SELECT_ALL, null);
    }

    public Observable<List<Checklist>> checklists() {
        return checklists.debounce(500, TimeUnit.MILLISECONDS).map(query -> {
            Cursor cursor = query.run();
            if (cursor == null || cursor.getCount() == 0) return Collections.emptyList();
            List<Checklist> results = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                results.add(Checklist.MAPPER.map(cursor));
            }
            return results;
        });
    }

    public void create(@NonNull String entry) {
        database.insert(Checklist.TABLE_NAME, Checklist.FACTORY.marshal()
            .entry(entry)
            .checked(false)
            .datetime(System.currentTimeMillis())
            .asContentValues());
    }

    public void update(long id, @NonNull String entry, boolean checked) {
        database.update(Checklist.TABLE_NAME, Checklist.FACTORY.marshal()
            ._id(id)
            .entry(entry)
            .checked(checked)
            .asContentValues(),
            Checklist._ID + "=?", new String[] { String.valueOf(id) });
    }

    public void delete(long id) {
        database.delete(Checklist.TABLE_NAME, Checklist._ID + "=?",
            new String[] { String.valueOf(id) });
    }
}
