package com.github.andx2.storage.DAO;

import com.github.andx2.storage.pojo.Good;

import java.util.List;

/**
 * Created by savos on 30.10.2016.
 */
public interface GoodDAO {

    List<Good> getAll();

    List<Good> getList(int page, int pageSize);

    void deleteAll();

    long getCount();

    Good getGoodById(long id);

    Good putGood(Good good);

    Good updateGood(long id, Good good);

    Good deleteGoodById(long id);

}
