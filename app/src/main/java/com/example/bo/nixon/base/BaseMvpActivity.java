package com.example.bo.nixon.base;

import android.os.Bundle;

import com.example.bo.nixon.presenter.BaseNixonView;
import com.example.bo.nixon.presenter.BasePresenter;

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements BaseNixonView {

    protected P presenter;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        presenter = createPresenter ();
        presenter.attachView(this);
    }

    protected abstract P createPresenter ();

    @Override protected void onDestroy () {
        super.onDestroy ();
        presenter.detachView ();
        presenter = null;
    }

}
