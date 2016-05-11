package yoyo_holdings.com.androidorgapp.features.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fragment.loadEntries();
            }
        });

        if (null == savedInstanceState) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragmentContainer);
        }
    }
}
