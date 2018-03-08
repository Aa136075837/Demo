package com.example.bo.nixon.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Toast;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.ui.activity.EditUserInfoActivity;
import com.example.bo.nixon.ui.activity.LoginActivity;

/**
 * @author bo.
 * @Date 2017/6/1.
 * @desc
 */
public class NetUtil {

    public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;

    /**
     * @desc 获取当前网络状况
     * @param context
     * @return NETWORN_WIFI | NETWORN_MOBILE | NETWORN_NONE
     */
	public static int getNetworkState(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=cm.getActiveNetworkInfo();
		if(info!=null&&info.isConnected()){
			switch (info.getType()){
				case ConnectivityManager.TYPE_WIFI:
					return NETWORN_WIFI;
				case ConnectivityManager.TYPE_MOBILE:
					return NETWORN_MOBILE;
			}
		}
		return NETWORN_NONE;
	}

    /**
     * 判断手机是否联网，如果没有弹出提示语，并return
     */
    public static void isConnectNet(Context context){
        if (getNetworkState (NixonApplication.getContext ()) == 0){
            // TODO: 2017/6/1 没有网络的提示
            return;
        }
    }

    //public void showDialog (@LayoutRes int resId, String msg) {
    //    final DialogUtil dialogUtil = new DialogUtil (this);
    //    dialogUtil.show (resId, msg, R.id.dialog_message_tv);
    //
    //}
}
