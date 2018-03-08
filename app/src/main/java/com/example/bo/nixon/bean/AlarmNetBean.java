package com.example.bo.nixon.bean;

import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/24.
 * @desc
 */

public class AlarmNetBean {

    /**
     * object : [{"id":2,"customerId":64,"eventReminder":"22","hour":33,"min":44,"isOpen":0,"noon":"pm","cycle":"1111111","isDeleted":0,"forbiddenDatetime":null,"createDatetime":1496286979000},{"id":3,"customerId":64,"eventReminder":"222","hour":333,"min":444,"isOpen":0,"noon":"am","cycle":"1010111","isDeleted":0,"forbiddenDatetime":1496287017000,"createDatetime":1496287019000},{"id":4,"customerId":64,"eventReminder":"test","hour":3,"min":6,"isOpen":0,"noon":"am","cycle":"0000000","isDeleted":0,"forbiddenDatetime":1496287258000,"createDatetime":1496287258000}]
     * code : 00000000
     * info : 成功
     */

    private String code;
    private String info;
    private List<ObjectBean> object;

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

    public List<ObjectBean> getObject () {
        return object;
    }

    public void setObject (List<ObjectBean> object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * id : 2
         * customerId : 64
         * eventReminder : 22
         * hour : 33
         * min : 44
         * isOpen : 0
         * noon : pm
         * cycle : 1111111
         * isDeleted : 0
         * forbiddenDatetime : null
         * createDatetime : 1496286979000
         */

        private int id;
        private int customerId;
        private String eventReminder;
        private int hour;
        private int min;
        private int isOpen;
        private String noon;
        private String cycle;
        private int isDeleted;
        private Object forbiddenDatetime;
        private long createDatetime;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public int getCustomerId () {
            return customerId;
        }

        public void setCustomerId (int customerId) {
            this.customerId = customerId;
        }

        public String getEventReminder () {
            return eventReminder;
        }

        public void setEventReminder (String eventReminder) {
            this.eventReminder = eventReminder;
        }

        public int getHour () {
            return hour;
        }

        public void setHour (int hour) {
            this.hour = hour;
        }

        public int getMin () {
            return min;
        }

        public void setMin (int min) {
            this.min = min;
        }

        public int getIsOpen () {
            return isOpen;
        }

        public void setIsOpen (int isOpen) {
            this.isOpen = isOpen;
        }

        public String getNoon () {
            return noon;
        }

        public void setNoon (String noon) {
            this.noon = noon;
        }

        public String getCycle () {
            return cycle;
        }

        public void setCycle (String cycle) {
            this.cycle = cycle;
        }

        public int getIsDeleted () {
            return isDeleted;
        }

        public void setIsDeleted (int isDeleted) {
            this.isDeleted = isDeleted;
        }

        public Object getForbiddenDatetime () {
            return forbiddenDatetime;
        }

        public void setForbiddenDatetime (Object forbiddenDatetime) {
            this.forbiddenDatetime = forbiddenDatetime;
        }

        public long getCreateDatetime () {
            return createDatetime;
        }

        public void setCreateDatetime (long createDatetime) {
            this.createDatetime = createDatetime;
        }
    }
}
