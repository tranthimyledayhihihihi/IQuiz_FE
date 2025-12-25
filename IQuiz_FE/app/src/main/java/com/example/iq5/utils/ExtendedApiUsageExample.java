package com.example.iq5.utils;

import android.util.Log;
import com.example.iq5.core.network.ApiServiceFactory;
import com.example.iq5.core.network.DailyQuizApiService;
import com.example.iq5.core.network.SocialApiService;
import com.example.iq5.core.prefs.PrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Ví dụ sử dụng các API mở rộng: Social, Daily Quiz, etc.
 */
public class ExtendedApiUsageExample {
    private static final String TAG = "ExtendedApiExample";
    
    // ===============================================
    // 1. BẢNG XẾP HẠNG
    // ===============================================
    public static void getLeaderboardExample(PrefsManager prefsManager) {
        SocialApiService socialService = ApiServiceFactory.getSocialService(prefsManager);
        
        Call<SocialApiService.LeaderboardResponse> call = socialService.getLeaderboard("monthly", 1, 10);
        
        call.enqueue(new Callback<SocialApiService.LeaderboardResponse>() {
            @Override
            public void onResponse(Call<SocialApiService.LeaderboardResponse> call, Response<SocialApiService.LeaderboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SocialApiService.LeaderboardResponse leaderboard = response.body();
                    
                    Log.d(TAG, "✅ Lấy bảng xếp hạng thành công!");
                    Log.d(TAG, "📊 Loại: " + leaderboard.getType());
                    Log.d(TAG, "👥 Tổng số người: " + leaderboard.getTongSoNguoi());
                    
                    for (SocialApiService.RankingUser user : leaderboard.getDanhSach()) {
                        Log.d(TAG, "🏆 #" + user.getRank() + " - " + user.getHoTen() + " (" + user.getTotalScore() + " điểm)");
                    }
                    
                    // Cập nhật RecyclerView với dữ liệu leaderboard
                    
                } else {
                    Log.e(TAG, "❌ Lỗi lấy bảng xếp hạng: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SocialApiService.LeaderboardResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi lấy bảng xếp hạng: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 2. THÀNH TỰU CỦA TÔI
    // ===============================================
    public static void getMyAchievementsExample(PrefsManager prefsManager) {
        SocialApiService socialService = ApiServiceFactory.getSocialService(prefsManager);
        
        Call<SocialApiService.AchievementsResponse> call = socialService.getMyAchievements();
        
        call.enqueue(new Callback<SocialApiService.AchievementsResponse>() {
            @Override
            public void onResponse(Call<SocialApiService.AchievementsResponse> call, Response<SocialApiService.AchievementsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SocialApiService.AchievementsResponse achievements = response.body();
                    
                    Log.d(TAG, "✅ Lấy thành tựu thành công!");
                    
                    if (achievements.getAchievements() != null) {
                        for (SocialApiService.Achievement achievement : achievements.getAchievements()) {
                            Log.d(TAG, "🏅 " + achievement.getTenThanhTuu() + " - " + achievement.getMoTa());
                        }
                    } else {
                        Log.d(TAG, "📝 " + achievements.getMessage());
                    }
                    
                    // Cập nhật UI với danh sách thành tựu
                    
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Token hết hạn, cần đăng nhập lại");
                } else {
                    Log.e(TAG, "❌ Lỗi lấy thành tựu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SocialApiService.AchievementsResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi lấy thành tựu: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 3. QUIZ HÀNG NGÀY - LẤY THÔNG TIN
    // ===============================================
    public static void getTodayQuizExample(PrefsManager prefsManager) {
        DailyQuizApiService dailyService = ApiServiceFactory.getDailyQuizService(prefsManager);
        
        Call<DailyQuizApiService.DailyQuizDetails> call = dailyService.getTodayQuiz();
        
        call.enqueue(new Callback<DailyQuizApiService.DailyQuizDetails>() {
            @Override
            public void onResponse(Call<DailyQuizApiService.DailyQuizDetails> call, Response<DailyQuizApiService.DailyQuizDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DailyQuizApiService.DailyQuizDetails dailyQuiz = response.body();
                    
                    Log.d(TAG, "✅ Lấy quiz hàng ngày thành công!");
                    Log.d(TAG, "📅 Tiêu đề: " + dailyQuiz.getTieuDe());
                    Log.d(TAG, "📝 Mô tả: " + dailyQuiz.getMoTa());
                    Log.d(TAG, "❓ Câu hỏi: " + dailyQuiz.getCauHoi().getNoiDung());
                    
                    // Hiển thị thông tin quiz hàng ngày trong UI
                    
                } else if (response.code() == 404) {
                    Log.w(TAG, "⚠️ Chưa có quiz hàng ngày nào hôm nay");
                } else {
                    Log.e(TAG, "❌ Lỗi lấy quiz hàng ngày: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DailyQuizApiService.DailyQuizDetails> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi lấy quiz hàng ngày: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 4. QUIZ HÀNG NGÀY - BẮT ĐẦU LÀM BÀI
    // ===============================================
    public static void startTodayQuizExample(PrefsManager prefsManager) {
        DailyQuizApiService dailyService = ApiServiceFactory.getDailyQuizService(prefsManager);
        
        Call<DailyQuizApiService.DailyQuizStartResponse> call = dailyService.startTodayQuiz();
        
        call.enqueue(new Callback<DailyQuizApiService.DailyQuizStartResponse>() {
            @Override
            public void onResponse(Call<DailyQuizApiService.DailyQuizStartResponse> call, Response<DailyQuizApiService.DailyQuizStartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DailyQuizApiService.DailyQuizStartResponse startResponse = response.body();
                    
                    Log.d(TAG, "✅ Bắt đầu quiz hàng ngày thành công!");
                    Log.d(TAG, "🎯 Attempt ID: " + startResponse.getAttemptID());
                    Log.d(TAG, "❓ Câu hỏi: " + startResponse.getQuestion().getNoiDung());
                    
                    // Chuyển đến DailyQuizActivity với attemptID và question
                    
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Cần đăng nhập để làm quiz hàng ngày");
                } else {
                    Log.e(TAG, "❌ Lỗi bắt đầu quiz hàng ngày: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DailyQuizApiService.DailyQuizStartResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi bắt đầu quiz hàng ngày: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 5. SỐ NGƯỜI ONLINE
    // ===============================================
    public static void getOnlineCountExample(PrefsManager prefsManager) {
        SocialApiService socialService = ApiServiceFactory.getSocialService(prefsManager);
        
        Call<SocialApiService.OnlineCountResponse> call = socialService.getOnlineCount();
        
        call.enqueue(new Callback<SocialApiService.OnlineCountResponse>() {
            @Override
            public void onResponse(Call<SocialApiService.OnlineCountResponse> call, Response<SocialApiService.OnlineCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SocialApiService.OnlineCountResponse onlineCount = response.body();
                    
                    Log.d(TAG, "✅ Lấy số người online thành công!");
                    Log.d(TAG, "👥 Số người online: " + onlineCount.getTongNguoiOnline());
                    
                    // Cập nhật UI với số người online
                    
                } else {
                    Log.e(TAG, "❌ Lỗi lấy số người online: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SocialApiService.OnlineCountResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi lấy số người online: " + t.getMessage());
            }
        });
    }
}