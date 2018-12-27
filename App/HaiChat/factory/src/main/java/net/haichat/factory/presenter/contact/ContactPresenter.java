package net.haichat.factory.presenter.contact;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.data.helper.UserHelper;
import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.model.db.AppDatabase;
import net.haichat.factory.model.db.User;
import net.haichat.factory.model.db.User_Table;
import net.haichat.factory.persistence.Account;
import net.haichat.factory.presenter.BasePresenter;

import java.util.ArrayList;
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
        // 加载 本地缓存数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollowed.eq(true)) // 是我关注的人
                .and(User_Table.id.notEq(Account.getUserId())) // 不是我自己
                .orderBy(User_Table.name, true) // 按 昵称 正向排序
                .limit(100) // 返回 100 条
                .async() // 异步操作
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        // replace 表示全局替换
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                })
                .execute();

        // 加载网络数据 -> 刷新联系人列表
        UserHelper.refreshContacts(new ApiCallback.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(int strRes) {
                // 因为本地有数据，所以 不用对 错误处理
            }

            @Override
            public void onDataLoadedSuccess(List<UserCard> userCards) {
                // 转换为 User
                final List<User> users = new ArrayList<>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.convertToUser());
                }

                // DBFlow 带事务的 保存
                DatabaseDefinition dd = FlowManager.getDatabase(AppDatabase.class);
                dd.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        for (UserCard userCard : userCards) {
                            FlowManager
                                    .getModelAdapter(User.class)
                                    .saveAll(users);
                        }
                    }
                }).build().execute();// 异步

                // 将 最新数据 刷新到界面
                getView().getRecyclerAdapter().replace(users);
                getView().onAdapterDataChanged();
            }
        });

        // todo: 问题
        // 1. 关注后虽然存储了数据库，但是没有刷新联系人
        // 2. 界面刷新是全局刷新，不是 增量刷新(局部刷新)
        // 3. 由于 本地/网络 拉取都是 异步操作，所以可能 引起界面刷新冲突
    }
}
