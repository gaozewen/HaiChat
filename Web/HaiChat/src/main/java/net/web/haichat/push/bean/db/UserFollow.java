package net.web.haichat.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户关系的 Model
 * 用于用户之间进行好友关系的实现
 */
@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    // 定义一个发起人，你关注某人，这里就是你
    // 多对1 -> 这里的 1 就是 origin，你可以关注很多人
    // 这里的多对一是： User 对应 多个 UserFollow
    // optional 不可选,必须存储,一条关注记录一定要有一个关注人
    @ManyToOne(optional = false)
    // 定义在 关联表 中的 字段名为 originId,对应的是 User 表 中的 id 字段
    @JoinColumn(name = "originId")
    private User origin;

    // 把数据库中这个字段 提取到 UserFollow 这个 model 中
    // 这个字段在 上面 origin 中已经在 数据库表中完成映射了，
    // 这里仅仅是 在 java 类中使用，不与数据库中字段有 映射关系
    // 这样就：
    //     可以在不加载 User 的情况下 通过 id 来操作
    @Column(nullable = false,updatable = false,insertable = false)
    private String originId;

    // 定义关注的目标，这里的 target 就是 你关注的人
    // 也是多对一，你可以被很多人关注，每次关注都是一条记录
    // 所以就是 多个 UserFollow 对应 一个 User 的情况
    @ManyToOne(optional = false)
    // 定义在 关联表 中的 字段名为 targetId,对应的是 user 表 中的 id
    @JoinColumn(name = "targetId")
    private User target;

    @Column(nullable = false,updatable = false,insertable = false)
    private String targetId;

    // 别名，对 target 的备注
    // 可以为 null
    @Column
    private String alias;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
