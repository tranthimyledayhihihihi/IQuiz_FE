package com.example.quiz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class PrefUtils {
    private static final String PREF = "quiz_prefs";
    private static final String KEY_SCORE = "score_daily";
    private static final String KEY_MISTAKES = "mistakes_set";

    public static void saveScore(Context c, int score) {
        c.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putInt(KEY_SCORE, score).apply();
    }
    public static int getScore(Context c) {
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE).getInt(KEY_SCORE, 0);
    }

    public static void addMistake(Context c, String q, String rightAns) {
        SharedPreferences sp = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Set<String> cur = new HashSet<>(sp.getStringSet(KEY_MISTAKES, new HashSet<>()));
        cur.add(q + "||" + rightAns);
        sp.edit().putStringSet(KEY_MISTAKES, cur).apply();
    }

    public static Set<String> loadMistakes(Context c) {
        SharedPreferences sp = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return new HashSet<>(sp.getStringSet(KEY_MISTAKES, new HashSet<>()));
    }
}
