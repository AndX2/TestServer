package com.github.andx2.storage;

import com.github.andx2.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static com.github.andx2.AppConfig.Storage.*;

/**
 * Created by savos on 29.10.2016.
 */
public class Initializer {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public static EntityManager initConnection() {
        Log.i("Initializer : initConnection ");
        entityManagerFactory =
                Persistence.createEntityManagerFactory("$objectdb/db/goodsNew7.odb");
        Log.i("Initializer : initConnection entityManagerFactory = " + entityManagerFactory.toString());
        return entityManager = entityManagerFactory.createEntityManager();
    }

    public static void closeConnection() {
        entityManagerFactory.close();
        entityManager.close();
    }


}
