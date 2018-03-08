package com.example.bo.nixon.bean;

/**
 * @author bo.
 * @Date 2017/7/12.
 * @desc
 */

public class UploadStepsBean {

    /**
     * object : {"id":16,"deviceId":"ddddd","step":456,"distance":34886.052,"calori":13.75,"timestamp":1491044400000,"createDatetime":1497351507929}
     * code : 00000000
     * info : 成功
     */

    private ObjectBean object;
    private String code;
    private String info;

    public ObjectBean getObject () {
        return object;
    }

    public void setObject (ObjectBean object) {
        this.object = object;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public String getInfo () {
        return info;
    }

    public void setInfo (String info) {
        this.info = info;
    }

    public static class ObjectBean {
        /**
         * id : 16
         * deviceId : ddddd
         * step : 456
         * distance : 34886.052
         * calori : 13.75
         * timestamp : 1491044400000
         * createDatetime : 1497351507929
         */

        private int id;
        private String deviceId;
        private int step;
        private double distance;
        private double calori;
        private long timestamp;
        private long createDatetime;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public String getDeviceId () {
            return deviceId;
        }

        public void setDeviceId (String deviceId) {
            this.deviceId = deviceId;
        }

        public int getStep () {
            return step;
        }

        public void setStep (int step) {
            this.step = step;
        }

        public double getDistance () {
            return distance;
        }

        public void setDistance (double distance) {
            this.distance = distance;
        }

        public double getCalori () {
            return calori;
        }

        public void setCalori (double calori) {
            this.calori = calori;
        }

        public long getTimestamp () {
            return timestamp;
        }

        public void setTimestamp (long timestamp) {
            this.timestamp = timestamp;
        }

        public long getCreateDatetime () {
            return createDatetime;
        }

        public void setCreateDatetime (long createDatetime) {
            this.createDatetime = createDatetime;
        }
    }
}
