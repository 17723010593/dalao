package com.s1243808733.aide.completion.translate;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "zh")
public class Table {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "source")
    private String source;

    @Column(name = "translation")
    private String translation;

    @Column(name = "state")
    private int state;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

}
    

