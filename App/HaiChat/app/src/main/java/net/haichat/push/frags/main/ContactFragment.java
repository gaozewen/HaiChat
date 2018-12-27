package net.haichat.push.frags.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.haichat.common.app.PresenterFragment;
import net.haichat.common.widget.EmptyView;
import net.haichat.common.widget.PortraitView;
import net.haichat.common.widget.recycler.RecyclerAdapter;
import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.model.db.User;
import net.haichat.factory.presenter.contact.ContactContract;
import net.haichat.factory.presenter.contact.ContactPresenter;
import net.haichat.push.R;
import net.haichat.push.activities.ChatActivity;

import java.util.List;

import butterknife.BindView;

public class ContactFragment extends PresenterFragment<ContactContract.Presenter>
        implements ContactContract.View, ApiCallback.Callback<List<UserCard>> {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    // 适配器，User， 可以直接从数据库 查询数据
    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        // 初始化 Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {

            @Override
            protected int getItemViewType(int position, User user) {
                // 返回 cell 的布局 id
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                // 每一个 cell 的 View
                return new ContactFragment.ViewHolder(root);
            }
        });

        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                ChatActivity.show(getContext(), user); // 跳转到聊天界面
            }
        });

        // 初始化 自定义的 占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start(); // 只在第一次初始化的时候，加载数据
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        // 初始化 Presenter
        return new ContactPresenter(this);
    }

    @Override
    public void onDataLoadedSuccess(List<UserCard> userCards) {

    }

    @Override
    public void onDataNotAvailable(int strRes) {

    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 数据改变，刷新 界面
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setup(Glide.with(ContactFragment.this), user);
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
        }
    }
}
