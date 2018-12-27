package net.haichat.factory.model;

/**
 * 基础用户接口
 */
public interface IUser {
    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getPortrait();

    void setPortrait(String portrait);
}
