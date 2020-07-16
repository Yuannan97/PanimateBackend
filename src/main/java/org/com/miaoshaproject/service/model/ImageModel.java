package org.com.miaoshaproject.service.model;

public class ImageModel {
    private String name;
    private String type;
    private String image;
    private Integer id;
    private Integer imageId;

    public ImageModel() {

        //super();
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ImageModel(String name, String type, String image) {
        this.name = name;
        this.type =type;
        this.image = image;
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

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
