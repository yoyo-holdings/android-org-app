package com.syvlabs.organizr.model.db.helpers;

import android.content.Context;
import android.util.Log;

import com.syvlabs.organizr.model.Colors;
import com.syvlabs.organizr.model.EntryType;
import com.syvlabs.organizr.model.db.objects.Entry;
import com.syvlabs.organizr.model.db.objects.EntryDao;
import com.syvlabs.organizr.model.db.services.DbChangedEvent;
import com.syvlabs.organizr.model.db.services.DbService;

import java.util.List;

public class EntriesDbHelper {

    public static List<Entry> retrieveEntriesForType(Context context, EntryType type) {
        switch(type) {
            case NOTE:
                return retrieveAllNotes(context);
            case TODO:
                return retrieveAllTodos(context);
            default:
                return null;
        }
    }

    public static List<Entry> retrieveAllNotes(Context context) {
        EntryDao dao = DaoHelper.getInstance(context).getDaoSession().getEntryDao();
        List<Entry> result = dao.queryBuilder()
                .where(EntryDao.Properties.Type.eq(EntryType.NOTE.index))
                .orderDesc(EntryDao.Properties.Id)
                .list();
        return result;
    }

    public static List<Entry> retrieveAllTodos(Context context) {
        EntryDao dao = DaoHelper.getInstance(context).getDaoSession().getEntryDao();
        List<Entry> result = dao.queryBuilder()
                .where(EntryDao.Properties.Type.eq(EntryType.TODO.index))
                .orderDesc(EntryDao.Properties.Id)
                .list();
        return result;
    }

    public static Entry createEntry(Context context, EntryType type) {
        return new Entry(null, "", "", false, type.index, Colors.getDefaultNoteColor(context));
    }

    public static void insertEntry(Context context, Entry entry) {
        EntryDao dao = DaoHelper.getInstance(context).getDaoSession().getEntryDao();
        int eventType = entry.isNew() ? DbChangedEvent.INSERTED : DbChangedEvent.EDITED;
        dao.insertOrReplace(entry);
        DbService.getBus().post(new DbChangedEvent(eventType, entry));
    }

    public static void deleteEntry(Context context, Entry entry) {
        EntryDao dao = DaoHelper.getInstance(context).getDaoSession().getEntryDao();
        dao.delete(entry);
        DbService.getBus().post(new DbChangedEvent(DbChangedEvent.DELETED, entry));
    }

    public static void deleteDone(Context context) {
        EntryDao dao = DaoHelper.getInstance(context).getDaoSession().getEntryDao();
        dao.queryBuilder()
                .where(EntryDao.Properties.Type.eq(EntryType.TODO.index),
                        EntryDao.Properties.Done.eq(true))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        DbService.getBus().post(new DbChangedEvent(DbChangedEvent.DELETE_MULTIPLE, null));
    }

    public static Entry retrieveEntry(Context context, long key) {
        EntryDao dao = DaoHelper.getInstance(context).getDaoSession().getEntryDao();
        return dao.loadByRowId(key);
    }

    public static void setCheckedStatus(Context context, Entry entry, boolean isChecked) {
        if(retrieveEntry(context, entry.getId()) == null)
            return;
        entry.setDone(isChecked);
        insertEntry(context, entry);
    }
}
