package com.github.andx2.storage.pojo;

import com.github.andx2.utils.DigestHelper;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String id;
    private String email;
    private String name;
    private String token;
    private String role;
    private boolean active;

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_BACKOFFICE = "back";
    public static final String ROLE_USER = "user";
    public static final String ROLE_GUEST = "guest";

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static User createUser() {
        User user = new User();
        user.setId(DigestHelper.getUuid());
        user.setToken(createToken(user));
        user.setRole(User.ROLE_GUEST);
        user.setActive(true);
        return user;
    }

    public static boolean isValid(User user) {
        boolean valid = false;
        String role = user.getRole();
        if (role.equals(User.ROLE_ADMIN) || role.equals(User.ROLE_BACKOFFICE) ||
                role.equals(User.ROLE_GUEST) || role.equals(User.ROLE_USER)) valid = true;
        user.setId(DigestHelper.getUuid());
        user.setToken(createToken(user));
        return valid;
    }

    private static String createToken(User user) {
        return "v1" + "&" + user.getId() + "&" + DigestHelper.getUuid() + DigestHelper.getUuid();
    }
}
