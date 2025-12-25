package com.example.iq5.core.network;

import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.PvPBattleResponse;
import com.example.iq5.data.model.PvPAnswerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PvPApiService {
    
    @GET("api/pvp/battles/user/{userId}")
    Call<PvPBattleResponse> getUserBattles(@Path("userId") int userId);
    
    @GET("api/pvp/battle/{battleId}/answers")
    Call<PvPAnswerResponse> getBattleAnswers(@Path("battleId") int battleId);
    
    @POST("api/pvp/battle/create")
    Call<ApiResponse> createBattle(@Body CreateBattleRequest request);
    
    @POST("api/pvp/battle/{battleId}/join")
    Call<ApiResponse> joinBattle(@Path("battleId") int battleId, @Body JoinBattleRequest request);
    
    class CreateBattleRequest {
        public int NguoiChoi1ID;
        
        public CreateBattleRequest(int nguoiChoi1Id) {
            this.NguoiChoi1ID = nguoiChoi1Id;
        }
    }
    
    class JoinBattleRequest {
        public int NguoiChoi2ID;
        
        public JoinBattleRequest(int nguoiChoi2Id) {
            this.NguoiChoi2ID = nguoiChoi2Id;
        }
    }
}