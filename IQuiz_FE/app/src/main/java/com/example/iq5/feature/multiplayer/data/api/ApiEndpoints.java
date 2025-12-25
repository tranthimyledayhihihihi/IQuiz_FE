package com.example.iq5.feature.multiplayer.data.api;

public class ApiEndpoints {
    // Base URL - THAY ĐỔI THEO SERVER CỦA BẠN
    public static final String BASE_URL = "http://172.26.97.66:5048/";

    // ===============================
    // MULTIPLAYER ENDPOINTS
    // ===============================
    public static final String ONLINE_COUNT = "trandau/online-count";
    public static final String CREATE_MATCH = "trandau/create-match";
    public static final String JOIN_MATCH = "trandau/join-match";
    public static final String GET_MATCH_INFO = "trandau/match/{matchCode}";
    public static final String GET_QUESTIONS = "trandau/questions/{matchCode}";
    public static final String SUBMIT_ANSWER = "trandau/submit-answer";
    public static final String END_MATCH = "trandau/end-match/{matchCode}";

    // ===============================
    // USER ENDPOINTS
    // ===============================
    public static final String LOGIN = "auth/login";
    public static final String REGISTER = "auth/register";
    public static final String GET_USER_INFO = "user/info";

    // ===============================
    // QUESTION ENDPOINTS
    // ===============================
    public static final String GET_ALL_QUESTIONS = "cauhoi/all";
    public static final String GET_QUESTION_BY_ID = "cauhoi/{id}";
}