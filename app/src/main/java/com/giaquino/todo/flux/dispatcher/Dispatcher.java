package com.giaquino.todo.flux.dispatcher;

import android.support.annotation.NonNull;
import com.giaquino.todo.flux.action.Action;
import com.giaquino.todo.flux.store.Store;
import java.util.List;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class Dispatcher {

    private List<Store> stores;

    public Dispatcher(@NonNull List<Store> stores) {
        this.stores = stores;
    }

    public void dispatch(@NonNull Action action) {
        for (Store store : stores) {
            store.dispatchAction(action);
        }
    }
}
