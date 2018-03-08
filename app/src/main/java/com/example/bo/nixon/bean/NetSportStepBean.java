package com.example.bo.nixon.bean;

import java.util.List;

/**
 * @author bo.
 * @Date 2017/7/12.
 * @desc
 */

public class NetSportStepBean {

    /**
     * object : [{"date":"2017-06-05","calori":0,"distance":0,"steps":650000},{"date":"2017-06-04","calori":0,"distance":0,"steps":4321}]
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
         * date : 2017-06-05
         * calori : 0
         * distance : 0
         * steps : 650000
         */

        private String date;
        private String calori;
        private String distance;
        private String steps;

        public String getDate () {
            return date;
        }

        public void setDate (String date) {
            this.date = date;
        }

        public String getCalori () {
            return calori;
        }

        public void setCalori (String calori) {
            this.calori = calori;
        }

        public String getDistance () {
            return distance;
        }

        public void setDistance (String distance) {
            this.distance = distance;
        }

        public String getSteps () {
            return steps;
        }

        public void setSteps (String steps) {
            this.steps = steps;
        }

        @Override public String toString () {
            return "ObjectBean{"
                + "date='"
                + date
                + '\''
                + ", calori='"
                + calori
                + '\''
                + ", distance='"
                + distance
                + '\''
                + ", steps='"
                + steps
                + '\''
                + '}';
        }
    }
}
