package me.dlet.androidorgapp.bus;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class Bus extends com.squareup.otto.Bus {
    private static final Bus bus = new Bus();

    private Bus() {
        super();
    }

    public static Bus getInstance() {
        return bus;
    }

}
