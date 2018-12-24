package net.web.haichat.push.service;

import net.web.haichat.push.bean.api.account.RegisterModel;
import net.web.haichat.push.bean.card.UserCard;
import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService {

    @GET
    @Path("/login")
    public String loginGet() {
        return "You get the login.";
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User loginPost() {
        User user = new User();
        user.setName("美女");
        user.setSex(2);
        return user;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserCard register(RegisterModel model) {
        // 进行数据库 保存 操作
        User user = UserFactory.register(model.getAccount(), model.getPassword(), model.getName());

        if (user != null){
            UserCard card = new UserCard();
            card.setName(user.getName());
            card.setPhone(user.getPhone());
            card.setSex(user.getSex());
            card.setFollowed(true);
            card.setModifyAt(user.getUpdateAt());
            return card;
        }

        return null;
    }
}
