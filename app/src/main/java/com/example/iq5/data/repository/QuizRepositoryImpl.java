package com.example.iq5.data.repository;

import androidx.lifecycle.LiveData;
import com.example.iq5.core.db.dao.QuestionDao;
import com.example.iq5.core.db.entity.QuestionLocalEntity;
import com.example.iq5.core.db.entity.TopicLocalEntity;
import com.example.iq5.core.network.QuizApiService;
import com.example.iq5.data.model.QuestionDto;
import com.example.iq5.data.model.TopicDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;

public class QuizRepositoryImpl implements QuizRepository {

    private final QuizApiService apiService;
    private final QuestionDao questionDao; // Đã bao gồm logic cho cả Topic và Question DAO

    public QuizRepositoryImpl(QuizApiService apiService, QuestionDao questionDao) {
        this.apiService = apiService;
        this.questionDao = questionDao;
    }

    // --- REMOTE METHODS (API) ---

    @Override
    public Call<List<TopicDto>> fetchTopicsFromRemote() {
        return apiService.getTopics();
    }

    @Override
    public Call<List<QuestionDto>> fetchQuestionsByTopicRemote(int topicId) {
        return apiService.getQuestionsByTopic(topicId);
    }

    // --- LOCAL METHODS (ROOM) ---

    @Override
    public LiveData<List<TopicLocalEntity>> getCachedTopics() {
        return questionDao.getAllTopics();
    }

    @Override
    public LiveData<List<QuestionLocalEntity>> getCachedQuestionsByTopic(int topicId) {
        return questionDao.getQuestionsByTopic(topicId);
    }

    @Override
    public void saveTopicsToLocal(List<TopicDto> topics) {
        // Cần thực hiện thao tác Room trên luồng I/O
        Executors.newSingleThreadExecutor().execute(() -> {
            List<TopicLocalEntity> entities = new ArrayList<>();
            for (TopicDto dto : topics) {
                // Ánh xạ DTO sang Entity để lưu vào Room
                entities.add(new TopicLocalEntity(dto.getChuDeId(), dto.getTenChuDe(), null));
            }
            questionDao.insertTopics(entities);
        });
    }

    @Override
    public void saveQuestionsToLocal(List<QuestionDto> questions) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<QuestionLocalEntity> entities = new ArrayList<>();
            for (QuestionDto dto : questions) {
                // Ánh xạ DTO sang Entity (Logic ánh xạ đầy đủ cần được thêm vào đây)
                entities.add(new QuestionLocalEntity(
                        dto.getCauHoiId(),
                        dto.getChuDeId(),
                        dto.getNoiDung(),
                        dto.getLuaChonA(),
                        dto.getLuaChonB(),
                        dto.getLuaChonC(),
                        dto.getLuaChonD(),
                        dto.getDapAn(),
                        "Unknown"
                ));
            }
            questionDao.insertAllQuestions(entities);
        });
    }
}