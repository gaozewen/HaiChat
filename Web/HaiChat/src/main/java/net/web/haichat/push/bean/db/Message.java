package net.web.haichat.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * 消息表
 */
@Entity
@Table(name = "TB_MESSAGE")
public class Message {

    public static final int TYPE_STR = 1; // 字符串类型
    public static final int TYPE_PIC = 2; // 图片类型
    public static final int TYPE_FILE = 3; // 文件类型
    public static final int TYPE_AUDIO = 4; // 语音类型

    @Id
    @PrimaryKeyJoinColumn
    // 这里不自动生成UUID，Id 由代码写入，由客户端负责生成之后，发送给服务器端进行存储
    // 避免复杂的服务器和客户端的映射关系
//    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String attach; // 附件

    @Column(nullable = false)
    private int type; // 消息类型

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    // 发送者
    // 多个消息 对应 一个发送者
    // 在 Message 表中 对应的字段名
    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    private User sender;
    // 这个字段 仅仅 只是为了 对应 sender 的数据库字段 senderId
    // 不允许手动的更新或者插入，只能通过 sender 来进行更新
    @Column(nullable = false, updatable = false, insertable = false)
    private String senderId;

    // 接收者可为空
    // 多个消息 对应 一个接收者
    @JoinColumn(name = "receiverId")
    @ManyToOne // 可选，即可为空，因为 接收者可以是 群组
    private User receiver;
    @Column(updatable = false, insertable = false)
    private String receiverId;

    // 一个群可以接收多个消息
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;
    @Column(updatable = false, insertable = false)
    private String groupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
