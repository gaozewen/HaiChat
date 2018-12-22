package net.haichat.push.frags.main;

import net.haichat.common.app.Fragment;
import net.haichat.common.widget.GalleryView;
import net.haichat.push.R;

import butterknife.BindView;

public class ActiveFragment extends Fragment {
    @BindView(R.id.galleryView)
    GalleryView mGallery;


    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

        mGallery.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
