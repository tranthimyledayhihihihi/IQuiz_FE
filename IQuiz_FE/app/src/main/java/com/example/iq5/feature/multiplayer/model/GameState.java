package com.example.iq5.feature.multiplayer.model;

import com.example.iq5.feature.multiplayer.data.models.Question;
import java.util.List;

public class GameState {
    private String matchCode;
    private int myUserId;
    private int opponentId;
    private String myRole; // Player1 or Player2

    private List<Question> questions;
    private int currentQuestionIndex;

    private int myScore;
    private int opponentScore;

    private boolean isMyTurn;
    private boolean isGameEnded;

    private long startTime;
    private int timeRemaining;

    public GameState() {
        this.currentQuestionIndex = 0;
        this.myScore = 0;
        this.opponentScore = 0;
        this.isGameEnded = false;
        this.timeRemaining = 15;
    }

    // Getters and Setters
    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public int getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(int myUserId) {
        this.myUserId = myUserId;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }

    public String getMyRole() {
        return myRole;
    }

    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public Question getCurrentQuestion() {
        if (questions != null && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public void nextQuestion() {
        this.currentQuestionIndex++;
    }

    public boolean hasMoreQuestions() {
        return questions != null && currentQuestionIndex < questions.size();
    }

    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public void addMyScore(int points) {
        this.myScore += points;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public void addOpponentScore(int points) {
        this.opponentScore += points;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public void resetTimer() {
        this.timeRemaining = 15;
        this.startTime = System.currentTimeMillis();
    }

    public int getTotalQuestions() {
        return questions != null ? questions.size() : 0;
    }

    public String getGameResult() {
        if (myScore > opponentScore) {
            return "WIN";
        } else if (myScore < opponentScore) {
            return "LOSE";
        } else {
            return "DRAW";
        }
    }
}