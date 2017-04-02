package com.josephimari.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.josephimari.myapplication.database.MemoItem;
import com.josephimari.myapplication.helpers.DBHelper;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joseph on 5/30/2016.
 */
public class DisplayMemoActivity extends AppCompatActivity {

  private static final String TAG = NewMemoActivity.class.getSimpleName();
  public static final String EXTRA_MEMO_ID = "extra_memo_id";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.title) TextView title;
  @Bind(R.id.message) TextView message;
  @Bind(R.id.last_modified) TextView lastModified;

  private MemoItem memoItem;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_memo);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    if (getIntent() == null || getIntent().getStringExtra(EXTRA_MEMO_ID) == null) {
      Toast.makeText(DisplayMemoActivity.this, "No Memo ID was passed", Toast.LENGTH_SHORT).show();
      finish();
    } else {
      memoItem =
          DBHelper.getMemoItemById(Integer.parseInt(getIntent().getStringExtra(EXTRA_MEMO_ID)));
      if (memoItem == null) {
        Toast.makeText(DisplayMemoActivity.this, "Memo not found in DB", Toast.LENGTH_SHORT).show();
        finish();
      } else {
        title.setText(memoItem.title);
        message.setText(memoItem.message);
        lastModified.setText("Last modified: " + getDate(memoItem.lastModified,"MMM dd, yyyy h:mm a"));
        title.setVisibility(memoItem.type == MemoItem.TYPE_TODO ? View.GONE : View.VISIBLE);
      }
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_display_memo, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_delete:

        AlertDialog.Builder builder =
            new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(null);
        builder.setMessage("This memo will be deleted.");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            MemoItem m = new MemoItem();
            m.setMemoId(memoItem.memoId);
            m.delete();

            DisplayMemoActivity.this.finish();
          }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
        builder.show();

        return true;

      case R.id.action_edit:
        launchEditMemoActivity();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @OnClick(R.id.title) void onTitleClick(View view) {
    launchEditMemoActivity();
  }

  @OnClick(R.id.message) void onMessageClick(View view) {
    launchEditMemoActivity();
  }

  private void launchEditMemoActivity() {
    Intent intent = new Intent(DisplayMemoActivity.this, EditMemoActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(EditMemoActivity.EXTRA_MEMO_ID, "" + memoItem.memoId);
    startActivity(intent);
    DisplayMemoActivity.this.finish();
  }

  private String getDate(long milliSeconds, String dateFormat)
  {
    // Create a DateFormatter object for displaying date in specified format.
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliSeconds);
    return formatter.format(calendar.getTime());
  }

}
