package net.haichat.factory.model.card;

import net.haichat.factory.model.IUser;
import net.haichat.factory.model.db.User;

import java.util.Date;

public class UserCard implements IUser {
    private String id;
    private String name;
    private String phone;
    private String portrait;
    private String desc;
    private int sex = 0;
    private int followings; // 用户 关注的人的数量
    private int followers; // 用户 粉丝的数量
    private boolean isFollowed; // 我 与 当前 User 的关系状态 ,是否 已经关注 当前 User
    private Date modifyAt; // 用户信息 最后的更新时间

    // 缓存一个对应的 User , 不能被GSON框架解析使用
    private transient User user;
    public User convertToUser(){
        if(this.user == null){
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPortrait(portrait);
            user.setPhone(phone);
            user.setDesc(desc);
            user.setSex(sex);
            user.setFollowed(isFollowed);
            user.setFollowings(followings);
            user.setFollowers(followers);
            user.setModifyAt(modifyAt);
            this.user = user;
        }
        return this.user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }
}
