package org.com.miaoshaproject.service.model;

public class AudioModel {

    private String name;
    private String type;
    private String audio;
    private String time;
    private Integer id;
    private Integer audioId;

    public AudioModel(){super();}

    public AudioModel(String name, String type, String audio, String time) {
        this.name = name;
        this.type = type;
        this.audio = audio;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
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

    public Integer getAudioId() {
        return audioId;
    }

    public void setAudioId(Integer audioId) {
        this.audioId = audioId;
    }
}
