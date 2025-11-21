package com.example.iq5.core.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ViewUtils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(visibility);
            }
        }
    }

    public static void setInvisible(View... views) {
        setVisibility(View.INVISIBLE, views);
    }

    public static void setVisible(View... views) {
        setVisibility(View.VISIBLE, views);
    }

    public static void show(View loadingView) {
    }

    public static void hide(View loadingView) {

    }
}