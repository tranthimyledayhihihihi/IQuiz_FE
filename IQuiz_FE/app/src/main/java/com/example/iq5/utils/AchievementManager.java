package com.example.iq5.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.iq5.feature.result.model.Achievement;

import java.util.ArrayList;
import java.util.List;

/**
 * Local Achievement Manager - tracks achievements using SharedPreferences
 * Real-time updates without API dependency
 */
public class AchievementManager {
    
    private static final String TAG = "AchievementManager";
    private static final String PREFS_NAME = "achievement_stats";
    
    // Stats keys
    private static final String KEY_TOTAL_QUIZZES = "total_quizzes";
    private static final String KEY_TOTAL_CORRECT = "total_correct";
    private static final String KEY_PERFECT_SCORES = "perfect_scores";
    private static final String KEY_TOTAL_SCORE = "total_score";
    private static final String KEY_LAST_PLAY_DATE = "last_play_date";
    
    private final SharedPreferences prefs;
    private final Context context;
    
    public AchievementManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Update stats after completing a quiz
     */
    public void updateQuizStats(int correctAnswers, int totalQuestions, int score) {
        Log.d(TAG, String.format("📊 Updating stats: %d/%d correct, score: %d", 
            correctAnswers, totalQuestions, score));
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Update counters
        int currentQuizzes = prefs.getInt(KEY_TOTAL_QUIZZES, 0);
        int currentCorrect = prefs.getInt(KEY_TOTAL_CORRECT, 0);
        int currentTotalScore = prefs.getInt(KEY_TOTAL_SCORE, 0);
        int currentPerfectScores = prefs.getInt(KEY_PERFECT_SCORES, 0);
        
        editor.putInt(KEY_TOTAL_QUIZZES, currentQuizzes + 1);
        editor.putInt(KEY_TOTAL_CORRECT, currentCorrect + correctAnswers);
        editor.putInt(KEY_TOTAL_SCORE, currentTotalScore + score);
        editor.putLong(KEY_LAST_PLAY_DATE, System.currentTimeMillis());
        
        // Check for perfect score
        if (score >= 100) {
            editor.putInt(KEY_PERFECT_SCORES, currentPerfectScores + 1);
            Log.d(TAG, "🎉 Perfect score achieved! Total: " + (currentPerfectScores + 1));
        }
        
        editor.apply();
        
        Log.d(TAG, String.format("✅ Stats updated - Total quizzes: %d, Perfect scores: %d", 
            currentQuizzes + 1, score >= 100 ? currentPerfectScores + 1 : currentPerfectScores));
    }
    
    /**
     * Get current stats
     */
    public QuizStats getStats() {
        QuizStats stats = new QuizStats();
        stats.totalQuizzes = prefs.getInt(KEY_TOTAL_QUIZZES, 0);
        stats.totalCorrect = prefs.getInt(KEY_TOTAL_CORRECT, 0);
        stats.totalScore = prefs.getInt(KEY_TOTAL_SCORE, 0);
        stats.perfectScores = prefs.getInt(KEY_PERFECT_SCORES, 0);
        stats.lastPlayDate = prefs.getLong(KEY_LAST_PLAY_DATE, 0);
        
        // Calculate average
        stats.averageScore = stats.totalQuizzes > 0 ? 
            (double) stats.totalScore / stats.totalQuizzes : 0.0;
            
        return stats;
    }
    
    /**
     * Generate achievements based on current stats
     */
    public List<Achievement> generateAchievements() {
        QuizStats stats = getStats();
        List<Achievement> achievements = new ArrayList<>();
        
        Log.d(TAG, String.format("🏆 Generating achievements from stats: %d quizzes, %.1f avg, %d perfect", 
            stats.totalQuizzes, stats.averageScore, stats.perfectScores));
        
        // 1. First Quiz
        achievements.add(new Achievement(1, "🎯 Người mới bắt đầu", 
            "Hoàn thành quiz đầu tiên", 
            stats.totalQuizzes >= 1, "🎯", 
            Math.min(stats.totalQuizzes, 1), 1));
        
        // 2. 5 Quizzes
        achievements.add(new Achievement(2, "📚 Học sinh chăm chỉ", 
            "Hoàn thành 5 quiz", 
            stats.totalQuizzes >= 5, "📚", 
            Math.min(stats.totalQuizzes, 5), 5));
        
        // 3. 10 Quizzes
        achievements.add(new Achievement(3, "🎓 Thạc sĩ tri thức", 
            "Hoàn thành 10 quiz", 
            stats.totalQuizzes >= 10, "🎓", 
            Math.min(stats.totalQuizzes, 10), 10));
        
        // 4. High Average 80
        achievements.add(new Achievement(4, "🥇 Chuyên gia", 
            "Đạt điểm trung bình trên 80", 
            stats.averageScore >= 80, "🥇", 
            (int) Math.min(stats.averageScore, 80), 80));
        
        // 5. High Average 90
        achievements.add(new Achievement(5, "🏆 Bậc thầy", 
            "Đạt điểm trung bình trên 90", 
            stats.averageScore >= 90, "🏆", 
            (int) Math.min(stats.averageScore, 90), 90));
        
        // 6. First Perfect Score
        achievements.add(new Achievement(6, "💯 Hoàn hảo", 
            "Đạt điểm tuyệt đối lần đầu", 
            stats.perfectScores >= 1, "💯", 
            Math.min(stats.perfectScores, 1), 1));
        
        // 7. 3 Perfect Scores
        achievements.add(new Achievement(7, "⭐ Siêu sao", 
            "Đạt điểm tuyệt đối 3 lần", 
            stats.perfectScores >= 3, "⭐", 
            Math.min(stats.perfectScores, 3), 3));
        
        // 8. 20 Quizzes (long term goal)
        achievements.add(new Achievement(8, "🚀 Chinh phục viên", 
            "Hoàn thành 20 quiz", 
            stats.totalQuizzes >= 20, "🚀", 
            Math.min(stats.totalQuizzes, 20), 20));
        
        // Count unlocked
        int unlockedCount = 0;
        for (Achievement ach : achievements) {
            if (ach.isUnlocked()) unlockedCount++;
        }
        
        Log.d(TAG, String.format("✅ Generated %d achievements (%d unlocked, %d locked)", 
            achievements.size(), unlockedCount, achievements.size() - unlockedCount));
        
        return achievements;
    }
    
    /**
     * Reset all stats (for testing)
     */
    public void resetStats() {
        prefs.edit().clear().apply();
        Log.d(TAG, "🗑️ All stats reset");
    }
    
    /**
     * Stats data class
     */
    public static class QuizStats {
        public int totalQuizzes = 0;
        public int totalCorrect = 0;
        public int totalScore = 0;
        public int perfectScores = 0;
        public double averageScore = 0.0;
        public long lastPlayDate = 0;
        
        @Override
        public String toString() {
            return String.format("QuizStats{quizzes=%d, avg=%.1f, perfect=%d}", 
                totalQuizzes, averageScore, perfectScores);
        }
    }
}