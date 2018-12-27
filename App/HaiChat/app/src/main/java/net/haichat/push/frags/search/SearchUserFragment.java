package net.haichat.push.frags.search;

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
import net.haichat.factory.presenter.search.SearchContract;
import net.haichat.factory.presenter.search.SearchUserPresenter;
import net.haichat.push.R;
import net.haichat.push.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;

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
     * 每一个 Cell 的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(UserCard userCard) {
            // 头像加载
            Glide.with(SearchUserFragment.this)
                    .load(userCard.getPortrait())
                    .centerCrop()
                    .into(mPortraitView);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollowed());
        }
    }
}
