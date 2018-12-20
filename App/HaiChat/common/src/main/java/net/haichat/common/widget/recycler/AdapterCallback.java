package net.haichat.common.widget.recycler;

// Data 任意类型的数据
public interface AdapterCallback<Data> {

    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
