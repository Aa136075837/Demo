package com.example.bo.nixon.bean;

/**
 * @author bo.
 * @Date 2017/6/29.
 * @desc
 */

public class UserInfoBean {

    /**
     * object : {"customerId":77,"nickname":"smart","gender":1,"age":18,"weight":123488,"height":213.36,"job":"程序员","address":"中国深圳","location":"保安qu","icon":"","activityGoal":20915,"unit":0}
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
         * customerId : 77
         * nickname : smart
         * gender : 1
         * age : 18
         * weight : 123488
         * height : 213.36
         * job : 程序员
         * address : 中国深圳
         * location : 保安qu
         * icon :
         * activityGoal : 20915
         * unit : 0
         */

        private String customerId;
        private String nickname;
        private String gender;
        private String age;
        private String weight;
        private String height;
        private String job;
        private String address;
        private String location;
        private String icon;
        private String activityGoal;
        private String unit;

        public String getCustomerId () {
            return customerId;
        }

        public void setCustomerId (String customerId) {
            this.customerId = customerId;
        }

        public String getNickname () {
            return nickname;
        }

        public void setNickname (String nickname) {
            this.nickname = nickname;
        }

        public String getGender () {
            return gender;
        }

        public void setGender (String gender) {
            this.gender = gender;
        }

        public String getAge () {
            return age;
        }

        public void setAge (String age) {
            this.age = age;
        }

        public String getWeight () {
            return weight;
        }

        public void setWeight (String weight) {
            this.weight = weight;
        }

        public String getHeight () {
            return height;
        }

        public void setHeight (String height) {
            this.height = height;
        }

        public String getJob () {
            return job;
        }

        public void setJob (String job) {
            this.job = job;
        }

        public String getAddress () {
            return address;
        }

        public void setAddress (String address) {
            this.address = address;
        }

        public String getLocation () {
            return location;
        }

        public void setLocation (String location) {
            this.location = location;
        }

        public String getIcon () {
            return icon;
        }

        public void setIcon (String icon) {
            this.icon = icon;
        }

        public String getActivityGoal () {
            return activityGoal;
        }

        public void setActivityGoal (String activityGoal) {
            this.activityGoal = activityGoal;
        }

        public String getUnit () {
            return unit;
        }

        public void setUnit (String unit) {
            this.unit = unit;
        }
    }
}
