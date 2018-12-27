package net.haichat.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import net.haichat.common.R;

public abstract class ToolbarActivity extends Activity {

    protected Toolbar mToolbar;


    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar(findViewById(R.id.toolbar));
    }

    /**
     * 初始化 toolbar
     *
     * @param toolbar Toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) setSupportActionBar(toolbar);
        initTitleNeedBack(); // 设置 返回键有效
    }

    protected void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
