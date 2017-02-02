package com.github.andx2.storage.DAO;

import com.github.andx2.storage.pojo.Good;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by savos on 30.10.2016.
 */

public class GoodDAOImpl implements GoodDAO {

    private EntityManager em;

    public GoodDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Good> getAll() {
        TypedQuery<Good> query =
                em.createQuery("SELECT p FROM Good p", Good.class);
        List<Good> results = query.getResultList();
        return results;
    }

    @Override
    public List<Good> getList(int page, int pageSize) {
        TypedQuery<Good> query =
                em.createQuery("SELECT p FROM Good p", Good.class);
        query.setFirstResult(pageSize * (page - 1));
        query.setMaxResults(pageSize);
        List<Good> results = query.getResultList();
        return results;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public long getCount() {
        Query query = em.createQuery("SELECT COUNT(p) FROM Good p");
        return Long.parseLong(query.getSingleResult() + "");
    }

    @Override
    public Good getGoodById(long id) {
        TypedQuery<Good> query =
                em.createQuery("SELECT p FROM Good p WHERE id = " + id + "", Good.class);
        Good result = query.getSingleResult();
        return result;
    }

    @Override
    public Good putGood(Good good) {
        try {
            em.getTransaction().begin();
            em.persist(good);
        } finally {
            em.getTransaction().commit();
        }
        return good;
    }

    @Override
    public Good updateGood(long id, Good good) {
        good.setId(id);
        try {
            em.getTransaction().begin();
            em.merge(good);
        } finally {
            em.getTransaction().commit();
        }
        return good;
    }

    @Override
    public Good deleteGoodById(long id) {
        Good good;
        try {
            em.getTransaction().begin();
            good = getGoodById(id);
            em.remove(good);
        } finally {
            em.getTransaction().commit();
        }

        return good;

    }
}
