package net.haichat.push.frags.search;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.haichat.common.app.Fragment;
import net.haichat.common.app.PresenterFragment;
import net.haichat.factory.model.card.GroupCard;
import net.haichat.factory.presenter.search.SearchContract;
import net.haichat.factory.presenter.search.SearchGroupPresenter;
import net.haichat.push.R;
import net.haichat.push.activities.SearchActivity;

import java.util.List;

/**
 * 搜索 群 的界面实现
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment,SearchContract.GroupView {


    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    // 搜索完成，加载 界面
    @Override
    public void onSearchDone(List<GroupCard> groupCards) {

    }
}
