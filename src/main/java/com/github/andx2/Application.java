package com.github.andx2;


import com.github.andx2.controllers.goods.GoodController;
import com.github.andx2.controllers.trust.TrustController;
import com.github.andx2.controllers.users.UserController;
import com.github.andx2.email.MailSender;
import com.github.andx2.storage.DAO.UserDAOImpl;
import com.github.andx2.storage.Initializer;
import com.github.andx2.storage.pojo.Good;
import com.github.andx2.storage.pojo.Photo;
import com.github.andx2.storage.pojo.User;
import com.github.andx2.utils.DigestHelper;
import com.github.andx2.utils.ImageResizer;
import com.github.andx2.utils.Log;

import javax.persistence.EntityManager;
import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static com.github.andx2.AppConfig.*;
import static com.github.andx2.AppConfig.Web.*;
import static com.github.andx2.utils.FileUtils.*;
import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Application {

    public static EntityManager em;
    public static MailSender mailSender = new MailSender("mailsender.stub@gmail.com", "1234567890A");

    public static void main(String... args) {
        System.out.println("try Goods from link http://localhost:4000/api/goods/");

//        Configure Spark
        port(4000);
        threadPool(MAX_THREADS, MIN_THREADS, TIME_OUT_MILLISECONDS);
        enableDebugScreen();


//        Init DB
        em = Initializer.initConnection();
        Good good = new Good();
        good.setTitle("title1");
        good.setDescription("desc1");
        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            photos.add(new Photo("photo# " + i));
        }
        good.setPhotos(photos);
        Good good2 = new Good();
        good2.setTitle("title2");
        good2.setDescription("desc2");
        good2.setPhotos(photos);
        em.getTransaction().begin();
        em.persist(good);
        em.persist(good2);
        em.getTransaction().commit();

        List<User> usersAdmin = new UserDAOImpl(Application.em).getUsersByRole("admin");
        if (usersAdmin.size() > 0) {
            Log.i("Admin user token: " + usersAdmin.get(0).getToken());
        } else {
            User admin = User.createUser();
            admin.setRole(User.ROLE_ADMIN);
            new UserDAOImpl(Application.em).putUser(admin);
            Log.i("Admin is created. Admin user token: " + admin.getToken());
        }
        em.getTransaction().begin();
        em.persist(good);
        em.persist(good2);
        em.getTransaction().commit();

//        Init serve static files (images)
        File file = new File(IMAGES);
        file.mkdir();
        file.getAbsolutePath();
        staticFiles.externalLocation(file.getAbsolutePath());
//        Set time cashing (10 min)
        staticFiles.expireTime(600);
//        staticFiles.location("/images122816");

//        Setup filters before
        before(GOODS + "*", (request, response) -> {

            response.header("Content-Type", "application/json");

            boolean authenticated = false;
            String token = request.headers("token");
            User user;
            if (token == null || token.isEmpty()) {
                user = new UserDAOImpl(Application.em).createUser();
                response.header("token", user.getToken());
                authenticated = true;
            } else {
                user = new UserDAOImpl(Application.em).getUser(token);
            }
            if (user != null && user.isActive()) {
                authenticated = true;
            }

            // ... check if authenticated
            if (!authenticated) {
                halt(401, "You are not welcome here");
            }
        });

        before(USERS + "*", (request, response) -> {

            response.header("Content-Type", "application/json");

            boolean authenticated = false;
            String token = "";
            token = request.headers("token");
            User user = null;
            if (token != null && !token.isEmpty()) {
                user = new UserDAOImpl(Application.em).getUser(token);
//                Log.i("before. User role: " + user.getRole() + ", user is active: " + user.isActive());
            }
            if (user != null && user.isActive() && user.getRole().equals(User.ROLE_ADMIN)) {
                authenticated = true;
            }

            // ... check if authenticated
            if (!authenticated) {
                halt(403, "Forbidden for you role");
            }
        });

        before(UPLOAD + "*", (request, response) -> {
            response.header("Content-Type", "application/json");
            boolean authenticated = false;
            String token = "";
            token = request.headers("token");
            User user = null;
            if (token != null && !token.isEmpty()) {
                user = new UserDAOImpl(Application.em).getUser(token);
//                Log.i("before. User role: " + user.getRole() + ", user is active: " + user.isActive());
            }
            if (user != null && user.isActive() && (user.getRole().equals(User.ROLE_ADMIN) ||
                    user.getRole().equals(User.ROLE_USER) || user.getRole().equals(User.ROLE_BACKOFFICE))) {
                authenticated = true;
            }

            // ... check if authenticated
            if (!authenticated) {
                halt(403, "Forbidden for you role");
            }
        });


//        Setup filters after

//        Setup routes
        get(GOODS, GoodController.fetchAllGoods);
        get(GOODS + ":id", GoodController.fetchGoodById);
        post(GOODS, GoodController.postGood);
        put(GOODS + ":id", GoodController.updateGood);
        delete(GOODS + ":id", GoodController.deleteGoodById);

        get(USERS, UserController.fetchAllUsers);
        post(USERS, UserController.postUser);
        put(USERS + ":id", UserController.updateUser);

        post(TRUST, TrustController.trustEmail);
        get(TRUST + ":token", TrustController.accept);

        File uploadDir = new File("upload");
        // create the upload directory if it doesn't exist
        uploadDir.mkdir();
        post(UPLOAD, (req, res) -> {
            res.header("Content-Type", "application/json");
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            // getPart needs to use same "name" as input field in form
            String fileName = "";
            String fileNameExt = "";
            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) {
                fileName = getFileName(req.raw().getPart("uploaded_file"));
//                Log.i("fileName: " + fileName);
                String[] fileNameParts = fileName.split("\\.");
//                Log.i("file name parts " + fileNameParts.length);
                fileNameExt = "." + fileNameParts[fileNameParts.length - 1];
                fileName = DigestHelper.getUuid() + fileNameExt;
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
                tempFile = renameTo(tempFile, fileName);
                res.status(201);
                return "Uploaded file " + tempFile.getFileName();
            } catch (Exception e) {
                res.status(401);
                return "Upload file false" + e.toString();
            }
        });

//        Start resizer images
        ImageResizer resizer = new ImageResizer(PATH_UPLOAD, PATH_IMAGES);
        new Thread(resizer).start();
    }

}
