package com.example.quiz.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.content.Context;


public class ViewUtils {
    public static void fadeIn(View v) {
        if (v == null) return;
        Context c = v.getContext();
        Animation a = AnimationUtils.loadAnimation(c, com.example.quiz.R.anim.fade_in);
        v.startAnimation(a);
    }


    public static void slideUp(View v) {
        if (v == null) return;
        Context c = v.getContext();
        Animation a = AnimationUtils.loadAnimation(c, com.example.quiz.R.anim.slide_up);
        v.startAnimation(a);
    }
}