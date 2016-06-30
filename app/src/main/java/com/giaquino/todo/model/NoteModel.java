package com.giaquino.todo.model;

import android.database.Cursor;
import com.giaquino.todo.model.db.Database;
import com.giaquino.todo.model.entity.Note;
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
public class NoteModel {

    private Database database;
    private QueryObservable notes;

    public NoteModel(Database database) {
        this.database = database;
        this.notes = database.query(Note.TABLE_NAME, Note.SELECT_ALL, null);
    }

    public Observable<List<Note>> notes() {
        return notes.debounce(500, TimeUnit.MILLISECONDS).map(query -> {
            Cursor cursor = query.run();
            if (cursor == null || cursor.getCount() == 0) return Collections.emptyList();
            List<Note> results = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                results.add(Note.MAPPER.map(cursor));
            }
            return results;
        });
    }

    public void create(String title, String text) {
        database.insert(Note.TABLE_NAME, Note.FACTORY.marshal()
            .title(title)
            .text(text)
            .datetime(System.currentTimeMillis())
            .asContentValues());
    }

    public void update(long id, String title, String text) {
        database.update(Note.TABLE_NAME, Note.FACTORY.marshal()
            ._id(id)
            .title(title)
            .text(text)
            .asContentValues(),
            Note._ID + "=?", new String[] { String.valueOf(id) });
    }

    public void delete(long id) {
        database.delete(Note.TABLE_NAME, Note._ID + "=?", new String[] { String.valueOf(id) });
    }
}
