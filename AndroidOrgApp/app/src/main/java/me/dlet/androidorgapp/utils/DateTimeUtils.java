package me.dlet.androidorgapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin@creativehothouse.com
 */
public class DateTimeUtils {
    public static String format(long time){
        return new SimpleDateFormat("MM-dd-yyyy HH:ss").format(new Date(time));
    }
}
