package com.syvlabs.organizr.model.db.services;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class DbService {
    private static Bus bus;

    public static Bus getBus() {
        if (bus == null)
            bus = new Bus(ThreadEnforcer.ANY);
        return bus;
    }
}
