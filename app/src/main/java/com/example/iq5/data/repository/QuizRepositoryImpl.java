package com.example.iq5.data.repository;

import androidx.lifecycle.LiveData;
import com.example.iq5.core.db.dao.QuestionDao;
import com.example.iq5.core.db.dao.TopicDao; // <-- IMPORT
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
    private final QuestionDao questionDao;
    private final TopicDao topicDao; // <<< THÊM: Tham chiếu TopicDao

    // Sửa Constructor để nhận TopicDao
    public QuizRepositoryImpl(QuizApiService apiService, QuestionDao questionDao, TopicDao topicDao) {
        this.apiService = apiService;
        this.questionDao = questionDao;
        this.topicDao = topicDao;
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
    // Đã sửa kiểu trả về và gọi phương thức TopicDao
    public LiveData<List<TopicLocalEntity>> getCachedTopics() {
        return topicDao.getAllTopics();
    }

    @Override
    public LiveData<List<QuestionLocalEntity>> getCachedQuestionsByTopic(int topicId) {
        return questionDao.getQuestionsByTopic(topicId);
    }

    @Override
    public void saveTopicsToLocal(List<TopicDto> topics) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<TopicLocalEntity> entities = new ArrayList<>();
            for (TopicDto dto : topics) {
                // Ánh xạ DTO sang Entity
                // LƯU Ý: Sửa thành constructor 2 tham số (id, name)
                entities.add(new TopicLocalEntity(dto.getChuDeId(), dto.getTenChuDe()));
            }
            // SỬA: Gọi từ topicDao
            topicDao.insertTopics(entities);
        });
    }

    @Override
    public void saveQuestionsToLocal(List<QuestionDto> questions) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<QuestionLocalEntity> entities = new ArrayList<>();
            for (QuestionDto dto : questions) {
                // Ánh xạ DTO sang Entity
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