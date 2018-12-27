package net.haichat.push.frags.search;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.haichat.common.app.PresenterFragment;
import net.haichat.common.widget.EmptyView;
import net.haichat.common.widget.PortraitView;
import net.haichat.common.widget.recycler.RecyclerAdapter;
import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.presenter.contact.FollowContract;
import net.haichat.factory.presenter.contact.FollowPresenter;
import net.haichat.factory.presenter.search.SearchContract;
import net.haichat.factory.presenter.search.SearchUserPresenter;
import net.haichat.push.R;
import net.haichat.push.activities.SearchActivity;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索 人 的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        // 初始化 Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {

            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                // 返回 cell 的布局 id
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                // 每一个 cell 的 View
                return new SearchUserFragment.ViewHolder(root);
            }
        });

        // 初始化 自定义的 占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 自动发起首次搜索
        search("");
    }

    // 执行 搜索 业务逻辑
    @Override
    public void search(String content) {
        // Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    // 搜索成功 加载界面
    @Override
    public void onSearchDone(List<UserCard> userCards) {
        // 数据成功的情况下返回数据
        mAdapter.replace(userCards);
        // 如果有数据，则是OK，没有数据就显示 空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    /**
     * 每一个 Cell 的布局操作(可以看作一个 Fragment)
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
            implements FollowContract.View {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        private FollowContract.Presenter mPresenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 当前View和Presenter绑定  ==> 其他 F 中 是 使用 initPresent
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            // 头像加载
            mPortraitView.setup(Glide.with(SearchUserFragment.this), userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollowed());
        }

        @OnClick(R.id.im_follow)
        void onFollowClick() {
            mPresenter.follow(mData.getId()); // 发起关注
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mFollow.getDrawable()).stop(); // 结束 loading 状态
                mFollow.setImageResource(R.drawable.sel_opt_done_add); // 设置为默认的
            }
            updateData(userCard); // 更新数据， 刷新 cell 界面
        }

        @Override
        public void showError(int str) {
            // 更改当前界面状态
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                // 停止动画，并显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
                mFollow.setImageResource(R.drawable.sel_opt_done_add); // 设置为默认的
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化一个圆形的动画 Drawable
            LoadingCircleDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);
            int[] color = {UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            // 将 loading 设置进 关注按钮
            mFollow.setImageDrawable(drawable);
            // 启动动画
            drawable.start();


        }

    }
}
