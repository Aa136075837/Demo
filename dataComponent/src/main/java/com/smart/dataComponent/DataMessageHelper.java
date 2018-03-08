package com.smart.dataComponent;

import android.util.Log;
import com.smart.dataComponent.data.Content;
import com.smart.dataComponent.data.ContentMessage;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/7/8 20:53
 * @说明
 */
public class DataMessageHelper {

    private Content mContent = new Content();
    private ContentMessage mCurrentMessage;

    public Content getmContent() {
        return mContent;
    }

    public void setmContent(Content mContent) {
        this.mContent = mContent;
    }

    public boolean isUploadComplete() {
        boolean complete = true;
        for (ContentMessage contentMessage : mContent.getmContentMessages()) {
            if (!contentMessage.isComplete()) {
                complete = false;
                break;
            }
        }
        return complete;
    }

    public int getUploadComplete() {
        int i = 0;
        for (ContentMessage contentMessage : mContent.getmContentMessages()) {
            if (!contentMessage.isComplete()) {
                break;
            } else {
                i++;
            }
        }
        return i;
    }

    public ContentMessage getNeedContentMessage() {
        ContentMessage message = null;
        for (ContentMessage contentMessage : mContent.getmContentMessages()) {
            if (!contentMessage.isComplete()) {
                message = contentMessage;
                break;
            }
        }
        mCurrentMessage = message;
        return message;
    }

    public ContentMessage getmCurrentMessage() {
        return mCurrentMessage;
    }

    public List<WatchBean> createWatchBean() {
        return mContent.createWatchBean();
    }

    public ContentMessage getDeleteMessage() {
        ContentMessage message = null;
        for (ContentMessage contentMessage : mContent.getmContentMessages()) {
            if (contentMessage.isComplete() && !contentMessage.isDelete()) {
                message = contentMessage;
                break;
            }
        }
        return message;
    }

    public boolean isDeleteComplete() {
        return mCurrentMessage.isDelete();
    }

    public boolean setMessageDelete(int utc, boolean b) {
        boolean result = false;
        for (ContentMessage contentMessage : mContent.getmContentMessages()) {
            Log.w("DataComponent", contentMessage.getmUtc () +":" + utc);
            if (!contentMessage.isDelete() && contentMessage.getmUtc() == utc) {
                contentMessage.setDelete(b);
                result = true;
                break;
            }
        }
        return result;
    }
}
