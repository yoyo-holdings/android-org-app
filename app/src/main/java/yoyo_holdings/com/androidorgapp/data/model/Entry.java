package yoyo_holdings.com.androidorgapp.data.model;

import android.os.Parcelable;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

/**
 * Created by aconcepcion on 5/12/16.
 */
@Entity
public interface Entry extends Parcelable, Persistable {
    @Key @Generated
    int getId();
    void setId(int id);

    String getTitle();
    void setTitle(String title);

    String getNote();
    void setNote(String note);

    boolean isTodo();
    void setTodo(boolean toDo);

    boolean isDone();
    void setDone(boolean done);
}