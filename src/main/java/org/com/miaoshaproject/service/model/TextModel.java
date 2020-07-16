package org.com.miaoshaproject.service.model;

public class TextModel {

    private String name;
    private String type;
    private String text;
    private String time;
    private Integer id;
    private Integer TextId;

    public TextModel() {
        super();
    }

    public TextModel(String name, String type, String text, String time) {
        this.name = name;
        this.type = type;
        this.text = text;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTextId() {
        return TextId;
    }

    public void setTextId(Integer textId) {
        TextId = textId;
    }
}
