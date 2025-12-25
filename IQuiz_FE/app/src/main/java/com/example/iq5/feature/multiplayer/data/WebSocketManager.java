package com.example.iq5.feature.multiplayer.data;

import android.util.Log;

import com.example.iq5.core.network.models.ClientMessage;
import com.example.iq5.core.network.models.MatchFoundData;
import com.example.iq5.core.network.models.ServerMessage;

import com.example.iq5.feature.multiplayer.data.models.GameResult;
import com.example.iq5.feature.multiplayer.data.models.Question;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.*;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ✅ PHIÊN BẢN FINAL - CHỈ WEBSOCKET
 * Đã fix tất cả bugs:
 * - joinRoomWithCode() method
 * - winnerUserID parsing (case-sensitive)
 * - Question parsing (trực tiếp, không qua network model)
 */
public class WebSocketManager {

    private static final String TAG = "WebSocketManager";
    private static WebSocketManager instance;

    private WebSocket webSocket;
    private final OkHttpClient client;
    private final Gson gson;

    // ================= CALLBACKS =================
    private OnMatchFoundListener matchFoundListener;
    private OnQuestionsReceivedListener questionsReceivedListener;
    private OnScoreUpdateListener scoreUpdateListener;
    private OnGameEndListener gameEndListener;
    private OnRoomCreatedListener roomCreatedListener;
    private OnErrorListener errorListener;
    private OnConnectionListener connectionListener;

    // ================= INTERFACES =================
    public interface OnMatchFoundListener {
        void onMatchFound(String matchCode, int opponentId, String role);
    }

    public interface OnQuestionsReceivedListener {
        void onQuestionsReceived(List<Question> questions);
    }

    public interface OnScoreUpdateListener {
        void onScoreUpdate(int userId, int questionId, boolean correct);
    }

    public interface OnGameEndListener {
        void onGameEnd(GameResult result);
    }

    public interface OnRoomCreatedListener {
        void onRoomCreated(String roomCode);
    }

    public interface OnErrorListener {
        void onError(String message);
    }

    public interface OnConnectionListener {
        void onConnected();
        void onDisconnected();
    }

    // ================= CONSTRUCTOR =================
    private WebSocketManager() {
        gson = new Gson();
        client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .pingInterval(20, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    // ================= CONNECT =================
    public void connect(String serverUrl, String token) {
        if (webSocket != null) {
            Log.d(TAG, "Already connected");
            return;
        }

        Request request = new Request.Builder()
                .url(serverUrl)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                Log.d(TAG, "✅ WebSocket Connected");
                if (connectionListener != null) connectionListener.onConnected();
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                Log.d(TAG, "📨 Received: " + text);
                handleMessage(text);
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
                Log.e(TAG, "❌ WebSocket Error", t);
                if (errorListener != null) errorListener.onError(t.getMessage());
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                Log.d(TAG, "🔌 WebSocket Closed: " + reason);
                if (connectionListener != null) connectionListener.onDisconnected();
            }
        });
    }

    // ================= MESSAGE HANDLER =================
    private void handleMessage(String json) {
        try {
            ServerMessage msg = gson.fromJson(json, ServerMessage.class);
            String type = msg.getType();

            Log.d(TAG, "📩 Message Type: " + type);

            switch (type) {
                case "MATCH_FOUND":
                    handleMatchFound(msg.getData());
                    break;

                case "QUESTIONS":
                    handleQuestions(msg.getData());
                    break;

                case "SCORE_UPDATE":
                    handleScoreUpdate(msg.getData());
                    break;

                case "GAME_END":
                    handleGameEnd(msg.getData());
                    break;

                case "ROOM_CREATED":
                    handleRoomCreated(msg.getData());
                    break;

                case "ERROR":
                    handleError(msg.getData());
                    break;

                case "ROOM_EXPIRED":
                    handleRoomExpired(msg.getData());
                    break;

                case "QUEUE_CANCELLED":
                    Log.d(TAG, "Queue cancelled");
                    break;

                default:
                    Log.w(TAG, "Unknown message type: " + type);
                    break;
            }

        } catch (Exception e) {
            Log.e(TAG, "❌ Parse error", e);
        }
    }

    // ================= HANDLERS =================
    private void handleMatchFound(Object data) {
        if (matchFoundListener != null) {
            MatchFoundData d = gson.fromJson(gson.toJson(data), MatchFoundData.class);
            matchFoundListener.onMatchFound(d.getMatchCode(), d.getOpponentId(), d.getYourRole());
        }
    }

    private void handleQuestions(Object data) {
        if (questionsReceivedListener == null) return;

        try {
            // 🔥 FIX: Parse trực tiếp vào feature Question model
            // Model đã có @SerializedName nên Gson tự động map đúng
            Question[] questions = gson.fromJson(
                    gson.toJson(data),
                    Question[].class
            );

            List<Question> questionList = Arrays.asList(questions);

            Log.d(TAG, "✅ Parsed " + questionList.size() + " questions");

            questionsReceivedListener.onQuestionsReceived(questionList);

        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing QUESTIONS", e);
        }
    }

    private void handleScoreUpdate(Object data) {
        if (scoreUpdateListener == null) return;

        JsonObject j = gson.fromJson(gson.toJson(data), JsonObject.class);
        scoreUpdateListener.onScoreUpdate(
                j.get("userId").getAsInt(),
                j.get("questionId").getAsInt(),
                j.get("correct").getAsBoolean()
        );
    }

    private void handleGameEnd(Object data) {
        if (gameEndListener == null) return;

        try {
            // 🔥 FIX: Parse trực tiếp để Gson tự động map winnerUserID
            // Gson sẽ dùng @SerializedName annotation trong GameResult model
            GameResult result = gson.fromJson(gson.toJson(data), GameResult.class);

            Log.d(TAG, "✅ Game End - Result: " + result.getKetQua()
                    + ", Winner ID: " + result.getWinnerUserId());

            gameEndListener.onGameEnd(result);

        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing GAME_END", e);
        }
    }

    private void handleRoomCreated(Object data) {
        if (roomCreatedListener == null) return;
        JsonObject j = gson.fromJson(gson.toJson(data), JsonObject.class);
        roomCreatedListener.onRoomCreated(j.get("roomCode").getAsString());
    }

    private void handleError(Object data) {
        if (errorListener == null) return;
        JsonObject j = gson.fromJson(gson.toJson(data), JsonObject.class);
        errorListener.onError(j.get("message").getAsString());
    }

    private void handleRoomExpired(Object data) {
        if (errorListener == null) return;
        JsonObject j = gson.fromJson(gson.toJson(data), JsonObject.class);
        String message = j.has("message") ? j.get("message").getAsString() : "Phòng đã hết hạn";
        errorListener.onError(message);
    }

    // ================= SEND MESSAGES =================
    private void send(String type, Object data) {
        if (webSocket == null) {
            Log.e(TAG, "❌ Cannot send - WebSocket is null");
            return;
        }

        try {
            String json = gson.toJson(new ClientMessage(type, data));
            webSocket.send(json);
            Log.d(TAG, "📤 Sent: " + type);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error sending message", e);
        }
    }

    // ================= PUBLIC METHODS =================

    /**
     * Tìm trận ngẫu nhiên
     */
    public void findRandomMatch() {
        send("FIND_MATCH", null);
    }

    /**
     * Hủy tìm trận
     */
    public void cancelQueue() {
        send("CANCEL_QUEUE", null);
    }

    /**
     * Tạo phòng riêng
     */
    public void createRoom() {
        send("CREATE_ROOM", null);
    }

    /**
     * ✅ Join phòng bằng room code (đã fix)
     */
    public void joinRoomWithCode(String roomCode) {
        Map<String, String> data = new HashMap<>();
        data.put("roomCode", roomCode);
        send("JOIN_ROOM_CODE", data);
    }

    /**
     * Join vào match đã được ghép (sau khi nhận MATCH_FOUND)
     */
    public void joinMatch(String matchCode) {
        Map<String, String> data = new HashMap<>();
        data.put("matchCode", matchCode);
        send("JOIN_MATCH", data);
    }

    /**
     * Gửi đáp án
     */
    public void submitAnswer(String matchCode, int questionId, String answer) {
        Map<String, Object> data = new HashMap<>();
        data.put("matchCode", matchCode);
        data.put("questionId", questionId);
        data.put("selectedAnswer", answer);
        send("SUBMIT_ANSWER", data);
    }

    // ================= UTILS =================

    public boolean isConnected() {
        return webSocket != null;
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "User closed connection");
            webSocket = null;
            Log.d(TAG, "🔌 Disconnected");
        }
    }

    // ================= SETTERS =================

    public void setOnMatchFoundListener(OnMatchFoundListener l) {
        matchFoundListener = l;
    }

    public void setOnQuestionsReceivedListener(OnQuestionsReceivedListener l) {
        questionsReceivedListener = l;
    }

    public void setOnScoreUpdateListener(OnScoreUpdateListener l) {
        scoreUpdateListener = l;
    }

    public void setOnGameEndListener(OnGameEndListener l) {
        gameEndListener = l;
    }

    public void setOnRoomCreatedListener(OnRoomCreatedListener l) {
        roomCreatedListener = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        errorListener = l;
    }

    public void setOnConnectionListener(OnConnectionListener l) {
        connectionListener = l;
    }

    public void clearAllListeners() {
        matchFoundListener = null;
        questionsReceivedListener = null;
        scoreUpdateListener = null;
        gameEndListener = null;
        roomCreatedListener = null;
        errorListener = null;
        connectionListener = null;
    }
}