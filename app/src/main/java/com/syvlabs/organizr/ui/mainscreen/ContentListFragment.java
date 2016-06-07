package com.syvlabs.organizr.ui.mainscreen;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.syvlabs.organizr.R;
import com.syvlabs.organizr.model.EntryType;
import com.syvlabs.organizr.model.db.helpers.EntriesDbHelper;
import com.syvlabs.organizr.model.db.objects.Entry;
import com.syvlabs.organizr.model.db.services.DbChangedEvent;
import com.syvlabs.organizr.model.db.services.DbService;
import com.syvlabs.organizr.ui.mainscreen.recyclerview.NoteViewHolder;
import com.syvlabs.organizr.ui.mainscreen.recyclerview.NotesAdapter;
import com.syvlabs.organizr.ui.mainscreen.recyclerview.TodoViewHolder;
import com.syvlabs.organizr.ui.mainscreen.recyclerview.TodosAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentListFragment extends Fragment {

    public EntryType fragmentType;

    @Bind(R.id.rv)
    RecyclerView rv;

    @Bind(R.id.text_noNotes)
    TextView textNoNotes;

    private OnContentListInteractionListener mListener;
    private List<Entry> entries;

    private int rvPosClicked;
    private long entryIdClicked;

    public ContentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content_list, container, false);
        ButterKnife.bind(this, v);
        fragmentType = EntryType.getEntryTypeFromIndex(getArguments().getInt(MainPagerAdapter.FRAGMENT_TYPE));
        setupRV();
        return v;
    }

    private void setupRV() {
        entries = new ArrayList<>();
        entries.addAll(EntriesDbHelper.retrieveEntriesForType(getContext(), fragmentType));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(llm);
        if (fragmentType.index == EntryType.NOTE.index) {
            NotesAdapter adapter = new NotesAdapter(entries, new NoteViewHolder.ClickListener() {
                @Override
                public void onClick(Entry data, int pos) {
                    onEntryClick(data, pos);
                }
            });
            rv.setAdapter(adapter);
        } else if (fragmentType.index == EntryType.TODO.index) {
            TodosAdapter adapter = new TodosAdapter(entries, new TodoViewHolder.ClickListener() {
                @Override
                public void onClick(Entry data, int pos) {
                    onEntryClick(data, pos);
                }
                @Override
                public void onCheckChanged(Entry data, boolean isChecked) {
                    onTodoCheckboxClicked(data, isChecked);
                }
            });
            rv.setAdapter(adapter);
        }
        refreshEmptyIndicator();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContentListInteractionListener) {
            mListener = (OnContentListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContentListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DbService.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        DbService.getBus().unregister(this);
    }

    @Subscribe
    public void onReceiveDbChange(final DbChangedEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshData(event);
            }
        });
    }

    private void refreshData(DbChangedEvent event) {
        if (entries == null)
            return;
        entries.clear();
        entries.addAll(EntriesDbHelper.retrieveEntriesForType(getContext(), fragmentType));
        if (rv == null || rv.getAdapter() == null) {
            Log.e("organizr", "Error: rv or its adapter is null");
            return;
        }
        if (event.event == DbChangedEvent.INSERTED) {
            if (event.entry.getType() == fragmentType.index) {
                rv.getAdapter().notifyItemInserted(0);
                rv.getLayoutManager().scrollToPosition(0);
            }
        } else if (event.event == DbChangedEvent.DELETED) {
            if (event.entry.getId() == entryIdClicked)
                rv.getAdapter().notifyItemRemoved(rvPosClicked);
        } else if (event.event == DbChangedEvent.EDITED) {
            rv.getAdapter().notifyDataSetChanged();
        } else {
            rv.getAdapter().notifyDataSetChanged();
        }
        refreshEmptyIndicator();
    }

    private void refreshEmptyIndicator() {
        textNoNotes.setVisibility(entries.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onEntryClick(Entry entry, int pos) {
        rvPosClicked = pos;
        entryIdClicked = entry.getId();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (EntriesDbHelper.retrieveEntry(getContext(), entry.getId()) == null)
            return;
        EntryFragment fragment = EntryFragment.newOpenEntryInstance(entry.getId());
        fragment.show(fm, "fragment_entry");
    }

    private void onTodoCheckboxClicked(final Entry data, final boolean isChecked) {
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                EntriesDbHelper.setCheckedStatus(getContext(), data, isChecked);
            }
        };
        h.postDelayed(r, 300);
    }

    public interface OnContentListInteractionListener {
    }

    public void clearAllDone() {
        EntriesDbHelper.deleteDone(getContext());
    }
}
