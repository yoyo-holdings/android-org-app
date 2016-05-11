package yoyo_holdings.com.androidorgapp.features.notes;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yoyo_holdings.com.androidorgapp.ApplicationModule;
import yoyo_holdings.com.androidorgapp.R;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;
import yoyo_holdings.com.androidorgapp.data.source.DaggerEntryRepositoryComponent;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepositoryComponent;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepositoryModule;

/**
 * Display a list of {@link yoyo_holdings.com.androidorgapp.data.model.Entry}s.
 */
public class NotesFragment extends Fragment implements NotesContract.View {

    @Bind(R.id.item_list)
    ListView itemList;
    private ResultsAdapter mListAdapter;
    private NotesContract.UserActionsListener mActionsListener;
    private EntryRepositoryComponent entryRepositoryComponent;

    public NotesFragment() {
        // Requires empty public constructor
    }

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        entryRepositoryComponent = DaggerEntryRepositoryComponent.builder()
                .entryRepositoryModule(new EntryRepositoryModule(getActivity()))
                .build();
        entryRepositoryComponent.inject(this);

        mActionsListener = DaggerNotesFragmentComponent.builder()
                .notesPresenterModule(new NotesPresenterModule(this))
                .entryRepositoryComponent(entryRepositoryComponent)
                .build().getPresenter();
        mActionsListener.loadEntries(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ArrayList<Entry> arrayList = new ArrayList<Entry>();
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
        arrayList.add(new EntryEntity());
//        mListAdapter = new ResultsAdapter(new ArrayList<Entry>(0), mItemListener);
        mListAdapter = new ResultsAdapter(arrayList, mItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, view);
        itemList.setAdapter(mListAdapter);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showEntries(List<Entry> entries) {

    }

    @Override
    public void showEntryDetailsUi(Entry entry) {
// in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
//        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
//        startActivity(intent);
    }

    @Override
    public void showLoadingEntryError() {

    }

    @Override
    public void showNewNoteUi() {

    }

    @Override
    public void showEditNoteUi(Entry entry) {

    }

    public void loadEntries() {
        mActionsListener.loadEntries(false);
    }

    public static class ResultsAdapter extends BaseAdapter {

        private List<Entry> items;

        private ResultItemListener mItemListener;

        public ResultsAdapter(List<Entry> items, ResultItemListener itemListener) {
            setList(items);
            mItemListener = itemListener;
        }

        public void replaceData(List<Entry> items) {
            setList(items);
            notifyDataSetChanged();
        }

        private void setList(List<Entry> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Entry getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.entry_item, viewGroup, false);
                viewHolder = new ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            final Entry entry = getItem(i);
            Context context = rowView.getContext();

//            viewHolder.productName.setText(product.getName());
//            viewHolder.overview.setText(product.getDescription());
//            String image = product.getImage();
//            Glide.with(context).load(image).into(viewHolder.productImage);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClicked(entry);
                }
            });

            return rowView;
        }

        public static class ViewHolder {
//            @Bind(R.id.product_image)
//            ImageView productImage;
//            @Bind(R.id.product_name)
//            TextView productName;
//            @Bind(R.id.product_description_label)
//            TextView overview;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    ResultItemListener mItemListener = new ResultItemListener() {
        @Override
        public void onItemClicked(Entry entry) {
            mActionsListener.openEntryDetails(entry);
        }
    };

    public interface ResultItemListener {

        void onItemClicked(Entry product);

    }
}
