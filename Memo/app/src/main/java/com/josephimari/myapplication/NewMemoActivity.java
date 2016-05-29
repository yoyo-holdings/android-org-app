package com.josephimari.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.josephimari.myapplication.adapters.ToolbarSpinnerAdapter;
import com.josephimari.myapplication.database.MemoItem;
import java.util.Calendar;

/**
 * Created by Joseph on 5/30/2016.
 */
public class NewMemoActivity extends AppCompatActivity {

  private static final String TAG = NewMemoActivity.class.getSimpleName();
  public static final String EXTRA_TYPE = "extra_type";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.title) EditText title;
  @Bind(R.id.message) EditText message;
  @Bind(R.id.spinner) Spinner spinner;

  private String[] arraySpinner = { "New Note ", "New Todo " };
  private int type;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_memo);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    message.requestFocus();
    initToolbarSpinner();
  }

  private void initToolbarSpinner() {
    ToolbarSpinnerAdapter spinnerAdapter =
        new ToolbarSpinnerAdapter(NewMemoActivity.this, arraySpinner);
    spinner.setAdapter(spinnerAdapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = position;
        switch (position) {
          case MemoItem.TYPE_NOTE:
            title.setVisibility(View.VISIBLE);
            title.animate();
            break;
          case MemoItem.TYPE_TODO:
            title.setText("");
            title.setVisibility(View.INVISIBLE);
            title.animate();
            break;
          default:
            break;
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    type = getIntent().getIntExtra(EXTRA_TYPE, MemoItem.TYPE_NOTE);
    spinner.setSelection(type);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_new_memo, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_save:
        if (!message.getText().toString().isEmpty()) {
          long timeNow = Calendar.getInstance().getTimeInMillis();
          new MemoItem(title.getText().toString(), message.getText().toString(), timeNow,
              timeNow, type, false).save();
        } else {
          Toast.makeText(NewMemoActivity.this, "No content to save. Memo discarded.",
              Toast.LENGTH_SHORT).show();
        }
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
