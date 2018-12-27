package net.haichat.push.frags.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.haichat.common.app.PresenterFragment;
import net.haichat.common.widget.EmptyView;
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
        mRecycler.setAdapter(new RecyclerAdapter<UserCard>(){

            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                // 返回 cell 的布局 id
                return 0;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return null;
            }
        });
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

    }

    /**
     * 
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(UserCard userCard) {

        }
    }
}
