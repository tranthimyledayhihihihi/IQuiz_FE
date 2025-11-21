package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import android.widget.Toast;


public class CompareResultActivity extends AppCompatActivity {

    private TextView tvResultStatus, tvYourFinalScore, tvOpponentFinalScore;
    private Button btnPlayAgain, btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        displayResults();
        setupButtons();
    }

    private void displayResults() {
        int yourScore = getIntent().getIntExtra("your_score", 100);
        int opponentScore = getIntent().getIntExtra("opponent_score", 80);
        boolean isWinner = getIntent().getBooleanExtra("is_winner", true);

        String result = isWinner ? "🎉 CHIẾN THẮNG!" : 
                       (yourScore == opponentScore ? "🤝 HÒA!" : "😞 THUA CUỘC");
        
        Toast.makeText(this, result + "\nBạn: " + yourScore + " - Đối thủ: " + opponentScore, 
                      Toast.LENGTH_LONG).show();
    }

    private void setupButtons() {
        // Sử dụng button có sẵn trong activity_result
        if (findViewById(R.id.btn_play_again) != null) {
            findViewById(R.id.btn_play_again).setOnClickListener(v -> {
                NavigationHelper.navigateToFindMatch(this);
                finish();
            });
        }
        
        if (findViewById(R.id.btn_retry) != null) {
            findViewById(R.id.btn_retry).setOnClickListener(v -> {
                NavigationHelper.navigateToHome(this, false);
                finish();
            });
        }
    }
}
