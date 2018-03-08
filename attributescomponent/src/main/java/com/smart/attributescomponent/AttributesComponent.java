package com.smart.attributescomponent;

import android.text.TextUtils;

import com.smart.smartble.DeviceCompat;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.utils.ByteHelper;

/**
 * @author ARZE
 * @version 创建时间：2017/6/20 15:53
 * @说明
 */
public class AttributesComponent extends AbAttributesComponent implements IAttributes {

    private IAttributesCompat mAttributesCompat;

    private static AttributesComponent mAttributesComponent;

    private AttributesComponent(SmartManager smartManager) {
        super(smartManager);
    }

    public static AttributesComponent getInstance(SmartManager smartManager) {
        if (null == mAttributesComponent) {
            synchronized (AttributesComponent.class) {
                if (null == mAttributesComponent) {
                    mAttributesComponent = new AttributesComponent(smartManager);
                }
            }
        }
        return mAttributesComponent;
    }

    @Override
    public void start() {

    }

    @Override
    public void dealAction(SmartAction action) {
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_GET_ELECTRICITY:
                int value = ByteHelper.calculateHigh(bytes[8]);
                dispatchPower(value);
                break;
            case REQUEST_ACTION_REQUEST_VERSION:
                if (compat().hasElectricity(action)) {
                    int electricity = compat().getElectricity(action);
                    dispatchPower(electricity);
                }
                int[] version = compat().getVersion(action);
                if (3 == version.length)
                    dispatchVersion(version[0], version[1], version[2]);
                break;
            case REQUEST_ACTION_GET_SN:
                if (!TextUtils.isEmpty(compat().saveSn(action))) {
                    dispatchSn(DeviceMessage.getInstance().getDeviceSn());
                }
                break;
        }
    }

    @Override
    public void getElectricity() {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_ELECTRICITY);
    }

    @Override
    public void changeBleName(String name) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_CHANGE_BLE_CAST, name);
    }

    @Override
    public void getVersion() {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_REQUEST_VERSION);
    }

    @Override
    public void getSn() {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_SN);
    }

    public IAttributesCompat compat() {
        if (DeviceCompat.isC007() && !(mAttributesCompat instanceof AttributesCompatC007)) {
            mAttributesCompat = new AttributesCompatC007();
        } else if (!DeviceCompat.isC007() && !(mAttributesCompat instanceof AttributesCompatC001)) {
            mAttributesCompat = new AttributesCompatC001();
        }
        return mAttributesCompat;
    }
}
