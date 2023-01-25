package com.example.class3demo2.model;

import java.util.List;

class TranslateResponse {
    private Data data;

    public Data getData() {
        return data;
    }
}

class Data {
    private List<Translation> translations;

    public List<Translation> getTranslations() {
        return translations;
    }
}

class Translation {
    private String translatedText;

    public String getTranslatedText() {
        return translatedText;
    }
}

