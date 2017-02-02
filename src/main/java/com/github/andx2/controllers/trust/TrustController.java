package com.github.andx2.controllers.trust;

import com.github.andx2.Application;
import com.github.andx2.email.MailSender;
import com.github.andx2.storage.DAO.TrustMessageDAOImpl;
import com.github.andx2.storage.DAO.UserDAOImpl;
import com.github.andx2.storage.pojo.TrustMessage;
import com.github.andx2.storage.pojo.User;
import com.github.andx2.utils.DigestHelper;
import com.github.andx2.utils.TextValueValidator;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.InputStream;

public class TrustController {

    private static MailSender mailSender = Application.mailSender;
    static Gson gson = new Gson();

    public static Route trustEmail = (Request req, Response resp) -> {
        try {
            InputStream is = req.raw().getInputStream();
            String tmp = IOUtils.toString(is, "UTF-8");
            User user = gson.fromJson(tmp, User.class);
            resp.status(201);
            String token = req.headers("token");
            User existUser = new UserDAOImpl(Application.em).getUser(token);
            if (existUser == null) throw new Exception();
            if (!User.isValid(user)) throw new Exception();
            if (!TextValueValidator.validate(TextValueValidator.Type.Email, user.getEmail())) throw new Exception();
            TrustMessage message = new TrustMessage();
            message.seteMail(user.getEmail());
            message.setTokenUser(token);
            message.setTrustToken(DigestHelper.getUuid() + DigestHelper.getUuid());
            mailSender.send("Trust you eMail", "Brouse this link " + "http://localhost:4000/api/trust/" +
                    message.getTrustToken(), "mailsender.stub@gmail.com", message.geteMail());
            new TrustMessageDAOImpl(Application.em).putMessage(message);
            return "Email send to " + user.getEmail();
        } catch (Exception e) {
            resp.status(404);
            return "Bad send user";
        }
    };

    public static Route accept = (Request req, Response resp) -> {
        try {
            resp.status(200);
            String token = String.valueOf(req.params(":token"));
            TrustMessage message = new TrustMessageDAOImpl(Application.em).getMessageByToken(token);
            if (message != null) {
                User user = new UserDAOImpl(Application.em).getUser(message.getTokenUser());
                user.setEmail(message.geteMail());
                user.setRole(User.ROLE_USER);
                new UserDAOImpl(Application.em).updateUser(user.getId(), user);
                new TrustMessageDAOImpl(Application.em).deleteMessageByToken(token);
            } else throw new Exception();
            return "You eMail is accepted!";
        } catch (Exception e) {
            resp.status(404);
            return "Error accept link";
        }
    };
}
