package com.syvlabs.organizr.model;

import com.syvlabs.organizr.R;

public enum EntryType {
    NOTE("notes", 0, R.drawable.ic_book_white_24dp),
    TODO("to-do", 1, R.drawable.ic_format_list_bulleted_white_24dp);

    public String tabTitle;
    public int index;
    public int icon;

    EntryType(String tabTitle, int index, int icon) {
        this.tabTitle = tabTitle;
        this.index = index;
        this.icon = icon;
    }

    public static EntryType getEntryTypeFromIndex(int index) {
        for (EntryType type : values()) {
            if (index == type.index)
                return type;
        }
        return null;
    }
}
