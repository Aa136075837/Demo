package com.example.bo.nixon.model.countrycode;

/**
 * Created by admin on 2016/7/22.
 */

import android.text.TextUtils;

import com.example.bo.nixon.bean.ContactBean;

import java.util.Comparator;

/**
 * 类简要描述
 * <p>
 * <p>
 * 类详细描述
 * </p>
 *
 * @author duanbokan
 */

public class CountryComparator implements Comparator<ContactBean.ObjectBean> {

    @Override
    public int compare(ContactBean.ObjectBean o1, ContactBean.ObjectBean o2) {
        if (TextUtils.isEmpty(o1.getContactNameX()) || TextUtils.isEmpty(o2.getContactNameX())) {
            return 1;
        }
        if ("@".equals(o1.getContactNameX()) || "#".equals(o2.getContactNameX())) {
            return -1;
        } else if ("#".equals(o1.getContactNameX()) || "@".equals(o2.getContactNameX())) {
            return 1;
        } else {
            return o1.getContactNameX().compareTo(o2.getContactNameX());
        }
    }
}
