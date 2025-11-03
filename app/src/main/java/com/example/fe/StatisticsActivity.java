package com.example.fe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;

import java.util.*;

public class StatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        BarChart chart = findViewById(R.id.barChart);
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= 7; i++) entries.add(new BarEntry(i, (float)(Math.random() * 10)));
        BarDataSet dataSet = new BarDataSet(entries, "Điểm mỗi ngày");
        chart.setData(new BarData(dataSet));
        chart.invalidate();
    }
}
