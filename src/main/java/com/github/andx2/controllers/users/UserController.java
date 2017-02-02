package com.github.andx2.controllers.users;

import com.github.andx2.Application;
import com.github.andx2.storage.DAO.UserDAOImpl;
import com.github.andx2.storage.pojo.User;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.InputStream;

/**
 * Created by savos on 30.10.2016.
 */
public class UserController {

    static Gson gson = new Gson();


    public static Route fetchAllUsers = (Request req, Response resp) -> {

        return gson.toJson(new UserDAOImpl(Application.em).getAll());
    };

    public static Route postUser = (Request req, Response resp) -> {
        try {
            InputStream is = req.raw().getInputStream();
            String tmp = IOUtils.toString(is, "UTF-8");
            User user = gson.fromJson(tmp, User.class);
            resp.status(201);
            if (!User.isValid(user)) throw new Exception();
            return gson.toJson(new UserDAOImpl(Application.em).putUser(user));
        } catch (Exception e) {
            resp.status(404);
            return "Bad send user";
        }
    };

    public static Route updateUser = (Request req, Response resp) -> {
        try {
            String id = String.valueOf(req.params(":id"));
            InputStream is = req.raw().getInputStream();
            String tmp = IOUtils.toString(is, "UTF-8");
            User user = gson.fromJson(tmp, User.class);
            resp.status(201);
            if (!User.isValid(user)) throw new Exception();
            return gson.toJson(new UserDAOImpl(Application.em).updateUser(id, user));
        } catch (Exception e) {
            resp.status(404);
            return "Error bounds of array goods";
        }
    };


}
