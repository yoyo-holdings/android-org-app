package com.josephimari.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.josephimari.myapplication.database.MemoItem;
import com.josephimari.myapplication.helpers.DBHelper;
import java.util.Calendar;

/**
 * Created by Joseph on 5/30/2016.
 */
public class EditMemoActivity extends AppCompatActivity {

  private static final String TAG = NewMemoActivity.class.getSimpleName();
  public static final String EXTRA_MEMO_ID = "extra_memo_id";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.title) EditText title;
  @Bind(R.id.message) EditText message;

  private MemoItem memoItem;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_memo);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    message.requestFocus();

    if (getIntent() == null || getIntent().getStringExtra(EXTRA_MEMO_ID) == null) {
      Toast.makeText(EditMemoActivity.this, "No Memo ID was passed", Toast.LENGTH_SHORT).show();
      finish();
    } else {
      memoItem =
          DBHelper.getMemoItemById(Integer.parseInt(getIntent().getStringExtra(EXTRA_MEMO_ID)));
      if (memoItem == null) {
        Toast.makeText(EditMemoActivity.this, "Memo not found in DB", Toast.LENGTH_SHORT).show();
        finish();
      } else {
        title.setText(memoItem.title);
        message.append(memoItem.message);
        title.setVisibility(memoItem.type == MemoItem.TYPE_TODO ? View.GONE : View.VISIBLE);
      }
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_edit_memo, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_save:
        saveMemo();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void saveMemo() {
    long timeNow = Calendar.getInstance().getTimeInMillis();
    MemoItem m =
        new MemoItem(title.getText().toString(), message.getText().toString(), memoItem.dateCreated,
            timeNow, memoItem.type, false);
    m.setMemoId(memoItem.memoId);
    m.save();

    finish();
  }

  @Override public void onBackPressed() {
    if (!memoItem.message.equals(message.getText().toString()) || !memoItem.title.equals(
        title.getText().toString())) {

      AlertDialog.Builder builder =
          new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
      builder.setTitle(null);
      builder.setMessage("Save your changes or discard them?");
      builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          saveMemo();
        }
      });
      builder.setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      });
      builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      });
      builder.show();
    } else {
      super.onBackPressed();
    }
  }
}
