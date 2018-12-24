package net.web.haichat.push.service;

import com.google.common.base.Strings;
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
        if (!RegisterModel.check(model)) return ResponseModel.buildParameterError();

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
        // 情况1：参数中携带了 pushId  ===>  登录 并 绑定 pushId
        if (!Strings.isNullOrEmpty(model.getPushId())) return bind(user, model.getPushId());
        // 情况2：参数中无 pushId ===> 仅登录
        return ResponseModel.buildOk(new AccountRespModel(user));
    }

    // 绑定设备id(PushId)
    @POST
    @Path("/bind/{pushId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRespModel> bind(
            @HeaderParam("token") String token, // 从 请求头 中获取 token 字段
            @PathParam("pushId") String pushId // 从 url 地址中 获取
    ) {
        // 参数校验
        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushId))
            return ResponseModel.buildParameterError();

        User user = UserFactory.findByToken(token);
        // token 失效，用户查不到，返回 账户异常
        if (user == null) return ResponseModel.buildAccountError();

        // 进入 绑定 方法
        return bind(user, pushId);
    }

    private ResponseModel<AccountRespModel> bind(User user, String pushId) {
        // 进行设备 id 的绑定操作
        user = UserFactory.bindPushId(user, pushId);
        // 绑定失败，显示 服务器异常
        if (user == null) return ResponseModel.buildServiceError();
        // 绑定成功
        return ResponseModel.buildOk(new AccountRespModel(user, true));
    }
}
