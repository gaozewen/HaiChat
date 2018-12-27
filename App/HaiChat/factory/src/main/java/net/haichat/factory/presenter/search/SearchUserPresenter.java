package net.haichat.factory.presenter.search;


import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.data.helper.UserHelper;
import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.presenter.BasePresenter;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * 搜索人的逻辑实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter, ApiCallback.Callback<List<UserCard>> {
    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    private Call searchCall;

    @Override
    public void search(String content) {
        start();
        // 临时变量，防止线程冲突
        Call call = searchCall;
        // 如果有上一次的请求，并且没有取消，
        // 则调用取消请求操作
        if (call != null && !call.isCanceled()) call.cancel();

        searchCall = UserHelper.search(content, this);
    }


    @Override
    public void onDataLoadedSuccess(List<UserCard> userCards) {
        // 搜索成功
        final SearchContract.UserView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        // 搜索失败
        final SearchContract.UserView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
