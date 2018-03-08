package com.example.bo.nixon.bean;

/**
 * @author bo.
 * @Date 2017/5/8.
 * @desc
 */

public class LoginResponseBean {

    /**
     * object : {"customerId":1321,"loginName":null,"customerName":null,"mobile":null,"email":"xjqcljsy@126.com"}
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
         * customerId : 1321
         * loginName : null
         * customerName : null
         * mobile : null
         * email : xjqcljsy@126.com
         */

        private int customerId;
        private Object loginName;
        private Object customerName;
        private Object mobile;
        private String email;

        public int getCustomerId () {
            return customerId;
        }

        public void setCustomerId (int customerId) {
            this.customerId = customerId;
        }

        public Object getLoginName () {
            return loginName;
        }

        public void setLoginName (Object loginName) {
            this.loginName = loginName;
        }

        public Object getCustomerName () {
            return customerName;
        }

        public void setCustomerName (Object customerName) {
            this.customerName = customerName;
        }

        public Object getMobile () {
            return mobile;
        }

        public void setMobile (Object mobile) {
            this.mobile = mobile;
        }

        public String getEmail () {
            return email;
        }

        public void setEmail (String email) {
            this.email = email;
        }
    }
}
