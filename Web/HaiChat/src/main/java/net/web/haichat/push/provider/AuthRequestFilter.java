package net.web.haichat.push.provider;

import com.google.common.base.Strings;
import net.web.haichat.push.bean.api.base.ResponseModel;
import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * 所有请求的过滤器
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {

    // 实现 接口的 方法
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 检测是否是 登录 注册 接口
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login") || relationPath.startsWith("account/register"))
            return; // 直接走正常逻辑，不做拦截

        // 获取 token
        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) { // 有 token
            User self = UserFactory.findByToken(token); // 根据 token 查询 自己的 用户信息

            if (self != null) {
                // 为 当前请求 提供 一个 上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    // 主体部分
                    @Override
                    public Principal getUserPrincipal() {
                        return self; // User 需实现 Principal 接口
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // 可以在这里写入 用户的权限，role 是权限名
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        // 默认 false 即可，这里是检查 是否是 https 请求
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null; // 不用管
                    }
                });
                // 进入正常逻辑
                return;
            }
        }

        // 除上述情况以外 都进行 拦截
        // 停止一个请求的继续下发，调用该方法后 直接 返回请求
        // 不会走到 Service 中
        requestContext.abortWith(
                Response // 构建一个返回
                        .status(Response.Status.OK)
                        .entity(ResponseModel.buildAccountError()) // 直接返回一个 账户异常
                        .build()
        );
    }
}
