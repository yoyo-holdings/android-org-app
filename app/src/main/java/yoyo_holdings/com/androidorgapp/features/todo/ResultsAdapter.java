package yoyo_holdings.com.androidorgapp.features.todo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
    @Bind(R.id.todo_entry)
    CheckBox todoEntry;
    @Bind(R.id.knife)
    KnifeText knife;
    private List<Entry> items;
    private TodoFragment.ResultItemListener mItemListener;

    public ResultsAdapter(Context context, TodoFragment.ResultItemListener itemListener) {
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
        return ((BaseApp) mContext.getApplicationContext()).getDataSource().select(EntryEntity.class)
                .where(EntryEntity.TODO.eq(true))
                .get();
    }

    @Override
    public View getView(final EntryEntity entry, View convertView, ViewGroup viewGroup) {
        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.todo_item, viewGroup, false);
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
                viewHolder.todoEntry.setText(entry.getTitle());
            }
            viewHolder.todoEntry.setChecked(entry.isDone());
            viewHolder.todoEntry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    entry.setDone(isChecked);
                    mItemListener.onSetDone(entry);
                }
            });
        }

        viewHolder.hitArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onItemClicked(entry);
            }
        });

        viewHolder.hitArea.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemListener.showContextDialog(entry);
                return true;
            }
        });

        return rowView;
    }

    public static class ViewHolder {
        @Bind(R.id.knife)
        KnifeText knife;
        @Bind(R.id.hit_area)
        View hitArea;
        @Bind(R.id.todo_entry)
        CheckBox todoEntry;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}