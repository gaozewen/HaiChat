package net.haichat.factory.presenter.account;

import net.haichat.factory.presenter.BasePresenter;

public class RegisterPresenter
        extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String password, String name) {

    }

    @Override
    public boolean checkMobile(String phone) {
        return false;
    }

}
