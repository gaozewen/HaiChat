package net.web.haichat.push.service;


import com.google.common.base.Strings;
import net.web.haichat.push.bean.api.base.ResponseModel;
import net.web.haichat.push.bean.api.user.UpdateInfoModel;
import net.web.haichat.push.bean.card.UserCard;
import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 用户信息处理的 Service
 */
@Path("/user")
public class UserService {

    // 修改 用户自己 的 信息
    @PUT
    @Path("/updateInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(
            @HeaderParam("token") String token, UpdateInfoModel model
    ) {
        if(Strings.isNullOrEmpty(token) || !UpdateInfoModel.check(model))
            return ResponseModel.buildParameterError();

        User user = UserFactory.findByToken(token);
        // token 失效
        if(user == null) return ResponseModel.buildAccountError();
        // 更新用户信息
        user = model.updateToUser(user);
        // 同步到数据库
        user = UserFactory.update(user);
        return ResponseModel.buildOk(new UserCard(user,true)); // 自己肯定已经关注自己
    }

}
