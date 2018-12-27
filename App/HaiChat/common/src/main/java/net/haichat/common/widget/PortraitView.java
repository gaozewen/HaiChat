package net.haichat.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;

import net.haichat.common.R;
import net.haichat.factory.model.IUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 头像控件
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, IUser iUser) {
        if (iUser == null) return;
        setup(manager, iUser.getPortrait());
    }

    public void setup(RequestManager manager, String url) {
        setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null) url = "";
        manager.load(url)
                .placeholder(resourceId) // 默认显示图片
                .centerCrop()
                .dontAnimate() // CircleImageView 控件中不能使用渐变动画，会导致显示延迟
                .into(this);
    }

}
