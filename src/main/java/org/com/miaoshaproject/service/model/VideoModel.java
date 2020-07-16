package org.com.miaoshaproject.service.model;

public class VideoModel {
    private String name;
    private String type;
    private String video;
    private String time;
    private Integer id;
    private Integer VideoId;

    public VideoModel(String name, String type, String video, String time) {
        this.name = name;
        this.type = type;
        this.video = video;
        this.time = time;
    }

    public VideoModel() {
        super();
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public Integer getVideoId() {
        return VideoId;
    }

    public void setVideoId(Integer videoId) {
        VideoId = videoId;
    }
}
