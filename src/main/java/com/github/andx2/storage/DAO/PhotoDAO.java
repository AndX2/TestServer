package com.github.andx2.storage.DAO;

import com.github.andx2.storage.pojo.Photo;

import java.util.List;

/**
 * Created by savos on 30.10.2016.
 */
public interface PhotoDAO {

    List<Photo> getAll();

    List<Photo> getPhotoById();

    List<Photo> getPhotoByGoodId();

    void putPhoto(Photo photo);

    void putPhoto(List<Photo> photos);
}
