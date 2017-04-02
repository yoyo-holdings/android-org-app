package com.josephimari.myapplication.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.josephimari.myapplication.DisplayMemoActivity;
import com.josephimari.myapplication.MainActivity;
import com.josephimari.myapplication.R;
import com.josephimari.myapplication.adapters.MainPagerAdapter;
import com.josephimari.myapplication.adapters.NoteAdapter;
import com.josephimari.myapplication.database.MemoItem;
import com.josephimari.myapplication.helpers.DBHelper;
import com.josephimari.myapplication.utils.RecyclerUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Joseph on 5/30/2016.
 */
public class NoteFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<List<MemoItem>>, MainPagerAdapter.Updateable {

  private static final String TAG = NoteFragment.class.getSimpleName();

  @Bind(R.id.recycler_view) RecyclerView recyclerView;
  @Bind(R.id.empty_view) TextView emptyView;

  private NoteAdapter adapter;
  private RecyclerView.LayoutManager layoutManager;

  public NoteFragment() {
  }

  public static NoteFragment newInstance() {
    NoteFragment fragment = new NoteFragment();
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_note, container, false);
    ButterKnife.bind(NoteFragment.this, rootView);
    return rootView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initList();
  }

  @Override public void onResume() {
    super.onResume();
    initLoader();
  }

  @Override public Loader<List<MemoItem>> onCreateLoader(int id, Bundle args) {
    return new AsyncTaskLoader<List<MemoItem>>(getActivity()) {

      @Override public List<MemoItem> loadInBackground() {
        return DBHelper.getNotesFromDb();
      }
    };
  }

  @Override public void onLoadFinished(Loader<List<MemoItem>> loader, List<MemoItem> data) {
    emptyView.setVisibility(data.size() > 0 ? View.GONE : View.VISIBLE);
    adapter.setData(data);
    adapter.notifyDataSetChanged();
  }

  @Override public void onLoaderReset(Loader<List<MemoItem>> loader) {
    adapter.setData(new ArrayList<MemoItem>(0));
  }

  private void initList() {
    recyclerView.setHasFixedSize(true);

    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);

    adapter = new NoteAdapter(getActivity(), new ArrayList<MemoItem>(0));
    adapter.setOnItemTagClickedListener(new RecyclerUtil.TagClickListener() {
      @Override public void onItemClick(String payload, View view) {

        Intent intent = new Intent(getActivity(), DisplayMemoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(DisplayMemoActivity.EXTRA_MEMO_ID, payload);
        startActivity(intent);
      }
    });
    adapter.setOnOverflowTagClickedListener(new RecyclerUtil.TagClickListener() {
      @Override public void onItemClick(String payload, View view) {

        final MemoItem m = DBHelper.getMemoItemById(Integer.parseInt(payload));

        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.menu_move_to_todo, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_move_to_todo:
                long timeNow = Calendar.getInstance().getTimeInMillis();
                m.type = MemoItem.TYPE_TODO;
                m.lastModified = timeNow;
                m.save();
                initLoader();
                break;
              case R.id.action_delete:
                AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle(null);
                builder.setMessage("This memo will be deleted.");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                    m.delete();
                    initLoader();
                  }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                  }
                });
                builder.show();
                break;
            }

            return true;
          }
        });
        popup.show();
      }
    });
    recyclerView.setAdapter(adapter);
  }

  private void initLoader() {
    getActivity().getSupportLoaderManager().initLoader(TAG.hashCode(), null, this).forceLoad();
  }

  @Override public void update() {
    initLoader();
  }
}
