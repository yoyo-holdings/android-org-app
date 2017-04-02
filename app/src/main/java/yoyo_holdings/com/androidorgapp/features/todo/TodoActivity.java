package yoyo_holdings.com.androidorgapp.features.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.util.ActivityUtils;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TodoFragment fragment = TodoFragment.newInstance();

        if (null == savedInstanceState) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragmentContainer);
        }
    }
}
