package net.haichat.factory.presenter.search;


import net.haichat.factory.presenter.BasePresenter;

/**
 * 搜索人的逻辑实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter{
    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }




}
