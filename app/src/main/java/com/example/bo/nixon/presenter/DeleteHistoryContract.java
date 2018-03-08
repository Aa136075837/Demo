package com.example.bo.nixon.presenter;

/**
 * @author bo.
 * @Date 2017/6/20.
 * @desc
 */

public interface DeleteHistoryContract {
    interface DeleteNixonView extends BaseNixonView {
        void showToast ();
    }

    class DeleteHistoryPresenter extends BasePresenter<DeleteNixonView> {
        public void deleteFile () {
            getView ().showToast ();
        }
    }
}
