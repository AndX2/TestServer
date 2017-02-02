package com.github.andx2.storage.pojo;

import javax.persistence.*;
import java.util.List;

@Entity
public class Good {

    @Id
    @GeneratedValue
    @Column(name = "GOOD_ID")
    private long id;
    private String title;
    private String description;
    private float cost;
    private String ownerId;
    @ElementCollection
    @CollectionTable(
            name = "GOOD_PHOTO",
            joinColumns = @JoinColumn(name = "OWNER_ID")
    )
    private List<Photo> photos;

    public Good() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<Photo> getPhotos() {
        return photos;
    }


    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public static boolean isValid(Good good) {
        if (good.getTitle().isEmpty() || good.getTitle().length() == 0 ||
                good.getOwnerId().isEmpty() || good.getOwnerId().length() == 0) return false;
        return true;
    }
}
