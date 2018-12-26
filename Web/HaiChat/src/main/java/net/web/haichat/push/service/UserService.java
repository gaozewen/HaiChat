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
    // 简化：关注操作其实是双方同时关注
    @PUT // 修改使用 put
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        if (Strings.isNullOrEmpty(followId)) return ResponseModel.buildParameterError();
        User self = getSelf();
        // 自己不能关注自己
        if (self.getId().equals(followId)) return ResponseModel.buildParameterError();

        // 将要关注的人
        User target = UserFactory.findById(followId);
        if (target == null) return ResponseModel.buildNotFoundUserError(null);

        // 执行数据库 关注操作
        target = UserFactory.follow(self, target, null); // 备注默认没有，右面可以扩展

        // 关注失败，返回服务器异常
        if (target == null) return ResponseModel.buildServiceError();

        // TODO: 通知 target 我关注了他

        // 返回 target 的 信息
        return ResponseModel.buildOk(new UserCard(target, true));
    }

    // 获取 某个 用户的信息
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUserInfo(@PathParam("id") String userId) {
        if (Strings.isNullOrEmpty(userId)) return ResponseModel.buildParameterError();

        User self = getSelf();
        // 若 id 是登录的用户，不必查询数据库
        if (self.getId().equals(userId)) return ResponseModel.buildOk(new UserCard(self, true));

        User target = UserFactory.findById(userId);
        if (target == null) return ResponseModel.buildNotFoundUserError(null);

        // 查询 self 是否已关注 target (有关注记录，则表示已关注)
        boolean isFollow = UserFactory.getRelationship(self, target) != null;
        // 返回 target 用户信息，并 返回 target 与 self 的关系
        return ResponseModel.buildOk(new UserCard(target, isFollow));
    }

    // 模糊搜索用户
    @GET
    @Path("/search/{name:(.*)?}") // 名字为任意字符，可以为空
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(
            @DefaultValue("") @PathParam("name") String name) {
        User self = getSelf();
        // 得到 查询结果
        List<User> searchUsers = UserFactory.search(name);
        // 我 的 联系人 列表
        List<User> contacts = UserFactory.contacts(self);
        // 把查询到的用户 封装为 UserCard
        List<UserCard> userCards = searchUsers.stream()
                .map(curUser -> {
                    // 判断 我 是否关注 当前用户 (或) 当前用户就是我自己
                    boolean isFollow = curUser.getId().equals(self.getId())
                            || contacts.stream().anyMatch(contact -> contact.getId().equals(curUser.getId()));
                    return new UserCard(curUser,isFollow);
                })
                .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }

}
