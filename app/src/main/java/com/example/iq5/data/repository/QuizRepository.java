package com.example.iq5.data.repository;

import androidx.lifecycle.LiveData;
import com.example.iq5.core.db.entity.QuestionLocalEntity;
import com.example.iq5.core.db.entity.TopicLocalEntity;
import com.example.iq5.data.model.QuestionDto;
import com.example.iq5.data.model.TopicDto;

import java.util.List;

import retrofit2.Call;

public interface QuizRepository {

    /**
     * Lấy danh sách Chủ đề từ API.
     * @return Call<List<TopicDto>> Phản hồi từ mạng.
     */
    Call<List<TopicDto>> fetchTopicsFromRemote();

    /**
     * Lấy danh sách Chủ đề đã cache từ Room.
     * @return LiveData<List<TopicLocalEntity>> Dữ liệu cục bộ theo thời gian thực.
     */
    LiveData<List<TopicLocalEntity>> getCachedTopics();

    /**
     * Lấy danh sách Câu hỏi theo ID Chủ đề từ API.
     * @param topicId ID của Chủ đề.
     * @return Call<List<QuestionDto>> Phản hồi từ mạng.
     */
    Call<List<QuestionDto>> fetchQuestionsByTopicRemote(int topicId);

    /**
     * Lấy danh sách Câu hỏi đã cache từ Room.
     * @param topicId ID của Chủ đề.
     * @return LiveData<List<QuestionLocalEntity>> Dữ liệu cục bộ theo thời gian thực.
     */
    LiveData<List<QuestionLocalEntity>> getCachedQuestionsByTopic(int topicId);

    /**
     * Lưu danh sách Chủ đề vào Room (Cache).
     */
    void saveTopicsToLocal(List<TopicDto> topics);

    /**
     * Lưu danh sách Câu hỏi vào Room (Cache).
     */
    void saveQuestionsToLocal(List<QuestionDto> questions);
}