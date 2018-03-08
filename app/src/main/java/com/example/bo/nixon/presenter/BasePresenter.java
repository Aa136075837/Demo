package com.example.bo.nixon.presenter;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.lang.ref.WeakReference;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public class BasePresenter<V extends MvpView> {

    private WeakReference<V> viewWeak;

    public V getView () {
        return viewWeak == null ? null : viewWeak.get ();
    }

    public boolean isViewAttach () {
        return viewWeak != null && viewWeak.get () != null;
    }

    public void attachView (V view) {
        viewWeak = new WeakReference<> (view);
    }

    public void detachView () {
        if (viewWeak != null) {
            viewWeak.clear ();
            viewWeak = null;
        }
    }
}
