package com.github.andx2.controllers.goods;

import com.github.andx2.Application;
import com.github.andx2.storage.DAO.GoodDAOImpl;
import com.github.andx2.storage.DAO.UserDAOImpl;
import com.github.andx2.storage.pojo.Good;
import com.github.andx2.storage.pojo.User;
import com.github.andx2.utils.Log;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import spark.*;

import java.io.InputStream;

import static com.github.andx2.utils.Log.*;

public class GoodController {

    static Gson gson = new Gson();


    public static Route fetchAllGoods = (Request req, Response resp) -> {

//        Log.i("fetchAllGoods");

        int page = 1;
        int pageSize = 30;

        try {
            page = Integer.parseInt(req.queryParams("page"));
            pageSize = Integer.parseInt(req.queryParams("pageSize"));
        } catch (Exception e) {
            page = 1;
            pageSize = 30;
        }

        return gson.toJson(new GoodDAOImpl(Application.em).getList(page, pageSize));
//        return "mock response";
//       return gson.toJson(new GoodDAOImpl(Application.em).getGoodById(1));
    };

    public static Route fetchGoodById = (Request req, Response resp) -> {
        try {
            Long id = Long.parseLong(req.params(":id"));
            return gson.toJson(new GoodDAOImpl(Application.em).getGoodById(id));
        } catch (Exception e) {
            resp.status(404);
            return "Error bounds of array goods";
        }
    };

    public static Route postGood = (Request req, Response resp) -> {
        String token = req.headers("token");
        User user = new UserDAOImpl(Application.em).getUser(token);
        if (user == null || user.getRole().equals(User.ROLE_GUEST)) throw new Exception();
        try {
            InputStream is = req.raw().getInputStream();
            String tmp = IOUtils.toString(is, "UTF-8");
            Good good = gson.fromJson(tmp, Good.class);
            resp.status(201);
            if (!Good.isValid(good)) throw new Exception();
            return gson.toJson(new GoodDAOImpl(Application.em).putGood(good));
        } catch (Exception e) {
            resp.status(404);
            return "Good not added";
        }
    };

    public static Route updateGood = (Request req, Response resp) -> {
        try {
            Long id = Long.parseLong(req.params(":id"));
            InputStream is = req.raw().getInputStream();
            String tmp = IOUtils.toString(is, "UTF-8");
            Good good = gson.fromJson(tmp, Good.class);
            resp.status(201);
            if (!Good.isValid(good)) throw new Exception();
            return gson.toJson(new GoodDAOImpl(Application.em).updateGood(id, good));
        } catch (Exception e) {
            resp.status(404);
            return "Error bounds of array goods";
        }
    };

    public static Route deleteGoodById = (Request req, Response resp) -> {
        String token = req.headers("token");
        User user = new UserDAOImpl(Application.em).getUser(token);
        if (user == null || user.getRole().equals(User.ROLE_GUEST)) throw new Exception();
        try {
            Long id = Long.parseLong(req.params(":id"));
            Good good = new GoodDAOImpl(Application.em).getGoodById(id);
            if (good.getOwnerId() == user.getId()) throw new Exception();
            good = new GoodDAOImpl(Application.em).deleteGoodById(id);
            i("GoodController.deleteGoodById/" + id);
            return gson.toJson(good);
        } catch (Exception e) {
            resp.status(404);
            return "Good not deleted";
        }

    };


}

