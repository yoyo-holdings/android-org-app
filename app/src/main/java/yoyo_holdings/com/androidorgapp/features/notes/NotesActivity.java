package yoyo_holdings.com.androidorgapp.features.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.util.ActivityUtils;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final NotesFragment fragment = NotesFragment.newInstance();

        if (null == savedInstanceState) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragmentContainer);
        }
    }
}
