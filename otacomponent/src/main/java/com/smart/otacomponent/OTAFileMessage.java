package com.smart.otacomponent;

/**
 * @author ARZE
 * @version 创建时间：2017/4/11 19:15
 * @说明
 */
public class OTAFileMessage {

    /**
     * 镜像id
     */
    private int mId;

    /**
     * 镜像版本
     */

    private int mMainVersion;

    private int mMinorVersion;

    private int mTestVersion;

    /**
     *子镜像文件数
     */
    private int mChildFiles;

    /**
     * 文件长度
     */
    private int mFileLength;

    /**
     * 文件数据
     */
    private byte[] mBytes;


    private int mDataCount;

    private boolean hasSend = false;

    private OTAFileMessage(int id , int mainVersion , int minorVersion,
                           int testVersion , int childFiles, int fileLength, byte[] bytes, int datCount) {
        mId = id;
        mMainVersion = mainVersion;
        mMinorVersion = minorVersion;
        mTestVersion = testVersion;
        mChildFiles = childFiles;
        mFileLength = fileLength;
        mBytes = bytes;
        mDataCount = datCount;
    }

    public static class Builder {

        private int id = 0;

        private int mainVersion = 0;

        private int minorVersion = 0;

        private int testVersion = 0 ;

        private int childFiles = 0;

        private int fileLength = 0;

        private byte[] bytes;

        private int dataCount = 0;

        public Builder () {

        }

        public Builder id (int id ) {
            this.id = id;
            return this;
        }

        public Builder mainVersion(int mainVersion) {
            this.mainVersion = mainVersion;
            return this;
        }

        public Builder minorVersion(int minorVersion) {
            this.minorVersion = minorVersion;
            return this;
        }

        public Builder testVersion(int testVersion) {
            this.testVersion = testVersion;
            return this;
        }

        public Builder childFiles(int childFiles) {
            this.childFiles = childFiles;
            return this;
        }

        public Builder fileLength(int fileLength) {
            this.fileLength = fileLength;
            return this;
        }

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public Builder dataCount(int dataCount) {
            this.dataCount = dataCount;
            return this;
        }

        public OTAFileMessage build () {
            return new OTAFileMessage(id,mainVersion,minorVersion,
                    testVersion,childFiles,fileLength,bytes,dataCount);
        }
    }

    public int getmId() {
        return mId;
    }


    public int getmMinorVersion() {
        return mMinorVersion;
    }


    public int getmMainVersion() {
        return mMainVersion;
    }


    public int getmTestVersion() {
        return mTestVersion;
    }


    public int getmChildFiles() {
        return mChildFiles;
    }

    public void setmChildFiles(int mClildFiles) {
        this.mChildFiles = mClildFiles;
    }

    public int getmFileLength() {
        return mFileLength;
    }


    public byte[] getmBytes() {
        return mBytes;
    }


    public int getmDataCount() {
        return mDataCount;
    }

    public boolean isHasSend() {
        return hasSend;
    }

    public void setHasSend(boolean hasSend) {
        this.hasSend = hasSend;
    }

    @Override
    public String toString() {
        return "OTAFileMessage{" +
                "mId=" + mId +
                ", mMainVersion=" + mMainVersion +
                ", mMinorVersion=" + mMinorVersion +
                ", mTestVersion=" + mTestVersion +
                ", mChildFiles=" + mChildFiles +
                ", mFileLength=" + mFileLength +
                ", mBytes(length)=" + mBytes.length +
                ", mDataCount=" + mDataCount +
                '}';
    }
}
