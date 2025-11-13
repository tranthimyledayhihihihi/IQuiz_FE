package com.example.iq5.core.network;

import com.example.iq5.data.model.TopAssignmentDto;
import com.example.iq5.data.model.HistoryRecordDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpecialApiService {
    @GET("stats/top-assignments")
    Call<List<TopAssignmentDto>> getTopAssignments(@Query("topN") int topCount);

    @GET("history")
    Call<List<HistoryRecordDto>> getHistory(@Query("objectType") String type, @Query("objectId") int id);
}