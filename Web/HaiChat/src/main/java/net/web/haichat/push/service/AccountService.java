package net.web.haichat.push.service;

import net.web.haichat.push.bean.api.account.RegisterModel;
import net.web.haichat.push.bean.card.UserCard;
import net.web.haichat.push.bean.db.User;

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
        UserCard card = new UserCard();
        card.setName(model.getName());
        card.setFollowed(true);
        return card;
    }
}
