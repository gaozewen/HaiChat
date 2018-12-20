package net.haichat.common.widget.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.haichat.common.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

// 上面的 Data 是泛型，不是具体类
public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener {
    // 集合不可变
    private final List<Data> mDataList = new ArrayList<>();


    /**
     * 创建一个 ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型（约定为 XML 布局的 id）
     * @return ViewHolder(我们自己定义的 ViewHolder)
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 得到 LayoutInflater 用于把 XML 初始化 为 View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 本来这个 viewType 是随意定义的，但是这里我们强制与定位 R.layout.[id]
        // 把 XML id 为 viewType 的布局文件初始化为一个 root View
        View root = inflater.inflate(viewType, parent, false);
        // 通过子类必须实现的方法，得到一个 ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        // 简化 onClick 事件，我们对 holder 设置 onClick 事件，实际是 为 root 设置就行了
        // 设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // 设置 View 的 Tag 为 ViewHolder, 进行双向绑定
        root.setTag(R.id.tag_recycler_holder,holder);

        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder,root);

        return holder;
    }

    /**
     * 创建一个 Holder
     *
     * @param root     界面根布局
     * @param viewType xml布局的id
     * @return ViewHolder<Data>
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    // 绑定数据到一个 Holder 上
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> dataViewHolder, int i) {
        // 得到需要绑定的数据
        Data data = mDataList.get(i);
        // 触发 我们自己写的 ViewHolder 的绑定方法
        dataViewHolder.bind(data);
    }

    // 得到当前集合的数据量
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    // 封装 ViewHolder
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Unbinder unbinder;
        private AdapterCallback<Data> callback;
        protected Data mData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        // default 当前包 当前类可调用

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) { // 将 数据 Data 和 ViewHolder 绑定
            this.mData = data;
            onBind(data);
        }

        // 当进行绑定的时候刷新界面

        /**
         * 当触发绑定数据的时候的回调。此回调必须重写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);


        /**
         * Holder 自己对自己对应的 Data 进行更新操作
         * @param data
         */
        public void updateData(Data data) { // 更新数据后，需要 刷新整个界面
            // 刷新整个界面需要我们 通过 ViewHolder，反向调用 RecyclerAdatper 进行一个更新
            // 那么他们需要建立相互之间的通知，所以我们建立 了 AdapterCallback 接口
            if(this.callback != null){
                this.callback.update(data,this);
            }
        }
    }

}
