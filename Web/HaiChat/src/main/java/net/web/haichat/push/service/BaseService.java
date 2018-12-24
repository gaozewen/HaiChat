package net.web.haichat.push.service;

import net.web.haichat.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseService {

    // 添加一个上下文注解，该注解 会 为 securityContext 赋值
    // 具体值 为 我们的 过滤器 中 所返回的  securityContext
    @Context
    private SecurityContext securityContext;

    /**
     * 从上下文中 获取自己的信息
     * @return
     */
    protected User getSelf(){
        return (User) securityContext.getUserPrincipal();
    }

}
