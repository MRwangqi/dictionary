package com.dictionary.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/21.
 */

public class WordModel implements Serializable {
    private String id;
    private String words;
    private String meaning;
    private String lx;
    private String flag;
    private boolean finish;

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    @Override
    public String toString() {
        return "WordModel{" +
                "id='" + id + '\'' +
                ", words='" + words + '\'' +
                ", meaning='" + meaning + '\'' +
                ", lx='" + lx + '\'' +
                ", flag='" + flag + '\'' +
                ", finish=" + finish +
                '}';
    }
}
