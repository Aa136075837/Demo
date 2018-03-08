package com.example.bo.nixon.bean;

/**
 * @author bo.
 * @Date 2017/6/28.
 * @desc
 */

public class AddALarmNetBean {

    /**
     * object : {"id":18,"customerId":80,"eventReminder":"qqq","hour":9,"min":null,"isOpen":null,"noon":null,"cycle":null}
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
         * id : 18
         * customerId : 80
         * eventReminder : qqq
         * hour : 9
         * min : null
         * isOpen : null
         * noon : null
         * cycle : null
         */

        private int id;
        private int customerId;
        private String eventReminder;
        private int hour;
        private int min;
        private int isOpen;
        private String noon;
        private String cycle;

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
    }
}
