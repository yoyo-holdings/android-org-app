package yoyo_holdings.com.androidorgapp.features.notes;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.mthli.knife.KnifeText;
import io.requery.android.QueryAdapter;
import io.requery.query.Result;
import yoyo_holdings.com.androidorgapp.BaseApp;
import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;

/**
 * Created by andrewconcepcion on 12/05/2016.
 */
public class ResultsAdapter extends QueryAdapter<EntryEntity> {

    private final Context mContext;
    private List<Entry> items;
    private NotesFragment.ResultItemListener mItemListener;

    public ResultsAdapter(Context context, NotesFragment.ResultItemListener itemListener) {
        super(EntryEntity.$TYPE);
        setList(items);
        mItemListener = itemListener;
        mContext = context;
    }

    public void replaceData(List<Entry> items) {
        setList(items);
        notifyDataSetChanged();
    }

    private void setList(List<Entry> items) {
        this.items = items;
    }

    @Override
    public Result<EntryEntity> performQuery() {
        return ((BaseApp)mContext.getApplicationContext()).getDataSource().select(EntryEntity.class)
                .orderBy(EntryEntity.ID.desc())
                .get();
    }

    @Override
    public View getView(final EntryEntity entry, View convertView, ViewGroup viewGroup) {
        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.entry_item, viewGroup, false);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if (entry != null) {
            if (!TextUtils.isEmpty(entry.getNote())) {
                viewHolder.knife.fromHtml(entry.getNote());
            }
            if (!TextUtils.isEmpty(entry.getTitle())) {
                viewHolder.title.setText(entry.getTitle());
            }
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onItemClicked(entry);
            }
        });

        return rowView;
    }

    public static class ViewHolder {
        @Bind(R.id.knife)
        KnifeText knife;
        @Bind(R.id.title)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}