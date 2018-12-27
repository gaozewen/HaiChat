package net.haichat.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import net.haichat.common.app.Activity;
import net.haichat.push.R;

public class SearchActivity extends Activity {
    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1; // 搜索人
    public static final int TYPE_GROUP = 2; // 搜索群


    private int curType; // 具体需要显示的类型

    /**
     * 显示搜索界面
     *
     * @param context 上下文
     * @param type    显示的类型，用户还是群
     */
    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        curType = bundle.getInt(EXTRA_TYPE);
        return curType == TYPE_USER || curType == TYPE_GROUP;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

}
