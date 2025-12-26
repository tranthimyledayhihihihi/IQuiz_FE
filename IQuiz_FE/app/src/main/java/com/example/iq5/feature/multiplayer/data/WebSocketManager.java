package com.example.iq5.feature.multiplayer.data;

import android.util.Log;

import com.example.iq5.core.network.models.ClientMessage;
import com.example.iq5.core.network.models.MatchFoundData;
import com.example.iq5.core.network.models.ServerMessage;

import com.example.iq5.feature.multiplayer.data.models.CauHoiDisplayModel;
import com.example.iq5.feature.multiplayer.data.models.GameResult;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ✅ FINAL - WebSocketManager (Android)
 * Fix:
 * - QUESTIONS parse đúng theo backend CauHoiDisplayModel
 * - Listener type đổi sang List<CauHoiDisplayModel>
 * - ScoreUpdate đọc linh hoạt Pascal/camel
 * - Log/validate để bắt lỗi map sai ngay lập tức
 */
public class WebSocketManager {
    private boolean connected = false;

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

    // ✅ FIX: đổi sang CauHoiDisplayModel
    public interface OnQuestionsReceivedListener {
        void onQuestionsReceived(List<CauHoiDisplayModel> questions);
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
        if (instance == null) instance = new WebSocketManager();
        return instance;
    }
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.disconnect();
            instance = null;
        }
    }

    // ================= CONNECT =================
    public void connect(String serverUrl, String token) {
        if (connected) {
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
                connected = true; // ⭐ THÊM
                if (connectionListener != null) connectionListener.onConnected();
            }


            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                Log.d(TAG, "📨 RAW: " + text);
                handleMessage(text);
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
                Log.e(TAG, "❌ WebSocket Error", t);
                connected = false;     // ⭐ THÊM
                WebSocketManager.this.webSocket = null;
                // ⭐ THÊM
                if (errorListener != null)
                    errorListener.onError(t.getMessage() != null ? t.getMessage() : "WebSocket error");
            }


            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                Log.d(TAG, "🔌 WebSocket Closed: " + reason);
                connected = false;     // ⭐ THÊM
                WebSocketManager.this.webSocket = null;
                // ⭐ THÊM
                if (connectionListener != null) connectionListener.onDisconnected();
            }

        });
    }

    // ================= MESSAGE HANDLER =================
    private void handleMessage(String json) {
        try {
            // Ưu tiên parse theo ServerMessage nếu bạn đang dùng class đó ổn định
            ServerMessage msg = gson.fromJson(json, ServerMessage.class);
            String type = msg != null ? msg.getType() : null;

            if (type == null) {
                // fallback: parse thủ công nếu ServerMessage không map được
                JsonObject root = gson.fromJson(json, JsonObject.class);
                type = getStringFlex(root, "Type", "type", null);
                JsonElement data = getElementFlex(root, "Data", "data");

                dispatch(type, data);
                return;
            }

            Log.d(TAG, "📩 Message Type: " + type);
            // msg.getData() thường là Object -> convert về JsonElement cho chắc
            JsonElement data = gson.toJsonTree(msg.getData());

            dispatch(type, data);

        } catch (Exception e) {
            Log.e(TAG, "❌ Parse error", e);
        }
    }

    private void dispatch(String type, JsonElement data) {
        if (type == null) {
            Log.w(TAG, "Unknown message: type is null");
            return;
        }

        switch (type) {
            case "MATCH_FOUND":
                handleMatchFound(data);
                break;

            case "QUESTIONS":
                handleQuestions(data);
                break;

            case "SCORE_UPDATE":
                handleScoreUpdate(data);
                break;

            case "GAME_END":
                handleGameEnd(data);
                break;

            case "ROOM_CREATED":
                handleRoomCreated(data);
                break;

            case "ERROR":
                handleError(data);
                break;

            case "ROOM_EXPIRED":
                handleRoomExpired(data);
                break;

            case "QUEUE_CANCELLED":
                Log.d(TAG, "Queue cancelled");
                break;

            default:
                Log.w(TAG, "Unknown message type: " + type);
                break;
            case "INFO":
                // hiển thị toast hoặc status
                if (errorListener != null) {
                    // hoặc tạo listener riêng, nhưng đơn giản dùng errorListener như thông báo
                    handleError(data); // hoặc viết handleInfo riêng
                }
                break;

        }
    }

    // ================= HANDLERS =================
    private void handleMatchFound(JsonElement data) {
        if (matchFoundListener == null) return;

        try {
            MatchFoundData d = gson.fromJson(data, MatchFoundData.class);
            matchFoundListener.onMatchFound(d.getMatchCode(), d.getOpponentId(), d.getYourRole());
        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing MATCH_FOUND", e);
        }
    }

    /**
     * ✅ QUESTIONS parse hoàn chỉnh:
     * - Data là JsonArray => parse list
     * - Nếu Data là JsonObject bọc array => tự tìm key phổ biến
     * - Validate dữ liệu: id/noiDung/cacLuaChon
     * - Fallback manual parse nếu map nghi ngờ
     */
    private void handleQuestions(JsonElement data) {
        if (questionsReceivedListener == null) {
            Log.w(TAG, "questionsReceivedListener is null - skip QUESTIONS");
            return;
        }

        try {
            if (data == null || data.isJsonNull()) {
                Log.e(TAG, "❌ QUESTIONS data is null");
                return;
            }

            JsonArray arr = null;

            // Case chuẩn: Data là array
            if (data.isJsonArray()) {
                arr = data.getAsJsonArray();
            }

            // Fallback: Data là object bọc array
            if (arr == null && data.isJsonObject()) {
                JsonObject obj = data.getAsJsonObject();
                arr = pickArray(obj, "questions", "Questions", "data", "Data", "items", "Items");
            }

            if (arr == null) {
                Log.e(TAG, "❌ QUESTIONS Data is not JsonArray. Payload=" + data.toString());
                return;
            }

            Type listType = new TypeToken<List<CauHoiDisplayModel>>() {}.getType();
            List<CauHoiDisplayModel> list = gson.fromJson(arr, listType);
            if (list == null) list = new ArrayList<>();

            // Validate tối thiểu để bắt mismatch
            if (!list.isEmpty()) {
                CauHoiDisplayModel first = list.get(0);

                boolean suspicious =
                        first.getCauHoiID() == 0 &&
                                (isBlank(first.getNoiDung())) &&
                                (isBlank(first.getCacLuaChon()));

                if (suspicious) {
                    Log.w(TAG, "⚠️ QUESTIONS suspicious mapping. Fallback manual parse...");
                    list = manualParseQuestions(arr);
                }
            }

            Log.d(TAG, "✅ Parsed QUESTIONS size=" + list.size());
            if (!list.isEmpty()) {
                Log.d(TAG, "✅ FirstQ id=" + list.get(0).getCauHoiID()
                        + " | text=" + safe(list.get(0).getNoiDung()));
            }

            questionsReceivedListener.onQuestionsReceived(list);

        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing QUESTIONS", e);
        }
    }

    private void handleScoreUpdate(JsonElement data) {
        if (scoreUpdateListener == null) return;

        try {
            if (data == null || data.isJsonNull() || !data.isJsonObject()) return;

            JsonObject j = data.getAsJsonObject();

            int userId = getIntFlex(j, "userId", "UserId", "userid", "UserID", 0);
            int questionId = getIntFlex(j, "questionId", "QuestionId", "questionID", "QuestionID", 0);
            boolean correct = getBoolFlex(j, "correct", "Correct", false);

            scoreUpdateListener.onScoreUpdate(userId, questionId, correct);

        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing SCORE_UPDATE", e);
        }
    }

    private void handleGameEnd(JsonElement data) {
        if (gameEndListener == null) return;

        try {
            GameResult result = gson.fromJson(data, GameResult.class);
            Log.d(TAG, "✅ Game End - Result: " + result.getKetQua() + ", Winner ID: " + result.getWinnerUserId());
            gameEndListener.onGameEnd(result);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing GAME_END", e);
        }
    }

    private void handleRoomCreated(JsonElement data) {
        if (roomCreatedListener == null) return;

        try {
            JsonObject j = data != null && data.isJsonObject() ? data.getAsJsonObject() : new JsonObject();
            String roomCode = getStringFlex(j, "roomCode", "RoomCode", null);
            if (roomCode != null) roomCreatedListener.onRoomCreated(roomCode);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing ROOM_CREATED", e);
        }
    }

    private void handleError(JsonElement data) {
        if (errorListener == null) return;

        try {
            JsonObject j = data != null && data.isJsonObject() ? data.getAsJsonObject() : new JsonObject();
            String message = getStringFlex(j, "message", "Message", "Lỗi không xác định");
            errorListener.onError(message);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing ERROR", e);
        }
    }

    private void handleRoomExpired(JsonElement data) {
        if (errorListener == null) return;

        try {
            JsonObject j = data != null && data.isJsonObject() ? data.getAsJsonObject() : new JsonObject();
            String message = getStringFlex(j, "message", "Message", "Phòng đã hết hạn");
            errorListener.onError(message);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing ROOM_EXPIRED", e);
        }
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
    public void findRandomMatch() { send("FIND_MATCH", null); }

    public void cancelQueue() { send("CANCEL_QUEUE", null); }

    public void createRoom() { send("CREATE_ROOM", null); }

    public void joinRoomWithCode(String roomCode) {
        Map<String, String> data = new HashMap<>();
        data.put("roomCode", roomCode);
        send("JOIN_ROOM_CODE", data);
    }

    public void joinMatch(String matchCode) {
        Map<String, String> data = new HashMap<>();
        data.put("matchCode", matchCode);
        send("JOIN_MATCH", data);
    }

    public void submitAnswer(String matchCode, int questionId, String answer) {
        Map<String, Object> data = new HashMap<>();
        data.put("matchCode", matchCode);
        data.put("questionId", questionId);
        data.put("selectedAnswer", answer);
        send("SUBMIT_ANSWER", data);
    }

    // ================= UTILS =================
    public boolean isConnected() {
        return connected;
    }


    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "User closed connection");
            webSocket = null;
        }
        connected = false; // ⭐ THÊM
        Log.d(TAG, "🔌 Disconnected");
    }


    // ================= SETTERS =================
    public void setOnMatchFoundListener(OnMatchFoundListener l) { matchFoundListener = l; }

    public void setOnQuestionsReceivedListener(OnQuestionsReceivedListener l) { questionsReceivedListener = l; }

    public void setOnScoreUpdateListener(OnScoreUpdateListener l) { scoreUpdateListener = l; }

    public void setOnGameEndListener(OnGameEndListener l) { gameEndListener = l; }

    public void setOnRoomCreatedListener(OnRoomCreatedListener l) { roomCreatedListener = l; }

    public void setOnErrorListener(OnErrorListener l) { errorListener = l; }

    public void setOnConnectionListener(OnConnectionListener l) { connectionListener = l; }

    public void clearAllListeners() {
        matchFoundListener = null;
        questionsReceivedListener = null;
        scoreUpdateListener = null;
        gameEndListener = null;
        roomCreatedListener = null;
        errorListener = null;
        connectionListener = null;
    }

    // ================= HELPERS =================
    private JsonArray pickArray(JsonObject obj, String... keys) {
        for (String k : keys) {
            if (obj.has(k) && obj.get(k).isJsonArray()) return obj.getAsJsonArray(k);
        }
        return null;
    }

    private List<CauHoiDisplayModel> manualParseQuestions(JsonArray arr) {
        List<CauHoiDisplayModel> list = new ArrayList<>();
        for (JsonElement e : arr) {
            if (!e.isJsonObject()) continue;
            JsonObject o = e.getAsJsonObject();

            CauHoiDisplayModel q = new CauHoiDisplayModel();
            q.setCauHoiID(getIntFlex(o, "CauHoiID", "cauHoiID", "cauHoiId", "CauHoiId", 0));
            q.setNoiDung(getStringFlex(o, "NoiDung", "noiDung", ""));
            q.setCacLuaChon(getStringFlex(o, "CacLuaChon", "cacLuaChon", "{}"));
            q.setThuTuTrongTranDau(getIntFlex(o, "ThuTuTrongTranDau", "thuTuTrongTranDau", 0));
            q.setThoiGianToiDa(getDoubleFlex(o, "ThoiGianToiDa", "thoiGianToiDa", 15.0));

            Integer chuDe = getIntFlexNullable(o, "ChuDeID", "chuDeID", "chuDeId", "ChuDeId");
            q.setChuDeID(chuDe);

            list.add(q);
        }
        return list;
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private String safe(String s) { return s == null ? "" : s; }

    private JsonElement getElementFlex(JsonObject o, String a, String b) {
        if (o == null) return null;
        if (o.has(a)) return o.get(a);
        if (o.has(b)) return o.get(b);
        return null;
    }

    private String getStringFlex(JsonObject o, String a, String b, String def) {
        try { if (o != null && o.has(a) && !o.get(a).isJsonNull()) return o.get(a).getAsString(); } catch (Exception ignored) {}
        try { if (o != null && o.has(b) && !o.get(b).isJsonNull()) return o.get(b).getAsString(); } catch (Exception ignored) {}
        return def;
    }

    private int getIntFlex(JsonObject o, String a, String b, int def) {
        try { if (o.has(a) && !o.get(a).isJsonNull()) return o.get(a).getAsInt(); } catch (Exception ignored) {}
        try { if (o.has(b) && !o.get(b).isJsonNull()) return o.get(b).getAsInt(); } catch (Exception ignored) {}
        return def;
    }

    private int getIntFlex(JsonObject o, String a, String b, String c, String d, int def) {
        try { if (o.has(a) && !o.get(a).isJsonNull()) return o.get(a).getAsInt(); } catch (Exception ignored) {}
        try { if (o.has(b) && !o.get(b).isJsonNull()) return o.get(b).getAsInt(); } catch (Exception ignored) {}
        try { if (o.has(c) && !o.get(c).isJsonNull()) return o.get(c).getAsInt(); } catch (Exception ignored) {}
        try { if (o.has(d) && !o.get(d).isJsonNull()) return o.get(d).getAsInt(); } catch (Exception ignored) {}
        return def;
    }

    private Integer getIntFlexNullable(JsonObject o, String a, String b, String c, String d) {
        try { if (o.has(a) && !o.get(a).isJsonNull()) return o.get(a).getAsInt(); } catch (Exception ignored) {}
        try { if (o.has(b) && !o.get(b).isJsonNull()) return o.get(b).getAsInt(); } catch (Exception ignored) {}
        try { if (o.has(c) && !o.get(c).isJsonNull()) return o.get(c).getAsInt(); } catch (Exception ignored) {}
        try { if (o.has(d) && !o.get(d).isJsonNull()) return o.get(d).getAsInt(); } catch (Exception ignored) {}
        return null;
    }

    private double getDoubleFlex(JsonObject o, String a, String b, double def) {
        try { if (o.has(a) && !o.get(a).isJsonNull()) return o.get(a).getAsDouble(); } catch (Exception ignored) {}
        try { if (o.has(b) && !o.get(b).isJsonNull()) return o.get(b).getAsDouble(); } catch (Exception ignored) {}
        return def;
    }

    private boolean getBoolFlex(JsonObject o, String a, String b, boolean def) {
        try { if (o.has(a) && !o.get(a).isJsonNull()) return o.get(a).getAsBoolean(); } catch (Exception ignored) {}
        try { if (o.has(b) && !o.get(b).isJsonNull()) return o.get(b).getAsBoolean(); } catch (Exception ignored) {}
        return def;
    }
}
