package com.example.iq5.core.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

// AUTH FLOW
import com.example.iq5.feature.auth.ui.HomeActivity;
import com.example.iq5.feature.auth.ui.LoginActivity;
import com.example.iq5.feature.auth.ui.ProfileActivity;
import com.example.iq5.feature.auth.ui.RegisterActivity;
import com.example.iq5.feature.auth.ui.SettingsActivity;
import com.example.iq5.feature.auth.ui.SplashActivity;

// MULTIPLAYER FLOW

import com.example.iq5.feature.multiplayer.ui.CreateRoomActivity;

import com.example.iq5.feature.multiplayer.ui.JoinRoomActivity;
import com.example.iq5.feature.multiplayer.ui.MatchResultActivity;


// QUIZ FLOW
import com.example.iq5.feature.multiplayer.ui.MultiplayerLobbyActivity;
import com.example.iq5.feature.multiplayer.ui.WaitingRoomActivity;
import com.example.iq5.feature.quiz.ui.QuizActivity;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity;

// RESULT FLOW
import com.example.iq5.feature.result.ui.AchievementActivity;
import com.example.iq5.feature.result.ui.DailyRewardActivity;
import com.example.iq5.feature.result.ui.ResultActivity;
import com.example.iq5.feature.result.ui.StatsActivity;
import com.example.iq5.feature.result.ui.StreakActivity;
import com.example.iq5.feature.specialmode.ui.CustomQuizEditorActivity;


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

    // ----------------------------------------------------
    // ==================== QUIZ FLOW ====================
    // ----------------------------------------------------

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
     * Chuyển đến màn hình API Quiz với category và difficulty (sử dụng API thật)
     */
    public static void navigateToApiQuiz(Context context, String categoryId, String difficulty) {
        Intent intent = new Intent(context, com.example.iq5.feature.quiz.ui.ApiQuizActivity.class);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("difficulty", difficulty);
        context.startActivity(intent);
    }
    public static void navigateToMultiplayerLobby(Context context) {
        Intent intent = new Intent(context, MultiplayerLobbyActivity.class);
        context.startActivity(intent);
    }
    /**
     * Chuyển đến màn hình API Select Category (sử dụng API thật)
     */
    public static void navigateToApiSelectCategory(Context context) {
        Intent intent = new Intent(context, com.example.iq5.feature.quiz.ui.ApiSelectCategoryActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình API Quiz với danh sách câu hỏi đã tải sẵn
     */
    public static void navigateToApiQuizWithQuestions(Context context, java.util.List<com.example.iq5.core.network.QuizApiService.TestQuestionModel> questions, String categoryName) {
        Intent intent = new Intent(context, com.example.iq5.feature.quiz.ui.ApiQuizActivity.class);

        // Convert questions to JSON string để truyền qua Intent
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String questionsJson = gson.toJson(questions);

        intent.putExtra("questions_json", questionsJson);
        intent.putExtra("category_name", categoryName);
        intent.putExtra("has_questions", true);

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

    // ----------------------------------------------------
    // ==================== RESULT FLOW ====================
    // ----------------------------------------------------

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
     * Chuyển đến màn hình Daily Reward (Activity nằm trong result.ui)
     */
    public static void navigateToDailyReward(Context context) {
        Intent intent = new Intent(context, DailyRewardActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Achievement (Activity nằm trong result.ui)
     */
    public static void navigateToAchievement(Context context) {
        Intent intent = new Intent(context, AchievementActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Stats (Activity nằm trong result.ui)
     */
    public static void navigateToStats(Context context) {
        Intent intent = new Intent(context, StatsActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến màn hình Streak (Activity nằm trong result.ui)
     */
    public static void navigateToStreak(Context context) {
        Intent intent = new Intent(context, StreakActivity.class);
        context.startActivity(intent);
    }

    // ----------------------------------------------------
    // ==================== MULTIPLAYER FLOW ====================
    // ----------------------------------------------------

    /**
     public static void navigateToMultiplayerLobby(Context context) {
     Intent intent = new Intent(context, MultiplayerLobbyActivity.class);
     context.startActivity(intent);
     }

     /**
     * 👉 Tạo phòng đối kháng
     */
    public static void navigateToCreateRoom(Context context) {
        Intent intent = new Intent(context, CreateRoomActivity.class);
        context.startActivity(intent);
    }

    /**
     * 👉 Join phòng bằng mã
     */
    public static void navigateToJoinRoom(Context context) {
        Intent intent = new Intent(context, JoinRoomActivity.class);
        context.startActivity(intent);
    }

    /**
     * 👉 Phòng chờ (sau khi tạo / join phòng)
     */
    public static void navigateToWaitingRoom(Context context, String matchCode) {
        Intent intent = new Intent(context, WaitingRoomActivity.class);
        intent.putExtra("matchCode", matchCode);
        context.startActivity(intent);
    }

    /**
     * 👉 Màn hình chơi trận đấu (PvP thực tế)
     */
    public static void navigateToMatch(Context context, String matchCode) {
        Intent intent = new Intent(context, MatchResultActivity.class);
        intent.putExtra("matchCode", matchCode);
        context.startActivity(intent);
    }

    /**
     * 👉 Màn hình kết quả trận đấu
     */
    public static void navigateToMatchResult(
            Context context,
            String matchCode,
            int yourScore,
            int opponentScore,
            String result,
            int winnerUserId
    ) {
        Intent intent = new Intent(context, MatchResultActivity.class);
        intent.putExtra("matchCode", matchCode);
        intent.putExtra("yourScore", yourScore);
        intent.putExtra("opponentScore", opponentScore);
        intent.putExtra("result", result);
        intent.putExtra("winnerUserId", winnerUserId);
        context.startActivity(intent);
    }
    /**
     * Chuyển đến màn hình Friends
     */


    /**
     * Chuyển đến màn hình Leaderboard
     */

    // ----------------------------------------------------
    // ==================== UTILITY METHODS ====================
    // ----------------------------------------------------

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
    public static void navigateToCustomQuiz(Context context) {
        Intent intent = new Intent(context, CustomQuizEditorActivity.class);
        context.startActivity(intent);
    }

    /**
     * Logout và quay về màn hình Login
     */
    public static void logout(Context context) {
        // Xóa session/token ở đây nếu cần
        navigateToLogin(context, true);
    }
}