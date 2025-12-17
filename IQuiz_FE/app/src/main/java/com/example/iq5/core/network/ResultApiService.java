package com.example.iq5.core.network;

import com.example.iq5.data.model.ScoreDto;
import com.example.iq5.data.model.ScoreRequest;
import com.example.iq5.data.model.SubmissionDto;
import com.example.iq5.data.model.SubmissionRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ResultApiService {

    @POST("nopbai")
    Call<SubmissionDto> submitAssignment(@Body SubmissionRequest submission);

    @POST("nopbai/{nopBaiId}/chamdiem")
    Call<ScoreDto> scoreSubmission(@Path("nopBaiId") int nopBaiId, @Body ScoreRequest score);

    @GET("nopbai")
    Call<List<SubmissionDto>> getSubmissionsByAssignment(@Query("doAnId") int doAnId);
}