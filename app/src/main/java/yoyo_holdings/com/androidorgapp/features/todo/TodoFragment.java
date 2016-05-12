package yoyo_holdings.com.androidorgapp.features.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;
import yoyo_holdings.com.androidorgapp.data.source.DaggerEntryRepositoryComponent;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepositoryComponent;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepositoryModule;
import yoyo_holdings.com.androidorgapp.features.createupdate.UpsertActivity;

/**
 * Display a list of {@link Entry}s.
 */
public class TodoFragment extends Fragment implements TodoContract.View {

    @Bind(R.id.item_list)
    ListView itemList;
    private ResultsAdapter mListAdapter;
    private TodoContract.UserActionsListener mActionsListener;
    private EntryRepositoryComponent entryRepositoryComponent;
    private ExecutorService executor;

    public TodoFragment() {
        // Requires empty public constructor
    }

    public static TodoFragment newInstance() {
        return new TodoFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        entryRepositoryComponent = DaggerEntryRepositoryComponent.builder()
                .entryRepositoryModule(new EntryRepositoryModule(getActivity()))
                .build();
        entryRepositoryComponent.inject(this);

        mActionsListener = DaggerTodoFragmentComponent.builder()
                .todoPresenterModule(new TodoPresenterModule(this))
                .entryRepositoryComponent(entryRepositoryComponent)
                .build().getPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_frag, container, false);
        ButterKnife.bind(this, view);
        executor = Executors.newSingleThreadExecutor();
        mListAdapter = new ResultsAdapter(getActivity(), mItemListener);
        mListAdapter.setExecutor(executor);
        itemList.setAdapter(mListAdapter);
        mListAdapter.queryAsync();

        return view;
    }

    @Override
    public void onResume() {
        mListAdapter.queryAsync();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
        mListAdapter.close();
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showSaveEntryDone() {

    }

    @Override
    public void showEditTodoUi(EntryEntity entry) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), UpsertActivity.class);
        intent.putExtra(UpsertActivity.ENTRY_EXTRA, entry);
        startActivity(intent);
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    ResultItemListener mItemListener = new ResultItemListener() {
        @Override
        public void onItemClicked(EntryEntity entry) {
            mActionsListener.editEntry(entry);
        }

        @Override
        public void onSetDone(EntryEntity entry) {
            mActionsListener.updateEntry(entry);
        }
    };

    public void update() {
        mListAdapter.queryAsync();
    }

    public interface ResultItemListener {
        void onItemClicked(EntryEntity entry);
        void onSetDone(EntryEntity entry);
    }
}
