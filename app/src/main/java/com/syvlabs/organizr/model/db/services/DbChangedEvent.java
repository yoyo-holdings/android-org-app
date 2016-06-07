package com.syvlabs.organizr.model.db.services;

import com.syvlabs.organizr.model.db.objects.Entry;

public class DbChangedEvent {
    public static final int INSERTED = 0;
    public static final int DELETED = 1;
    public static final int EDITED = 2;
    public static final int DELETE_MULTIPLE = 3;

    public int event;
    public Entry entry;

    public DbChangedEvent(int event, Entry entry) {
        this.event = event;
        this.entry = entry;
    }
}
