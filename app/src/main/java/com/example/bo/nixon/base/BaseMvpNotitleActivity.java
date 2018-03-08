package com.example.bo.nixon.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.bo.nixon.presenter.BaseNixonView;
import com.example.bo.nixon.presenter.BasePresenter;

public abstract class BaseMvpNotitleActivity<P extends BasePresenter> extends BaseNoTitleActivity implements BaseNixonView {
    protected P presenter;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        presenter = createPresenter ();
        presenter.attachView (this);
    }

    protected abstract P createPresenter ();

    @Override protected void onDestroy () {
        super.onDestroy ();
        presenter.detachView ();
        presenter = null;
    }

    protected void toActivity(@NonNull Class cl) {
        startActivity(new Intent(this, cl));
    }

}
