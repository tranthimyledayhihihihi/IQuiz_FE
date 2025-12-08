package com.example.iq5.feature.multiplayer.data;

import com.example.iq5.feature.multiplayer.model.Question;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionRepository {

    public static List<Question> getAllQuestions() {
        // Tạo danh sách 10 câu hỏi mẫu
        List<Question> questions = Arrays.asList(
                // Câu 1 (Đúng: A)
                new Question("Thủ đô của Việt Nam là gì?", "A",
                        Arrays.asList("A. Hà Nội", "B. TP. HCM", "C. Đà Nẵng", "D. Hải Phòng")),
                // Câu 2 (Đúng: C)
                new Question("Ai là tác giả của Truyện Kiều?", "C",
                        Arrays.asList("A. Hồ Xuân Hương", "B. Nguyễn Trãi", "C. Nguyễn Du", "D. Cao Bá Quát")),
                // Câu 3 (Đúng: D)
                new Question("Nguyên tố có ký hiệu là Fe?", "D",
                        Arrays.asList("A. Vàng", "B. Bạc", "C. Đồng", "D. Sắt")),
                // Câu 4 (Đúng: B)
                new Question("Đỉnh núi cao nhất Việt Nam?", "B",
                        Arrays.asList("A. Tam Đảo", "B. Fansipan", "C. Bạch Mã", "D. Lang Biang")),

                // Câu 5 (Bổ sung - Đúng: A)
                new Question("Trong bóng đá, 'Cầu thủ thứ 12' thường được dùng để chỉ ai?", "A",
                        Arrays.asList("A. Khán giả", "B. Trọng tài", "C. Huấn luyện viên", "D. Cầu thủ dự bị")),

                // Câu 6 (Bổ sung - Đúng: B)
                new Question("Năm 1945, Việt Nam giành được độc lập vào tháng nào?", "B",
                        Arrays.asList("A. Tháng Bảy", "B. Tháng Tám", "C. Tháng Chín", "D. Tháng Mười")),

                // Câu 7 (Bổ sung - Đúng: C)
                new Question("Môn học nào nghiên cứu về các loại hình kinh tế và thị trường?", "C",
                        Arrays.asList("A. Triết học", "B. Xã hội học", "C. Kinh tế học", "D. Chính trị học")),

                // Câu 8 (Bổ sung - Đúng: D)
                new Question("Hành tinh nào gần Mặt Trời nhất?", "D",
                        Arrays.asList("A. Trái Đất", "B. Sao Kim", "C. Sao Hỏa", "D. Sao Thủy")),

                // Câu 9 (Bổ sung - Đúng: A)
                new Question("Thành phố nào được mệnh danh là 'Thành phố đáng sống' ở Việt Nam?", "A",
                        Arrays.asList("A. Đà Nẵng", "B. Nha Trang", "C. Huế", "D. Cần Thơ")),

                // Câu 10 (Bổ sung - Đúng: B)
                new Question("Trong khoa học, H2O là công thức hóa học của chất gì?", "B",
                        Arrays.asList("A. Muối ăn", "B. Nước", "C. Khí Carbonic", "D. Đường"))
        );

        // Xáo trộn danh sách câu hỏi trước khi trả về
        Collections.shuffle(questions);
        return questions;
    }
}