package net.haichat.common;

public class Common {

    /**
     * 一些不可变的参数
     * 通常用于一些配置
     */
    public interface Constance{
        // 手机号 正则表达式, 11 位 手机号
        String REGEX_MOBILE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    }

}
