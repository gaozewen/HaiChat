package net.web.haichat.push.service;

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
}
