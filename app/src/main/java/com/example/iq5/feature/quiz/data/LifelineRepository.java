package com.example.iq5.feature.quiz.data;

import com.example.iq5.feature.quiz.model.Lifeline;
import java.util.ArrayList;
import java.util.List;

public class LifelineRepository {

    // ...
    // ...
    public List<Lifeline> getDefaultLifelines() {
        List<Lifeline> list = new ArrayList<>();

        // Sửa tham số thứ hai thành giá trị boolean 'false'
        list.add(new Lifeline("5050", false)); // KHẮC PHỤC LỖI
        list.add(new Lifeline("Hint", false));  // KHẮC PHỤC LỖI
        list.add(new Lifeline("Skip", false));  // KHẮC PHỤC LỖI

        return list;
    }
// ...
}
