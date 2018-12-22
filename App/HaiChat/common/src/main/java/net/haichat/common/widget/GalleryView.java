package net.haichat.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import net.haichat.common.R;
import net.haichat.common.widget.recycler.RecyclerAdapter;


public class GalleryView extends RecyclerView {

    private Adapter mAdapter = new Adapter();

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
        setLayoutManager(new GridLayoutManager(getContext(),4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListener<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {

            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Image image) {

            }
        });
    }

    private static class Image {

    }

    private class Adapter extends RecyclerAdapter<Image>{

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.gallery_cell; // 约定 ViewType 为 布局文件 id
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

        @Override
        public void update(Image image, ViewHolder<Image> holder) {

        }
    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Image image) {

        }
    }

}
