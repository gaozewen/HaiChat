package net.haichat.factory.presenter.contact;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.haichat.factory.model.db.User;
import net.haichat.factory.model.db.User_Table;
import net.haichat.factory.persistence.Account;
import net.haichat.factory.presenter.BasePresenter;

import java.util.List;

/**
 * 联系人列表 P层(业务逻辑层)
 */
public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter {
    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        // todo: 加载数据

        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollowed.eq(true)) // 是我关注的人
                .and(User_Table.id.notEq(Account.getUserId())) // 不是我自己
                .orderBy(User_Table.name,true) // 按 昵称 正向排序
                .limit(100) // 返回 100 条
                .async() // 异步操作
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                })
                .execute();
    }
}
