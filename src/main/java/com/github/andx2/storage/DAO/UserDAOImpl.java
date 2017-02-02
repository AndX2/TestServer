package com.github.andx2.storage.DAO;

import com.github.andx2.storage.pojo.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.github.andx2.utils.Log.*;

public class UserDAOImpl implements UserDAO {

    private EntityManager em;

    public UserDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public User getUser(String token) {
        String[] tokenArray = token.split("&");
        if (tokenArray.length == 3 && !tokenArray[1].isEmpty()) {
            return getUserById(tokenArray[1]);
        }
        return null;
    }

    @Override
    public User createUser() {
        User user = User.createUser();
        putUser(user);
        return user;
    }

    @Override
    public User getUserById(String id) {
        TypedQuery<User> query =
                em.createQuery("SELECT p FROM User p WHERE id = '" + id + "'", User.class);
        User result = null;
        try {
            result = query.getSingleResult();
        } catch (Exception e) {
            i("UserDAOImpl.getUserById user not found");
        }
        return result;
    }

    @Override
    public List<User> getAll() {
        TypedQuery<User> query =
                em.createQuery("SELECT p FROM User p", User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @Override
    public User putUser(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
        } finally {
            em.getTransaction().commit();
        }
        return user;
    }

    @Override
    public List<User> getUsersByRole(String role) {
        TypedQuery<User> query =
                em.createQuery("SELECT p FROM User p WHERE role = '" + role + "'", User.class);
        List<User> result = null;
        try {
            result = query.getResultList();
        } catch (Exception e) {
            i("UserDAOImpl.getUsersByRole user not found");
        }
        return result;
    }

    @Override
    public User updateUser(String id, User user) {
        user.setId(id);
        try {
            em.getTransaction().begin();
            em.merge(user);
        } finally {
            em.getTransaction().commit();
        }
        return user;
    }


}


