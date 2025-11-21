package com.example.iq5.core.network;

import com.example.iq5.data.model.GameSessionDto;
import com.example.iq5.data.model.MatchmakingRequest;
import com.example.iq5.data.model.PlayerAnswerDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MultiplayerApiService {
    @POST("multiplayer/matchmaking")
    Call<GameSessionDto> startMatchmaking(@Body MatchmakingRequest request);

    @POST("multiplayer/session/{sessionId}/answer")
    Call<GameSessionDto> submitAnswer(@Path("sessionId") String sessionId, @Body PlayerAnswerDto answer);
}