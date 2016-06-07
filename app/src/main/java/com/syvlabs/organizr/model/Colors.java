package com.syvlabs.organizr.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.syvlabs.organizr.R;

import java.util.Arrays;

public class Colors {
    private static int[] noteColors;

    public static int[] getNoteColors(Context context) {
        if (noteColors  == null)
            noteColors = context.getResources().getIntArray(R.array.noteColors);
        return noteColors;
    }

    public static int getDefaultNoteColor(Context context) {
        return getNoteColors(context)[2];
    }

    public static int getNextColor(Context context, int color) {
        int[] colors = getNoteColors(context);
        int pos = -1;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == color) {
                pos = i;
                break;
            }
        }
        return noteColors[(pos + 1)%noteColors.length];
    }
}
