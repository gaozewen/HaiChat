package net.web.haichat.push.service;


import com.google.common.base.Strings;
import net.web.haichat.push.bean.api.base.ResponseModel;
import net.web.haichat.push.bean.api.user.UpdateInfoModel;
import net.web.haichat.push.bean.card.UserCard;
import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

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

    // 拉取联系人
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();
        // 通过数据库 获取 我的联系人
        List<User> contacts = UserFactory.contacts(self);
        List<UserCard> userCards = contacts.stream()
                .map(user -> new UserCard(user, true)) // map 相当于转置操作
                .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }

    // 关注
    @PUT // 修改使用 put
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        if(Strings.isNullOrEmpty(followId)) return ResponseModel.buildParameterError();
        User self = getSelf();
        // 自己不能关注自己
        if(self.getId().equals(followId)) return ResponseModel.buildParameterError();

        // 将要关注的人
        User target = UserFactory.findById(followId);
        if(target == null) return ResponseModel.buildNotFoundUserError(null);

        // 执行数据库 关注操作
        target = UserFactory.follow(self,target,null); // 备注默认没有，右面可以扩展

        // 关注失败，返回服务器异常
        if(target == null) return ResponseModel.buildServiceError();

        // TODO: 通知 target 我关注了他

        // 返回 target 的 信息
        return ResponseModel.buildOk(new UserCard(target,true));
    }

}
