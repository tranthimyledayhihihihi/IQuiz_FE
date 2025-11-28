package com.example.iq5.core.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.iq5.feature.auth.ui.HomeActivity;
import com.example.iq5.feature.auth.ui.LoginActivity;
import com.example.iq5.feature.auth.ui.ProfileActivity;
import com.example.iq5.feature.auth.ui.RegisterActivity;
import com.example.iq5.feature.auth.ui.SettingsActivity;
import com.example.iq5.feature.auth.ui.SplashActivity;
import com.example.iq5.feature.multiplayer.ui.CompareResultActivity;
import com.example.iq5.feature.multiplayer.ui.FindMatchActivity;
import com.example.iq5.feature.multiplayer.ui.FriendsActivity;
import com.example.iq5.feature.multiplayer.ui.LeaderboardActivity;
import com.example.iq5.feature.multiplayer.ui.PvPBattleActivity;
import com.example.iq5.feature.multiplayer.ui.RoomLobbyActivity;
import com.example.iq5.feature.quiz.ui.QuizActivity;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity;
import com.example.iq5.feature.result.ui.AchievementActivity;
import com.example.iq5.feature.result.ui.DailyRewardActivity;
import com.example.iq5.feature.result.ui.ResultActivity;
import com.example.iq5.feature.result.ui.StatsActivity;
import com.example.iq5.feature.result.ui.StreakActivity;

/**
 * Helper class để quản lý navigation giữa các màn hình
 * Sử dụng: NavigationHelper.navigateToHome(context);
 */
public class NavigationHelper {

    // ==================== AUTH FLOW ====================
    
    /**
     * Chuyển đến màn hình Splash (khởi động app)
     */
    public static void navigateToSplash(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Login
     * @param clearStack true nếu muốn xóa toàn bộ back stack
     */
    public static void navigateToLogin(Context context, boolean clearStack) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Register
     */
    public static void navigateToRegister(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Home (sau khi login thành công)
     * @param clearStack true để xóa back stack (không cho back về login)
     */
    public static void navigateToHome(Context context, boolean clearStack) {
        Intent intent = new Intent(context, HomeActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Profile
     */
    public static void navigateToProfile(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Settings
     */
    public static void navigateToSettings(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    // ==================== QUIZ FLOW ====================

    /**
     * Chuyển đến màn hình chọn Category
     */
    public static void navigateToSelectCategory(Context context) {
        Intent intent = new Intent(context, SelectCategoryActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Quiz với category và difficulty
     */
    public static void navigateToQuiz(Context context, String categoryId, String difficulty) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("difficulty", difficulty);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Quiz với Bundle tùy chỉnh
     */
    public static void navigateToQuiz(Context context, Bundle extras) {
        Intent intent = new Intent(context, QuizActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Review Questions
     */
    public static void navigateToReviewQuestions(Context context, String quizId) {
        Intent intent = new Intent(context, ReviewQuestionActivity.class);
        intent.putExtra("quiz_id", quizId);
        context.startActivity(intent);
    }

    // ==================== RESULT FLOW ====================

    /**
     * Chuyển đến màn hình Result sau khi hoàn thành quiz
     */
    public static void navigateToResult(Context context, Bundle resultData) {
        Intent intent = new Intent(context, ResultActivity.class);
        if (resultData != null) {
            intent.putExtras(resultData);
        }
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Daily Reward
     */
    public static void navigateToDailyReward(Context context) {
        Intent intent = new Intent(context, DailyRewardActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Achievement
     */
    public static void navigateToAchievement(Context context) {
        Intent intent = new Intent(context, AchievementActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Stats
     */
    public static void navigateToStats(Context context) {
        Intent intent = new Intent(context, StatsActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Streak
     */
    public static void navigateToStreak(Context context) {
        Intent intent = new Intent(context, StreakActivity.class);
        context.startActivity(intent);
    }

    // ==================== MULTIPLAYER FLOW ====================

    /**
     * Chuyển đến màn hình Find Match (tìm đối thủ)
     */
    public static void navigateToFindMatch(Context context) {
        Intent intent = new Intent(context, FindMatchActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Room Lobby
     */
    public static void navigateToRoomLobby(Context context, String roomId) {
        Intent intent = new Intent(context, RoomLobbyActivity.class);
        intent.putExtra("room_id", roomId);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình PvP Battle
     */
    public static void navigateToPvPBattle(Context context, String matchId) {
        Intent intent = new Intent(context, PvPBattleActivity.class);
        intent.putExtra("match_id", matchId);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Compare Result (so sánh kết quả PvP)
     */
    public static void navigateToCompareResult(Context context, Bundle matchData) {
        Intent intent = new Intent(context, CompareResultActivity.class);
        if (matchData != null) {
            intent.putExtras(matchData);
        }
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Friends
     */
    public static void navigateToFriends(Context context) {
        Intent intent = new Intent(context, FriendsActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Leaderboard
     */
    public static void navigateToLeaderboard(Context context) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        context.startActivity(intent);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Finish activity hiện tại và quay về màn hình trước
     */
    public static void goBack(Activity activity) {
        activity.finish();
    }

    /**
     * Finish activity và chuyển đến màn hình mới
     */
    public static void navigateAndFinish(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    /**
     * Logout và quay về màn hình Login
     */
    public static void logout(Context context) {
        // Xóa session/token ở đây nếu cần
        navigateToLogin(context, true);
    }
}
