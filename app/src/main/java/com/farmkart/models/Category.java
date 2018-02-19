package com.farmkart.models;

/**
 * Created by kumar on 2/4/2018.
 */

public class Category {

    private String name;

    private String image;

    private String type;

    public Category(){

    }

    public Category(String name, String image, String type) {
        this.name = name;
        this.image = image;
        this.type = type;
    }

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
