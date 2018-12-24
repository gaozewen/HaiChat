package net.web.haichat.push.service;

import net.web.haichat.push.bean.api.account.AccountRespModel;
import net.web.haichat.push.bean.api.account.LoginModel;
import net.web.haichat.push.bean.api.account.RegisterModel;
import net.web.haichat.push.bean.api.base.ResponseModel;
import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService {
    // 注册
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRespModel> register(RegisterModel model) {
        // 校验参数
        if(!RegisterModel.check(model)) return ResponseModel.buildParameterError();

        // 账号已注册
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) return ResponseModel.buildHaveAccountError();

        // 用户名已存在
        user = UserFactory.findByName(model.getName().trim());
        if (user != null) return ResponseModel.buildHaveNameError();

        // start 注册逻辑
        user = UserFactory.register(model.getAccount(), model.getPassword(), model.getName());
        // 注册失败
        if (user == null) return ResponseModel.buildRegisterError();
        // 注册成功
        return ResponseModel.buildOk(new AccountRespModel(user));
    }

    // 登录
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRespModel> login(LoginModel model) {
        // 接收参数校验
        if (!LoginModel.check(model)) return ResponseModel.buildParameterError();
        User user = UserFactory.login(model.getAccount(), model.getPassword());
        // 登录失败
        if (user == null) return ResponseModel.buildLoginError();
        // 登录成功
        return ResponseModel.buildOk(new AccountRespModel(user));
    }
}
