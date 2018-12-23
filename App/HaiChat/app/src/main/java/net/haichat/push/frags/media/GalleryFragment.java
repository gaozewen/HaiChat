package net.haichat.push.frags.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.haichat.common.tools.UiTool;
import net.haichat.common.widget.GalleryView;
import net.haichat.push.R;
import net.qiujuer.genius.ui.Ui;


/**
 * 图片选择 Fragment (头像选择用)
 */
public class GalleryFragment extends BottomSheetDialogFragment
        implements GalleryView.SelectedChangeListener {

    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {
    }


    /**
     * 设置事件监听 并返回自己
     *
     * @param listener OnSelectedListener
     * @return GalleryFragment
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 我们先使用默认的
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取我们的 GalleryView
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if (count > 0) { // 如果选中了一张图片
            dismiss();
            if (mListener != null) {
                // 获取所有选中图片的路径
                String[] selectedPaths = mGallery.getSelectedPaths();
                // 返回第一张
                mListener.onSelectedImage(selectedPaths[0]);
                // 因为已经隐藏自己了，所以 F 销毁了 mListener 也就无效了，所以置为空
                // 取消 和 唤起者之间的引用 ，加快内存回收
                mListener = null;
            }
        }
    }

    /**
     * 选中图片后的回调
     */
    public interface OnSelectedListener {
        void onSelectedImage(String path); // 只能选中一个图片
    }

    // 透明状态栏的 Dialog
    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null) return;
            // 屏幕高度
            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            // 状态栏高度
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());
            // 计算 dialog 的高度并设置
            int dialogHeight = screenHeight - statusHeight;

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }
}
