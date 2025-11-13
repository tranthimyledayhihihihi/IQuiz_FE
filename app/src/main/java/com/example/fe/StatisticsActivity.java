package com.example.fe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class StatisticsActivity extends AppCompatActivity {

    private Spinner spinnerTimePeriod;
    private RecyclerView rvStatisticsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        spinnerTimePeriod = findViewById(R.id.spinner_time_period);
        rvStatisticsList = findViewById(R.id.rv_statistics_list);

        // Thiết lập Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statistic_periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimePeriod.setAdapter(adapter);

        spinnerTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPeriod = parent.getItemAtPosition(position).toString();
                // TODO: Gọi API 5. Lấy dữ liệu thống kê theo ngày
                loadStatistics(selectedPeriod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Thiết lập RecyclerView
        // ... Cần tạo StatisticsAdapter
        rvStatisticsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadStatistics(String period) {
        Toast.makeText(this, "Đang tải dữ liệu cho: " + period, Toast.LENGTH_SHORT).show();
    }
}