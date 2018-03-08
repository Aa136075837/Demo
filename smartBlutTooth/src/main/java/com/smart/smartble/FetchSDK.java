package com.smart.smartble;

import android.support.annotation.NonNull;

import com.smart.smartble.c007.ProtocolC007;
import com.smart.smartble.c007.SmartActionOperatorC007;
import com.smart.smartble.client.IClient;
import com.smart.smartble.c007.SmartClientC007;
import com.smart.smartble.client.SmartClientSDK1;
import com.smart.smartble.client.protocolImp.IProtocol;
import com.smart.smartble.event.ISmartActionOperator;
import com.smart.smartble.service.IService;
import com.smart.smartble.service.SmartService;
import com.smart.smartble.smartBle.BleHelper;

/**
 * @author ARZE
 * @version 创建时间：2016/12/14 9:33
 * @说明 版本识别类,
 */
public class FetchSDK {

    public static void fetch(@NonNull SmartManager smartManager, BleHelper bleHelper, boolean init) {
        IClient iClient = smartManager.getIClient();
        IService iService = smartManager.getIService();
        ISmartActionOperator  iSmartActionOperator= smartManager.getISmartActionOperator();
        if (false) {
            if (false) {
                if (null == iClient || !(iClient instanceof SmartClientSDK1)) {
                    iClient = new SmartClientSDK1(bleHelper);
                    iService = new SmartService();
                }
            } else {
                if (null == iClient || !(iClient instanceof SmartClientC007)) {
                    iClient = new SmartClientC007(bleHelper);
                    iService = new SmartService();
                }
            }
        } else {
            iClient = new SmartClientC007(bleHelper);
            iService = new SmartService();
            iSmartActionOperator = new SmartActionOperatorC007();

        }
        smartManager.setIClient(iClient);
        smartManager.setIService(iService);
        smartManager.setISmartActionOperator(iSmartActionOperator);
    }

    public static IProtocol fetchProtocol() {
        return ProtocolC007.getInstance();
    }

}
