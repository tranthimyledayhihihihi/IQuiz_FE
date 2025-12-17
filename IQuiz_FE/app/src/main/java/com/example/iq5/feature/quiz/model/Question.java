package com.example.iq5.feature.quiz.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    // Text câu hỏi
    private String question_text;

    // Id đáp án đúng (tham chiếu tới option.id)
    private String correct_answer_id;

    // Id đáp án user đã chọn
    private String user_selected_answer_id;

    // Danh sách các lựa chọn
    private List<Option> options;

    // ===== GETTER / SETTER GỐC (GIỮ NGUYÊN) =====

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getCorrect_answer_id() {
        return correct_answer_id;
    }

    public void setCorrect_answer_id(String correct_answer_id) {
        this.correct_answer_id = correct_answer_id;
    }

    public String getUser_selected_answer_id() {
        return user_selected_answer_id;
    }

    public void setUser_selected_answer_id(String user_selected_answer_id) {
        this.user_selected_answer_id = user_selected_answer_id;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    // ===== HELPER CHO FLOW QUIZ =====

    /**
     * User đã chọn đáp án chưa?
     */
    public boolean hasUserAnswer() {
        return user_selected_answer_id != null && !user_selected_answer_id.trim().isEmpty();
    }

    /**
     * Kiểm tra user chọn có đúng không
     */
    public boolean isUserAnswerCorrect() {
        if (correct_answer_id == null || user_selected_answer_id == null) {
            return false;
        }
        return correct_answer_id.equals(user_selected_answer_id);
    }

    /**
     * Lấy Option tương ứng với id đáp án đúng
     */
    public Option getCorrectOption() {
        if (options == null || correct_answer_id == null) return null;
        for (Option o : options) {
            if (o != null && correct_answer_id.equals(o.getOption_id())) {
                return o;
            }
        }
        return null;
    }

    /**
     * Lấy Option tương ứng với id mà user chọn
     */
    public Option getUserSelectedOption() {
        if (options == null || user_selected_answer_id == null) return null;
        for (Option o : options) {
            if (o != null && user_selected_answer_id.equals(o.getOption_id())) {
                return o;
            }
        }
        return null;
    }

    /**
     * Reset lựa chọn của user (dùng khi chơi lại / review)
     */
    public void clearUserSelection() {
        this.user_selected_answer_id = null;
    }
}
