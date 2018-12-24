package net.web.haichat.push;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import net.web.haichat.push.provider.AuthRequestFilter;
import net.web.haichat.push.provider.GsonProvider;
import net.web.haichat.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

public class Application extends ResourceConfig {

    public Application() {

        packages(AccountService.class.getPackage().getName()); // 注册逻辑处理的包名
        register(GsonProvider.class); // 注册 Json 解析器 ,register(JacksonJsonProvider.class); == >替换解析器为 Gson
        register(Logger.class); // 注册日志打印输出
        register(AuthRequestFilter.class); // 注册我们的 全局 请求过滤器

    }
}
