package net.haichat.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.haichat.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

// 上面的 Data 是泛型，不是具体类
public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener,
        AdapterCallback<Data> {

    // 集合不可变
    private final List<Data> mDataList;
    // 监听器
    private AdapterListener<Data> mListener;


    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 重写默认的布局类型 返回
     *
     * @param position 坐标(索引)
     * @return 类型(ViewType) ,其实重写后，返回的都是 XML 文件的 id
     */
    @Override
    public int getItemViewType(int position) {
        return this.getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     当前的数
     * @return XML文件的ID, 用于创建 ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

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
        ViewHolder<Data> holder = this.onCreateViewHolder(root, viewType);

        // 简化 onClick 事件，我们对 holder 设置 onClick 事件，实际是 为 root 设置就行了

        // 设置 View 的 Tag 为 ViewHolder, 进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);

        // 设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        // 绑定 callback
        holder.callback = this;

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

    /**
     * 绑定数据到一个 Holder 上
     *
     * @param holder
     * @param i      索引index
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int i) {
        // 得到需要绑定的数据
        Data data = mDataList.get(i);
        // 触发 我们自己写的 ViewHolder 的绑定方法
        holder.bind(data);
    }

    /**
     * 得到当前集合的数据量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入并通知插入
     *
     * @param data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemChanged(mDataList.size() - 1);
    }

    /**
     * 插入 [] 类型数据，并通知这段集合更新
     *
     * @param dataList
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList); // 将 数组 加入 集合
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入 Collection 类型数据，并通知这段集合更新
     *
     * @param dataList
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList); // 将 集合 加入 集合
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换整个集合 为一个新的 集合(其中包括了清空)
     *
     * @param dataList
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear(); // 清空实例中的 集合
        if (dataList == null || dataList.size() == 0) return;

        // 如果 dataList 不为空 且 有值
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * 设置 适配器的 监听器
     *
     * @param adapterListener
     */
    public void setListener(AdapterListener<Data> adapterListener) {
        this.mListener = adapterListener;
    }

    /**
     * 实现 类上的 Click 接口
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到 ViewHolder 当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }

    }

    /**
     * 实现 类上的 LongClick 接口
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到 ViewHolder 当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true; // 处理之后 return true
        }
        return false;
    }

    /**
     * 我们的自定义监听器
     *
     * @param <Data> 泛型
     */
    public interface AdapterListener<Data> {
        // 当 Cell 点击时触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        // 当 Cell 长按时触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 封装 自定义的 ViewHolder
     *
     * @param <Data>
     */
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
         *
         * @param data
         */
        public void updateData(Data data) { // 更新数据后，需要 刷新整个界面
            // 刷新整个界面需要我们 通过 ViewHolder，反向调用 RecyclerAdatper 进行一个更新
            // 那么他们需要建立相互之间的通知，所以我们建立 了 AdapterCallback 接口
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }

}
