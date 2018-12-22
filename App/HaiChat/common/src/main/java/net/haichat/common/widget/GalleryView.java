package net.haichat.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.haichat.common.R;
import net.haichat.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3; // 最大的选中图片数量
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024; // 最小的图片大小
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectedChangeListener mListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                // Cell 点击操作，
                // 若允许 点击， 更新对应的 Cell 状态，然后更新界面，
                // 若禁止 点击(以达到最大可以选中的 图片数量)， 不再更新 新的Cell 状态
                if (isNeedRefresh(image)) {
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * 初始化 Loader 方法
     *
     * @param loaderManager
     * @return 一个 LOADER_ID 可用于销毁 Loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener) {
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        mListener = listener;
        return LOADER_ID;
    }

    /**
     * 获取选中图片的全部 path
     *
     * @return String[]
     */
    public String[] getSelectedPaths() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 清空选中的所有图片
     */
    public void clear() {
        for (Image image : mSelectedImages) {
            image.isSelected = false; // 将状态变更为 未选中状态
        }
        mSelectedImages.clear(); // 清空集合
        mAdapter.notifyDataSetChanged(); // 通知界面更新
    }

    /**
     * Cell 点击 的具体逻辑
     *
     * @param image
     * @return True 数据更新,需要刷新界面 False 数据不更新，不刷新界面
     */
    private boolean isNeedRefresh(Image image) {
        boolean isNeedRefresh; // 是否需要刷新
        if (mSelectedImages.contains(image)) { // 之前已经是 被选中状态
            mSelectedImages.remove(image);
            image.isSelected = false;
            isNeedRefresh = true; // 状态已经改变，需要更新
        } else { // 加入图片 到 被选中集合中
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT) { // 已经达到 选中临界值
                isNeedRefresh = false; // 不更新 图片状态
                // 获取提示文字，并弹出 提示
                String msg = getResources().getString(R.string.label_gallery_select_max_size);
                msg = String.format(msg,MAX_IMAGE_COUNT);
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
            } else {
                mSelectedImages.add(image);
                image.isSelected = true;
                isNeedRefresh = true;
            }
        }
        // 若 数据状态 变更，我们需要通知 外面的监听者
        if (isNeedRefresh) notifySelectedChanged();
        return isNeedRefresh;
    }

    /**
     * 通知 选中的个数 改变
     */
    private void notifySelectedChanged() {
        // 监听 被选中个数 的数量变化 的 回调
        if (mListener != null) {
            mListener.onSelectedCountChanged(mSelectedImages.size());
        }
    }

    /**
     * 通知 Adapter 更新数据源
     *
     * @param images 新的数据
     */
    private void updateDataSource(List<Image> images) {
        mAdapter.replace(images);
    }

    /**
     * 用于实际的数据加载的 Loader Callback
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,        // id
                MediaStore.Images.Media.DATA,       // 图片路径
                MediaStore.Images.Media.DATE_ADDED  // 图片的创建时间
        };

        // 创建一个 Loader 后
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
            // 创建一个 Loader
            if (id == LOADER_ID) {
                // 如果是我们的 ID 则可以进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,// 图片外部 uri
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC"); // 按创建时间倒序
            }
            return null;
        }

        // Loader 加载成功的情况下 回调
        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            List<Image> images = new ArrayList<>();
            if (cursor != null) { // 判断是否有数据
                int count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst(); // 移动游标到开始

                    // 获取对应列 的 index
                    int indexId = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);

                    do {
                        // 循环读取，知道没有下一条数据
                        int id = cursor.getInt(indexId);
                        String path = cursor.getString(indexPath);
                        long dateTime = cursor.getLong(indexDate);

                        File file = new File(path);
                        // 图片不存在 或者是 表情包 则 不操作 这一条数据
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) continue;

                        // 添加一条 有效 数据
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;
                        images.add(image);

                    } while (cursor.moveToNext());
                }
            }
            // 通知 Adapter 更新数据源
            updateDataSource(images);
        }


        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            // Loader 销毁 或 重置的情况下 回调，进行界面清空
            updateDataSource(null);
        }
    }

    /**
     * 内部的数据结构
     */
    private static class Image {
        int id;             // 数据 id
        String path;        // 手机存储路径
        long date;          // 按创建时间排序图片
        boolean isSelected; // 是否被选中

        // 判断时 同一 path 则为同一张图片
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @Override
        public int hashCode() {

            return Objects.hash(path);
        }
    }

    /**
     * 适配器 Adapter
     */
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.gallery_cell; // 约定 ViewType 为 布局文件 id
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

    }

    /**
     * Cell 对应的 ViewHolder(对应 布局文件：gallery_cell.xml)
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private ImageView mPic;
        private View mShade;
        private CheckBox mSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPic = itemView.findViewById(R.id.im_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelect = itemView.findViewById(R.id.cb_select);
        }

        // 绑定 数据 和 视图
        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 因为是本地图片,不使用缓存，直接从原图加载
                    .centerCrop() // 居中剪切
                    .placeholder(R.color.grey_200) // 延时加载，默认显示颜色
                    .into(mPic);

            // 在 选中图片情况下 显示 遮罩
            mShade.setVisibility(image.isSelected?VISIBLE:INVISIBLE);

            mSelect.setChecked(image.isSelected);
            mSelect.setVisibility(VISIBLE);

        }
    }

    /**
     * 对外的一个监听器
     * 是 使用 GalleryView 时 需要设置的监听器
     */
    public interface SelectedChangeListener {
        void onSelectedCountChanged(int count);
    }

}
