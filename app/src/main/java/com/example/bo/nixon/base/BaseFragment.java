package com.example.bo.nixon.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bo.nixon.R;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.utils.DialogUtil;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public abstract class BaseFragment extends Fragment {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected FragmentManager mFragmentManager;
    protected FragmentTransaction transaction;
    private DialogUtil mDialogUtil;
    /**
     * 页面是否可见
     */
    private boolean isVisible;
    /**
     * view是否初始化完成
     */
    private boolean isCreated;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            doUnVisibleThing();
        }
    }

    /**
     * 页面不可见时的操作，比如取消请求等，子类根据自身业务去实现
     */
    protected void doUnVisibleThing() {
    }

    /**
     * 加载数据的具体方法，需要子类去实现
     */
    protected void loadData() {
    }

    /**
     * 如果isVisible&&isCreated为true时，方可加载数据
     */
    private void lazyLoad() {
        if (isVisible && isCreated) {
            loadData();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            /**
             * 根据保存的状态，hide或show farmgent
             */
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden()); //自己保存fragment 退出时的状态，防止fragment重叠
    }

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(NixonApplication.getContext()).inflate(getLayoutResId(), null);
        FontManager.changeFonts((ViewGroup) view);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected void initView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentManager = getChildFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        isCreated = true;
        initView();
        lazyLoad();
    }

    public void onReloadClick() {
    }

    @LayoutRes
    public abstract int getLayoutResId();

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    //
    //protected void show1BlackErrorDialog (@StringRes int msg) {
    //    showOneBtnDialog (R.layout.one_btn_black_dialog, R.drawable.icon_tooltip_error, R.id.dialog_tip_img, msg,
    //        R.id.dialog_message_tv, new View.OnClickListener () {
    //            @Override public void onClick (View view) {
    //                hideDialog ();
    //                afterHideErrorDialog (view);
    //            }
    //        }, R.id.dialog_ok_tv);
    //}

    protected void afterHideErrorDialog(View view) {
        // NONE
    }

    protected void show2NewDialog(@StringRes int msg, View.OnClickListener listener) {
        showTwoBtnDialog(R.layout.two_btn_white_dialog_new, msg, R.id.dialog_message_tv, listener,
                R.id.dialog_confirm_tv, R.id.dialog_cancel_tv);
    }

    protected boolean isShowing() {
        if (null == mDialogUtil)
            return false;
        return mDialogUtil.isShowing();
    }

    protected void show2NewDialog(@StringRes int msg, View.OnClickListener listener, @StringRes int title,
                                  @StringRes int confirm, @StringRes int cancel) {
        showTwoBtnDialog(R.layout.two_btn_white_dialog_new1, msg, R.id.dialog_message_tv, listener,
                R.id.dialog_confirm_tv, R.id.dialog_cancel_tv, title, R.id.dialog_title, confirm, cancel);
    }

    protected void show1NewDialog(@StringRes int msg) {
        showOneBtnDialog(R.layout.one_btn_white_dialog_new_got, msg, R.id.dialog_message_tv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialog();
                afterHideErrorDialog(view);
            }
        }, R.id.dialog_confirm_tv);
    }

    protected void show1NewDialog(@StringRes int msg, @IdRes int titleId, @StringRes int title) {
        showOneBtnDialog(R.layout.one_btn_white_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialog();
            }
        }, R.id.dialog_confirm_tv);
    }

    protected void show1NewMidDialog(@StringRes int msg, @IdRes int titleId, @StringRes int title) {
        showOneBtnDialog(R.layout.one_btn_mid_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialog();
                afterHideErrorDialog(view);
            }
        }, R.id.dialog_confirm_tv, titleId, title);
    }

    protected void show1NewLongDialog(@LayoutRes int layoutRes,@StringRes int msg, @IdRes int titleId, @StringRes int title) {
        showOneBtnDialog(layoutRes, msg, R.id.dialog_message_tv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialog();
                afterHideErrorDialog(view);
            }
        }, R.id.dialog_confirm_tv, titleId, title);
    }

    protected void show1NewLongDialog(@StringRes int msg, @IdRes int titleId, @StringRes int title) {
        showOneBtnDialog(R.layout.contact_tip_new_got, msg, R.id.dialog_message_tv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialog();
                afterHideErrorDialog(view);
            }
        }, R.id.dialog_confirm_tv, titleId, title);
    }


    protected void showOneBtnDialog(@LayoutRes int res, @StringRes int msg, @IdRes int tvID,
                                    View.OnClickListener listener, @IdRes int btnID, @IdRes int titleId, @StringRes int title) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss();
        }
        mDialogUtil = new DialogUtil(getActivity());
        mDialogUtil.show(res, msg, title, tvID, titleId);
        mDialogUtil.setOnClickListener(listener, btnID);
        mDialogUtil.setLayoutParams();
    }

    protected void showOneBtnDialog(@LayoutRes int res, @StringRes int msg, @IdRes int tvID,
                                    View.OnClickListener listener, @IdRes int btnID) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss();
        }
        mDialogUtil = new DialogUtil(getActivity());
        mDialogUtil.show(res, msg, tvID);
        mDialogUtil.setOnClickListener(listener, btnID);
        mDialogUtil.setLayoutParams();
    }

    //protected void showOneBtnDialog (@LayoutRes int res, @DrawableRes int draw, @IdRes int imgID, @StringRes int msg,
    //    @IdRes int tvID, View.OnClickListener listener, @IdRes int btnID) {
    //    mDialogUtil = new DialogUtil (getActivity ());
    //    mDialogUtil.show (res, draw, imgID, msg, tvID);
    //    mDialogUtil.setOnClickListener (listener, btnID);
    //}

    protected void showTwoBtnDialog(@LayoutRes int res, @StringRes int msg, @IdRes int tvID,
                                    View.OnClickListener listener, @IdRes int IDL, @IdRes int IDR) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss();
        }
        mDialogUtil = new DialogUtil(getActivity());
        mDialogUtil.show(res, msg, tvID);
        mDialogUtil.setOnClickListener(listener, IDL, IDR);
        mDialogUtil.setLayoutParams();
    }

    protected void showTwoBtnDialog(@LayoutRes int res, String msg, @IdRes int tvID, View.OnClickListener listener,
                                    @IdRes int IDL, @IdRes int IDR) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss();
        }
        mDialogUtil = new DialogUtil(getActivity());
        mDialogUtil.show(res, msg, tvID);
        mDialogUtil.setOnClickListener(listener, IDL, IDR);
        mDialogUtil.setLayoutParams();
    }

    protected void showTwoBtnDialog(@LayoutRes int res, @StringRes int msg, @IdRes int tvID,
                                    View.OnClickListener listener, @IdRes int iDL, @IdRes int iDR, @StringRes int title, @IdRes int titleId,
                                    @StringRes int confirm, @StringRes int cancel) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss();
        }
        mDialogUtil = new DialogUtil(getActivity());
        mDialogUtil.show(res, msg, title, tvID, titleId, confirm, iDL, cancel, iDR);
        mDialogUtil.setOnClickListener(listener, iDL, iDR);
        mDialogUtil.setLayoutParams();
    }

    protected void show2BlackRightDialog(@StringRes int msg, View.OnClickListener listener) {
        showTwoBtnDialog(R.layout.two_btn_black_dialog, msg, R.id.dialog_message_tv, listener, R.id.dialog_confirm_tv,
                R.id.dialog_cancel_tv);
    }

    protected void showNoneBtnDialog(@LayoutRes int res, @StringRes int msg, @IdRes int tvID) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss();
        }
        mDialogUtil = new DialogUtil(getActivity());
        mDialogUtil.show(res, msg, tvID);
        mDialogUtil.setLayoutParams();
    }

    protected View findViewByDialog(@IdRes int id) {
        return mDialogUtil.findViewById(id);
    }

    protected void hideDialog() {
        if (mDialogUtil != null) {
            mDialogUtil.dismiss();
        }
    }
}
