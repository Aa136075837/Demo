package com.smart.otacomponent;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/4/11 19:00
 * @说明
 */
public class UpdateHelper {

    private static final String TAG = "UpdateHelper";

    private Activity mActivity;

    /**
     * OTAFileMessage 为 bin文件信息
     */
    private List<OTAFileMessage> mFileMessage = new ArrayList<>();

    private static final int DATA_LENGTH = 16;

    private static int mStatus = 0;

    private UpdateInterface mUpdateInterface;

    private String mPath = "";

    private long mPart = 0;
    private long mLength = 0;
    private int mFileCount = 0;

    public UpdateHelper(String binPath) {
        mPath = binPath;
    }


    /**
     * 处理文件，拿出bin文件的信息
     */
    public void dealFile() {
        mFileMessage.clear();
        Log.i(TAG, "startUpdate ::  " + " start...");
        boolean result = dealFiles();
        Log.i(TAG, "startUpdate ::  " + " dealFiles result " + result + " file count:" + mFileMessage.size());
        if (result && mFileMessage.size() > 0) {
            Log.i(TAG, "startUpdate ::  " + result + "    or   " + mFileMessage.size());
            String messageBuffer = "";
            for (OTAFileMessage message : mFileMessage) {
                messageBuffer = messageBuffer + message.toString() + " \n\n ";
            }
            if (null != mUpdateInterface) {
                mUpdateInterface.dealFileSuccessful(getMainFileMessage());
            }
        }
    }

    private boolean dealFiles() {
        File file = new File(mPath);
        boolean result = false;
        if (file.exists()) {
            File[] files = file.listFiles();
            sortFiles(files);
            for (int i = 0; i < files.length; i++) {
                File binFile = files[i];
                if (binFile.getAbsolutePath().endsWith("bin")) {
                    dealFileMessage(binFile);
                    if (i == files.length - 1) {
                        result = true;
                    }
                } else {
                    mFileMessage.clear();
                    break;
                }
            }
        }
        return result;
    }

    private void sortFiles(File[] files) {
        List<File> fileArrayList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            fileArrayList.add(files[i]);
        }
        Collections.sort(fileArrayList, new SortFile());
        for (int i = 0; i < fileArrayList.size(); i++) {
            files[i] = fileArrayList.get(i);
        }
    }

    private class SortFile implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            String fileName1 = lhs.getName();
            String fileName2 = rhs.getName();
            int index1;
            int index2;
            index1 = Integer.valueOf(fileName1.substring(5, fileName1.length() - 4));
            index2 = Integer.valueOf(fileName2.substring(5, fileName2.length() - 4));
            if (index1 == index2) {
                return 0;
            } else if (index1 > index2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private void dealFileMessage(File binFile) {
        long longLength = binFile.length();
        int length = new Long(longLength).intValue();
        byte[] bytes = readBinByOffset(binFile, length);
        int count = bytes[8] & 0xff;
        int dataCount = length / DATA_LENGTH;
        int id = (bytes[0] & 0xff) + ((bytes[1] & 0xff) << 8) + ((bytes[2] & 0xff) << 16) + ((bytes[3] & 0xff) << 24);
        if (length % DATA_LENGTH > 0) {
            dataCount++;
        }
        mLength += longLength;
        OTAFileMessage message = new OTAFileMessage.Builder()
                .bytes(bytes)
                .id(id)
                .mainVersion((bytes[6] & 0xff))
                .minorVersion((bytes[5] & 0xff))
                .testVersion((bytes[4] & 0xff))
                .childFiles(count)
                .fileLength(length)
                .dataCount(dataCount)
                .build();
        Log.w(TAG,"dealFileMessage--->" + message.toString());
        mFileMessage.add(message);
    }

    private byte[] readBinByOffset(File file, int length) {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[length];
            inputStream.skip(0);
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder("");
        byte tempByte;
        String tempString;
        for (int j = 0; j < bytes.length; j++) {
            tempByte = bytes[j];
            if (tempByte >= 0 && tempByte < 16) {
                sb.append("0");
            }
            tempString = Integer.toHexString((bytes[j] & 0xff));
            sb.append(tempString.length() == 1 ? tempString : tempString.subSequence(0, 2));
            sb.append(" ");
            if ((j + 1) % 16 == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public OTAFileMessage getOTAMessageById(int index) {
        for (OTAFileMessage message : mFileMessage) {
            if (message.getmId() == index) {
                return message;
            }
        }
        return null;
    }

    /**
     * 获取bin文件的第index包的数据
     *
     * @param message bin文件信息
     * @param index
     * @return
     */
    public byte[] getDataByIndex(OTAFileMessage message, int index) {
        byte[] bytes = message.getmBytes();
        byte[] dataBytes = new byte[DATA_LENGTH];
        int start = 0;
        int end = 0;

        if (message.getmDataCount() - 1 == index) {
            start = DATA_LENGTH * index;
            end = message.getmFileLength() - 1;
        } else if (message.getmDataCount() > index) {
            start = DATA_LENGTH * index;
            end = DATA_LENGTH * (index + 1) - 1;
        } else {
            return dataBytes;
        }
        for (int i = 0; i <= end - start; i++) {
            dataBytes[i] = bytes[start + i];
        }
        Log.i(TAG, "getDataByIndex ::  " + " 数据序号 :" + index + "  数据：" + byteToString(dataBytes));
        return dataBytes;
    }

    public interface UpdateInterface {

        void dealFileSuccessful(OTAFileMessage message);
    }

    public void setUpdateListener(UpdateInterface updateListener) {
        mUpdateInterface = updateListener;
    }

    public List<OTAFileMessage> getmFileMessage() {
        return mFileMessage;
    }

    public long getmLength() {
        return mLength;
    }

    public OTAFileMessage getMainFileMessage() {
        for (OTAFileMessage message : mFileMessage) {
            if (64 == message.getmFileLength()) {
                return message;
            }
        }
        return null;
    }

    public int getFileCount() {
        if (0 != mFileCount)
            return mFileCount;
        for (OTAFileMessage message : mFileMessage) {
            mFileCount += message.getmDataCount();
        }
        return mFileCount;
    }

    public void initHadSend(OTAFileMessage message) {
        for (OTAFileMessage otaFileMessage : mFileMessage) {
            if (otaFileMessage.getmId() == message.getmId())
                return;
            else{
                otaFileMessage.setHasSend(true);
            }
        }
    }

    public int getHasSendCount(OTAFileMessage tempMessage, int index) {
        int count = 0;
        for (OTAFileMessage message : mFileMessage) {
            if (message != tempMessage && message.isHasSend()) {
                count += message.getmDataCount();
            } else if (null != message && message == tempMessage && message.isHasSend()) {
                if (0 != index)
                    index = index - 1;
                count += index;
            }
        }
        return  count;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }
}
