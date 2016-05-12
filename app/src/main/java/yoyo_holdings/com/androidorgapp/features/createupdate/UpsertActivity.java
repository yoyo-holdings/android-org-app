package yoyo_holdings.com.androidorgapp.features.createupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import icepick.Icepick;
import icepick.State;
import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;
import yoyo_holdings.com.androidorgapp.util.ActivityUtils;

public class UpsertActivity extends AppCompatActivity {

    public static final String ENTRY_EXTRA = "ENTRY_EXTRA";

    @State
    EntryEntity entryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.notes_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null == savedInstanceState) {
            entryItem = getIntent().getParcelableExtra(ENTRY_EXTRA);
        }
        getIntent().getParcelableExtra(ENTRY_EXTRA);

        if (entryItem != null) {
            final UpsertNotesFragment fragment = UpsertNotesFragment.newInstance(entryItem);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragmentContainer);
        }
    }
}
