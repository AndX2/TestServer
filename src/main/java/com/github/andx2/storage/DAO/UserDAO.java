package com.github.andx2.storage.DAO;

import com.github.andx2.storage.pojo.User;

import java.util.List;

/**
 * Created by savos on 30.10.2016.
 */
public interface UserDAO {
    User getUser(String token);

    User createUser();

    User getUserById(String id);

    List<User> getAll();

    User putUser(User user);

    List<User> getUsersByRole(String role);

    User updateUser(String id, User user);
}
