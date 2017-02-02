package com.github.andx2.storage.pojo;

import javax.persistence.*;


@Embeddable
public class Photo {
    //    @Id
//    @GeneratedValue
//    private long id;
//    private Good good_id;
    private String link;

    public Photo() {
    }

    public Photo(String link) {
        this.link = link;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public Good getGood_id() {
//        return good_id;
//    }
//
//    public void setGood_id(Good good_id) {
//        this.good_id = good_id;
//    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
