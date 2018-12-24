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
public class UserService extends BaseService {

    // 修改 用户自己 的 信息
    @PUT
    @Path("/updateInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if (!UpdateInfoModel.check(model)) return ResponseModel.buildParameterError();

        User user = getSelf(); // 获取 自己的 用户信息
        user = model.updateToUser(user); // 更新用户信息
        user = UserFactory.update(user); // 同步到数据库

        return ResponseModel.buildOk(new UserCard(user, true)); // 自己肯定已经关注自己
    }

}
