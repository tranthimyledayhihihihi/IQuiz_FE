package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;

public class PvPBattleActivity extends AppCompatActivity {

    private TextView tvQuestion, tvYourScore, tvOpponentScore, tvTimer;
    private Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    private String matchId;
    private int yourScore = 0;
    private int opponentScore = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        matchId = getIntent().getStringExtra("match_id");
        if (matchId == null) matchId = "unknown";

        Toast.makeText(this, "Trận đấu PvP - Trả lời câu hỏi!", Toast.LENGTH_SHORT).show();
        handler = new Handler(getMainLooper());
        
        simulateBattle();
        
        // Tự động kết thúc sau 5 giây
        handler.postDelayed(this::endBattle, 5000);
    }

    private void simulateBattle() {
        // Giả lập điểm số
        yourScore = 100;
        opponentScore = 80;
    }

    private void endBattle() {
        Bundle matchData = new Bundle();
        matchData.putInt("your_score", yourScore);
        matchData.putInt("opponent_score", opponentScore);
        matchData.putString("match_id", matchId);
        matchData.putBoolean("is_winner", yourScore > opponentScore);
        
        NavigationHelper.navigateToCompareResult(this, matchData);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
