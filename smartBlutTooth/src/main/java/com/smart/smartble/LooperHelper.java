package com.smart.smartble;

import android.content.Context;
import android.os.Handler;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 16:52
 * @说明
 */
public class LooperHelper extends Handler {

    public LooperHelper(Context context) {
        super(context.getMainLooper());
    }

}
