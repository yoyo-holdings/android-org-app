package me.dlet.androidorgapp.tasks;

import android.os.AsyncTask;

import java.util.Collections;
import java.util.List;

import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Note;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class LoadNotesTask extends AsyncTask<Void, Void, List<Note>>{
    private OnLoadListener<Note> onLoadListener;

    public LoadNotesTask(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    protected List<Note> doInBackground(Void... params) {
        List<Note> notes = DaoHelper.getInstance().getNotesDao().getAll();
        Collections.sort(notes);
        return notes;
    }

    @Override
    protected void onPostExecute(List<Note> notes) {
        super.onPostExecute(notes);
        onLoadListener.onFinish(notes);
    }
}
