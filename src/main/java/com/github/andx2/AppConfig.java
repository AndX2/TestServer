package com.github.andx2;

/**
 * Created by savos on 29.10.2016.
 */
public interface AppConfig {

    int MAX_THREADS = 30;
    int MIN_THREADS = 2;
    int TIME_OUT_MILLISECONDS = 5000;
    String PATH_UPLOAD = "upload";
    String PATH_IMAGES = "images";

    interface Web {
        String GOODS = "/api/goods/";
        String USERS = "/api/users/";
        String UPLOAD = "/api/upload/";
        String IMAGES = "images";
        String TRUST = "/api/trust/";
    }

    interface Storage {
        String DRIVER = "org.postgresql.Driver";
        String URL = "jdbc:postgresql://localhost:5432/skillbranch";
        String LOGIN = "sb_client";
        String PASS = "123456789";
    }

}
