package com.example.bo.nixon.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bo.nixon.presenter.BaseNixonView;
import com.example.bo.nixon.presenter.BasePresenter;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseFragment

    implements BaseNixonView {

    protected P presenter;

    @Nullable @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        presenter = createPresenter ();
        if (presenter == null) {
            throw new RuntimeException ("The method createPresenter return null");
        }
        presenter.attachView (this);
        return super.onCreateView (inflater, container, savedInstanceState);
    }

    protected abstract P createPresenter ();

    @Override public void onDestroyView () {
        super.onDestroyView ();
        presenter.detachView ();
        presenter = null;
    }

}
