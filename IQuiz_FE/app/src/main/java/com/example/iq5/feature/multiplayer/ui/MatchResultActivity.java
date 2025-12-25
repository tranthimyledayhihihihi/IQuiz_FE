package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;

public class MatchResultActivity extends AppCompatActivity {

    private TextView tvResult, tvMatchCode, tvYourScore, tvOpponentScore, tvResultMessage;
    private Button btnBackToLobby, btnViewDetails;

    private String matchCode;
    private int yourScore;
    private int opponentScore;
    private String result;
    private int winnerUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ GẮN LAYOUT (BẮT BUỘC)
        setContentView(R.layout.activity_match_result);

        // ✅ LẤY DATA
        getIntentData();

        // ✅ ÁNH XẠ VIEW
        initViews();

        // ✅ HIỂN THỊ KẾT QUẢ
        displayResult();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;

        matchCode = intent.getStringExtra("matchCode");
        yourScore = intent.getIntExtra("yourScore", 0);
        opponentScore = intent.getIntExtra("opponentScore", 0);
        result = intent.getStringExtra("result");
        winnerUserId = intent.getIntExtra("winnerUserId", -1);
    }

    private void initViews() {
        tvResult = findViewById(R.id.tvResult);
        tvMatchCode = findViewById(R.id.tvMatchCode);
        tvYourScore = findViewById(R.id.tvYourScore);
        tvOpponentScore = findViewById(R.id.tvOpponentScore);
        tvResultMessage = findViewById(R.id.tvResultMessage);

        btnBackToLobby = findViewById(R.id.btnBackToLobby);
        btnViewDetails = findViewById(R.id.btnViewDetails);

        if (matchCode != null) {
            tvMatchCode.setText("Mã trận: " + matchCode);
        }

        btnBackToLobby.setOnClickListener(v -> backToLobby());
        btnViewDetails.setOnClickListener(v -> viewDetails());
    }

    private void displayResult() {
        tvYourScore.setText(String.valueOf(yourScore));
        tvOpponentScore.setText(String.valueOf(opponentScore));

        if (yourScore > opponentScore) {
            tvResult.setText("🎉 CHIẾN THẮNG!");
            tvResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            tvResultMessage.setText("Chúc mừng bạn đã giành chiến thắng!");
        } else if (yourScore < opponentScore) {
            tvResult.setText("😢 THUA CUỘC");
            tvResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            tvResultMessage.setText("Cố gắng lần sau nhé!");
        } else {
            tvResult.setText("🤝 HÒA");
            tvResult.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            tvResultMessage.setText("Hai bên ngang tài ngang sức!");
        }
    }

    private void backToLobby() {
        Intent intent = new Intent(this, MultiplayerLobbyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void viewDetails() {
        Toast.makeText(this, "Chi tiết trận đấu (coming soon)", Toast.LENGTH_SHORT).show();
    }


}
