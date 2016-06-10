package me.dlet.androidorgapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dlet.androidorgapp.R;
import me.dlet.androidorgapp.activites.NoteTakingActivity;
import me.dlet.androidorgapp.activites.TodoEntryActivity;
import me.dlet.androidorgapp.bus.Bus;
import me.dlet.androidorgapp.bus.DeleteEvent;
import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Note;
import me.dlet.androidorgapp.models.Todo;
import me.dlet.androidorgapp.utils.DateTimeUtils;

/**
 * Created by darwinlouistoledo on 6/6/16.
 * Email: darwin.louis@ymail.com
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewhHolder>{
    private Context context;
    private List<Note> notes;


    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public NotesViewhHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        NotesViewhHolder viewhHolder = new NotesViewhHolder(context, v);
        return viewhHolder;
    }

    @Override
    public void onBindViewHolder(NotesViewhHolder holder, int position) {
        Note note = notes.get(position);
        holder.setNote(note);
        holder.setPos(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NotesViewhHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvText) TextView tvText;
        @BindView(R.id.tvViewMore) TextView tvViewMore;

        private Context context;
        private int pos;
        private Note note;

        public NotesViewhHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Bus.getInstance().register(this);
            this.context=context;
        }

        private void showPopUpMenu(View v){
            PopupMenu popup = new PopupMenu(this.context, v);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.action_note_menu);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.update:
                    editNote();
                    break;
                case R.id.delete:
                    deleteNote();
                    break;
                case R.id.convert:
                    convertNoteToTODO();
            }
            return true;
        }

        @OnClick(R.id.btnMoreAction)
        public void onPopUpMenu(View v){
            showPopUpMenu(v);
        }

        private void deleteNote(){
            DaoHelper.getInstance().getNotesDao().delete(note.getId());
            Bus.getInstance().post(new DeleteEvent<>(note));
        }

        private void editNote(){
            NoteTakingActivity.startActivity(context,
                    NoteTakingActivity.TYPE_UPDATE,note,pos);
        }

        private void convertNoteToTODO(){
            Todo todo = new Todo(note.getTitle().concat("\n\n").concat(note.getText()),
                    false, System.currentTimeMillis());
            TodoEntryActivity.startActivity(context,
                    TodoEntryActivity.TYPE_CONVERT,todo,pos);
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public Note getNote() {
            return note;
        }

        public void setNote(Note note) {
            this.note = note;
            tvTitle.setText(note.getTitle());
            tvText.setText(note.getText());
            tvTime.setText(DateTimeUtils.format(note.getLastUpdated()));
        }
    }
}
