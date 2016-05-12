package yoyo_holdings.com.androidorgapp.features.createupdate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.mthli.knife.KnifeText;
import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;
import yoyo_holdings.com.androidorgapp.data.source.DaggerEntryRepositoryComponent;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepositoryComponent;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepositoryModule;

/**
 * Display a list of {@link Entry}s.
 */
public class UpsertNotesFragment extends Fragment implements UpsertNotesContract.View {

    private static final String ARG_ENTRY = "ENTRY";
    @Bind(R.id.bold)
    ImageButton bold;
    @Bind(R.id.italic)
    ImageButton italic;
    @Bind(R.id.underline)
    ImageButton underline;
    @Bind(R.id.strikethrough)
    ImageButton strikethrough;
    @Bind(R.id.bullet)
    ImageButton bullet;
    @Bind(R.id.quote)
    ImageButton quote;
    @Bind(R.id.link)
    ImageButton link;
    @Bind(R.id.clear)
    ImageButton clear;
    @Bind(R.id.tools)
    HorizontalScrollView tools;
    @Bind(R.id.knife)
    KnifeText knife;
    @Bind(R.id.title)
    EditText title;
    @Bind(R.id.save)
    CircularProgressButton save;

    private UpsertNotesContract.UserActionsListener mActionsListener;
    private EntryRepositoryComponent entryRepositoryComponent;

    private EntryEntity entry;
    private boolean isEditMode;

    public UpsertNotesFragment() {
        // Requires empty public constructor
    }

    public static UpsertNotesFragment newInstance(EntryEntity entry) {
        UpsertNotesFragment fragment = new UpsertNotesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ENTRY, entry);
        fragment.setArguments(args);
        return fragment;
    }

    public static UpsertNotesFragment newInstance() {
        return new UpsertNotesFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        entryRepositoryComponent = DaggerEntryRepositoryComponent.builder()
                .entryRepositoryModule(new EntryRepositoryModule(getActivity()))
                .build();
        entryRepositoryComponent.inject(this);

        mActionsListener = DaggerUpsertNotesFragmentComponent.builder()
                .upsertNotesPresenterModule(new UpsertNotesPresenterModule(this))
                .entryRepositoryComponent(entryRepositoryComponent)
                .build().getPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            isEditMode = true;
            entry = getArguments().getParcelable(ARG_ENTRY);
        } else {
            entry = new EntryEntity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upsert_note_frag, container, false);
        ButterKnife.bind(this, view);

        if (entry != null) {
            if (!TextUtils.isEmpty(entry.getTitle())) {
                title.setText(entry.getTitle());
            }
            if (!TextUtils.isEmpty(entry.getNote())) {
                knife.fromHtml(entry.getNote());
            }
        }
        return view;
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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                title.setText("");
                knife.setText("");
            }
        });
    }

    @Override
    public void showSaveEntryError() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.bold, R.id.italic, R.id.underline, R.id.strikethrough, R.id.bullet, R.id.quote, R.id.link, R.id.clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bold:
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));
                break;
            case R.id.italic:
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
                break;
            case R.id.underline:
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
                break;
            case R.id.strikethrough:
                knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
                break;
            case R.id.bullet:
                knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET));
                break;
            case R.id.quote:
                knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE));
                break;
            case R.id.link:
                break;
            case R.id.clear:
                knife.clearFormats();
                break;
        }
    }

    @OnClick(R.id.save)
    public void onClick() {
        entry.setTitle(title.getText().toString());
        entry.setNote(knife.toHtml());

        if (!isEditMode) {
            entry.setId(0);
        }

        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogFragment();
        bottomSheetDialogFragment.setListener(entry, mActionsListener);
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}
