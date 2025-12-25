package com.example.iq5.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.iq5.debug.DebugAchievementActivity;

/**
 * Helper class for debugging functions
 */
public class DebugHelper {
    
    /**
     * Open Debug Achievement Activity
     */
    public static void openDebugAchievement(Context context) {
        try {
            Intent intent = new Intent(context, DebugAchievementActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "❌ Cannot open debug activity: " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Open Network Test Activity
     */
    public static void openNetworkTest(Context context) {
        try {
            Intent intent = new Intent(context, com.example.iq5.debug.NetworkTestActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "❌ Cannot open network test: " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
        }
    }
    public static void showTokenDebug(Context context) {
        com.example.iq5.core.prefs.PrefsManager prefsManager = 
            new com.example.iq5.core.prefs.PrefsManager(context);
        
        String token = prefsManager.getAuthToken();
        if (token != null) {
            String preview = token.length() > 30 ? token.substring(0, 30) + "..." : token;
            Toast.makeText(context, "🔑 Token: " + preview, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "❌ No token found", Toast.LENGTH_SHORT).show();
        }
    }
}