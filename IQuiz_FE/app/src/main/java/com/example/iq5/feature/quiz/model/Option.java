package com.example.iq5.feature.quiz.model;

import java.io.Serializable;

public class Option implements Serializable {
    private String option_id;
    private String text;
    private boolean hidden = false;

    public Option() { }

    public Option(String option_id, String text) {
        this.option_id = option_id;
        this.text = text;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_text() {
        return text;
    }

    public void setOption_text(String option_text) {
        this.text = option_text;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
