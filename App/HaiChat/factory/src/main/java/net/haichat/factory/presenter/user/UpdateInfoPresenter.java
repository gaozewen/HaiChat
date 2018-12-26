package net.haichat.factory.presenter.user;

import android.text.TextUtils;

import net.haichat.factory.Factory;
import net.haichat.factory.R;
import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.data.helper.UserHelper;
import net.haichat.factory.model.api.user.UserUpdateModel;
import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.model.db.User;
import net.haichat.factory.net.UploadHelper;
import net.haichat.factory.presenter.BasePresenter;

public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, ApiCallback.Callback<UserCard> {
    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(String portraitPath, boolean isMan, String desc) {
        start();
        UpdateInfoContract.View view = getView();

        if (TextUtils.isEmpty(portraitPath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
            return;
        }
        // 上传头像到 OSS
        Factory.runOnAsync(() -> {
            String remoteUrl = UploadHelper.uploadPortrait(portraitPath);
            if (TextUtils.isEmpty(remoteUrl)) { // 上传失败
                view.showError(R.string.data_upload_error);
                return;
            }
            // 上传成功
            UserUpdateModel model = new UserUpdateModel(
                    "", remoteUrl, desc,
                    isMan ? User.SEX_MAN : User.SEX_WOMAN
            );
            // 发送 Api 请求 更新 用户信息
            UserHelper.update(model,UpdateInfoPresenter.this);


        });
    }

    @Override
    public void onDataLoadedSuccess(UserCard userCard) {

    }

    @Override
    public void onDataNotAvailable(int strRes) {

    }
}
