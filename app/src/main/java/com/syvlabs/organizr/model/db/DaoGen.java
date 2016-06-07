package com.syvlabs.organizr.model.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGen {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.syvlabs.organizr.model.db.objects");
        schema.enableKeepSectionsByDefault();

        //Add entry entity, both for notes and to-do entries
        Entity entry = schema.addEntity("Entry");
        entry.addIdProperty();
        entry.addStringProperty("title");
        entry.addStringProperty("content");
        entry.addBooleanProperty("done");
        entry.addIntProperty("type");   //Refer to EntryType Enum
        entry.addIntProperty("color");

        //Remember to change schema number above when editing this part!

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }
}
