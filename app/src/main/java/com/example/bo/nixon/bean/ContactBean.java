package com.example.bo.nixon.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/1.
 * @desc
 */

public class ContactBean {

    /**
     * object : [{"id":1,"customerId":64,"contactName":"老大","phoneNumber":"123","isDeleted":0,"forbiddenDatetime":null,"createDatetime":1496285541000},{"id":2,"customerId":64,"contactName":"老二","phoneNumber":"234","isDeleted":0,"forbiddenDatetime":null,"createDatetime":1496285579000},{"id":4,"customerId":64,"contactName":"老wu","phoneNumber":"17324450708","isDeleted":0,"forbiddenDatetime":1496286276000,"createDatetime":1496287378000}]
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
        public ObjectBean (String contactNameX, String phoneNumber) {
            this.contactNameX = contactNameX;
            this.phoneNumber = phoneNumber;
        }

        /**
         * id : 1
         * customerId : 64
         * contactName : 老大
         * phoneNumber : 123
         * isDeleted : 0
         * forbiddenDatetime : null
         * createDatetime : 1496285541000
         */
        private int id;
        private int customerId;
        @SerializedName ("contactName") private String contactNameX;
        private String phoneNumber;
        private int isDeleted;
        private Object forbiddenDatetime;
        private long createDatetime;
        private boolean isChecked;

        public boolean isChecked () {
            return isChecked;
        }

        public void setChecked (boolean checked) {
            isChecked = checked;
        }

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

        public String getContactNameX () {
            return contactNameX;
        }

        public void setContactNameX (String contactNameX) {
            this.contactNameX = contactNameX;
        }

        public String getPhoneNumber () {
            return phoneNumber;
        }

        public void setPhoneNumber (String phoneNumber) {
            this.phoneNumber = phoneNumber;
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

        @Override public String toString () {
            return "ObjectBean{"
                + "id="
                + id
                + ", customerId="
                + customerId
                + ", contactNameX='"
                + contactNameX
                + '\''
                + ", phoneNumber='"
                + phoneNumber
                + '\''
                + ", isDeleted="
                + isDeleted
                + ", forbiddenDatetime="
                + forbiddenDatetime
                + ", createDatetime="
                + createDatetime
                + ", isChecked="
                + isChecked
                + '}';
        }
    }

}
