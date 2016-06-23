package me.dlet.androidorgapp.tasks;

import android.os.AsyncTask;

import java.util.List;

import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Todo;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class LoadTodosTask extends AsyncTask<Void, Void, List<Todo>>{
    private OnLoadListener<Todo> onLoadListener;

    public LoadTodosTask(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    protected List<Todo> doInBackground(Void... params) {
        List<Todo> todos = DaoHelper.getInstance().getTodosDao().getAll();
        return todos;
    }

    @Override
    protected void onPostExecute(List<Todo> todos) {
        super.onPostExecute(todos);
        onLoadListener.onFinish(todos);
    }
}
