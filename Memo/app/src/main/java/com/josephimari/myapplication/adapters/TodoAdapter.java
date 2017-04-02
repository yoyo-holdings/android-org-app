package com.josephimari.myapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.josephimari.myapplication.R;
import com.josephimari.myapplication.database.MemoItem;
import com.josephimari.myapplication.utils.RecyclerUtil;
import java.util.List;

/**
 * Created by Joseph on 5/30/2016.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

  Context context;
  List<MemoItem> notes;

  RecyclerUtil.TagClickListener overflowTagClickListener;
  RecyclerUtil.TagClickListener itemTagClickListener;
  RecyclerUtil.TagClickListener checkboxClickListener;

  public TodoAdapter(Context context, List<MemoItem> notes) {
    this.context = context;
    this.notes = notes;
  }

  public void setData(List<MemoItem> notes) {
    this.notes = notes;
  }

  public void setOnItemTagClickedListener(RecyclerUtil.TagClickListener itemTagClickListener) {
    this.itemTagClickListener = itemTagClickListener;
  }

  public void setOnCheckboxClickListener(RecyclerUtil.TagClickListener checkboxClickListener) {
    this.checkboxClickListener = checkboxClickListener;
  }

  public void setOnOverflowTagClickedListener(
      RecyclerUtil.TagClickListener overflowTagClickListener) {
    this.overflowTagClickListener = overflowTagClickListener;
  }

  public MemoItem getItem(int position) {
    return notes.get(position);
  }

  @Override public TodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false));
  }

  @Override public void onBindViewHolder(TodoAdapter.ViewHolder holder, int position) {
    MemoItem note = notes.get(position);
    holder.checkBox.setChecked(note.taskCompleted);
    holder.title.setVisibility(View.GONE);

    String msg = note.message.replaceFirst("^[ \n\r]*", "");
    if (msg.isEmpty()) {
      holder.message.setText("No Text");
      holder.message.setTextColor(Color.GRAY);
    } else {
      holder.message.setText(msg);
      holder.message.setTextColor(Color.BLACK);
    }

  }

  @Override public int getItemCount() {
    return notes.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    CheckBox checkBox;
    TextView title;
    TextView message;
    ImageView overflow;

    public ViewHolder(View v) {
      super(v);
      checkBox = (CheckBox) v.findViewById(R.id.checkbox);
      title = (TextView) v.findViewById(R.id.title);
      message = (TextView) v.findViewById(R.id.message);
      overflow = (ImageView) v.findViewById(R.id.overflow);

      overflow.setOnClickListener(this);
      v.setOnClickListener(this);
      checkBox.setOnCheckedChangeListener(this);
    }

    @Override public void onClick(View view) {
      switch (view.getId()) {
        case R.id.overflow:
          overflowTagClickListener.onItemClick("" + getItem(getAdapterPosition()).memoId, view);
          break;
        default:
          itemTagClickListener.onItemClick("" + getItem(getAdapterPosition()).memoId, view);
          break;
      }
    }

    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      checkboxClickListener.onItemClick("" + getItem(getAdapterPosition()).memoId, buttonView);
    }
  }
}
