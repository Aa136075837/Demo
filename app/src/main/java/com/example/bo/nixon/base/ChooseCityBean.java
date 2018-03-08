package com.example.bo.nixon.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/16.
 * @desc
 */

public class ChooseCityBean {

    /**
     * object : [{"id":1,"shortName":"GMT","city":"London","cityCN":"伦敦","country":"UK","countryCN":"英国","timezone":"+00:00"},{"id":2,"shortName":"CET","city":"Central European","cityCN":"中欧","country":"Central Europe","countryCN":"中欧","timezone":"+01:00"},{"id":3,"shortName":"EET","city":"Eastern European","cityCN":"东欧","country":"Eastern European","countryCN":"东欧","timezone":"+02:00"},{"id":4,"shortName":"EAT","city":"Arabia Standard Time","cityCN":"阿拉伯标准时间","country":"Middle East","countryCN":"中东","timezone":"+03:00"},{"id":5,"shortName":"GST","city":"Gulf Standard Time","cityCN":"海湾标准时间","country":"Middle East","countryCN":"中东","timezone":"+04:00"},{"id":6,"shortName":"MVT","city":"Maldives/Pakistan Standard Time","cityCN":"马尔代夫/巴基斯坦标准时间","country":"Pakistan","countryCN":"巴基斯坦","timezone":"+05:00"},{"id":7,"shortName":"BST","city":"Bangladesh Standard Time","cityCN":"孟加拉国时间","country":"Bangladesh","countryCN":"孟加拉国","timezone":"+06:00"},{"id":8,"shortName":"ICT","city":"Indochina Standard Time","cityCN":"印度支那标准时间","country":"Asia","countryCN":"亚洲","timezone":"+07:00"},{"id":9,"shortName":"HKT","city":"Hong Kong Standard Time","cityCN":"香港标准时间","country":"China","countryCN":"中国","timezone":"+08:00"},{"id":10,"shortName":"JST","city":"Japan Standard Time","cityCN":"日本标准时间","country":"Japan","countryCN":"日本","timezone":"+09:00"},{"id":11,"shortName":"AEST","city":"Australian Eastern Standard Time","cityCN":"东部标准时间","country":"Australia","countryCN":"澳大利亚","timezone":"+10:00"},{"id":12,"shortName":"VUT","city":"Vanuatu Standard Time","cityCN":"瓦努阿图标准时间","country":"South Pacific","countryCN":"南太平洋","timezone":"+11:00"},{"id":13,"shortName":"NZST","city":"New Zealand Standard Time","cityCN":"新西兰标准时间","country":"New Zealand","countryCN":"新西兰","timezone":"+12:00"},{"id":14,"shortName":"SST","city":"Samoa Standard Time","cityCN":"萨摩亚标准时间","country":"Samoa","countryCN":"萨摩亚","timezone":"-11:00"},{"id":15,"shortName":"HADT","city":"Hawaii-Aleutian Standard Time","cityCN":"夏威夷标准时间","country":"Hawaii","countryCN":"夏威夷","timezone":"-10:00"},{"id":16,"shortName":"AKST","city":"Alaskan Standard Time","cityCN":"阿拉斯加标准时间","country":"USA","countryCN":"美国","timezone":"-09:00"},{"id":17,"shortName":"PST","city":"Pacific Standard Time","cityCN":"太平洋标准时间","country":"USA","countryCN":"美国","timezone":"-08:00"},{"id":18,"shortName":"MST","city":"Mountain Standard Time","cityCN":"山区标准时间","country":"USA","countryCN":"美国","timezone":"-07:00"},{"id":19,"shortName":"CST","city":"Central Standard Time","cityCN":"中央标准时间","country":"USA","countryCN":"美国","timezone":"-06:00"},{"id":20,"shortName":"EST","city":"Eastern Standard Time","cityCN":"（美国）东部时间","country":"USA","countryCN":"美国","timezone":"-05:00"},{"id":21,"shortName":"AST","city":"Atlantic Standard Time","cityCN":"大西洋标准时间","country":"Caribbean","countryCN":"加勒比海","timezone":"-04:00"},{"id":22,"shortName":"BRT","city":"Brazil Standard Time ","cityCN":"巴西标准时间","country":"Brazil","countryCN":"巴西","timezone":"-03:00"},{"id":23,"shortName":"FNT","city":"Fernando de Noronha Time","cityCN":"费尔南多-迪诺罗尼亚时间","country":"Brazil","countryCN":"巴西","timezone":"-02:00"},{"id":24,"shortName":"AZOT","city":"Azores Time","cityCN":"亚速尔群岛时间","country":"Azores","countryCN":"亚速尔群岛","timezone":"-01:00"}]
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

    public static class ObjectBean implements Serializable{
        /**
         * id : 1
         * shortName : GMT
         * city : London
         * cityCN : 伦敦
         * country : UK
         * countryCN : 英国
         * timezone : +00:00
         */

        private int id;
        private String shortName;
        private String city;
        private String cityCN;
        private String country;
        private String countryCN;
        private String timezone;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public String getShortName () {
            return shortName;
        }

        public void setShortName (String shortName) {
            this.shortName = shortName;
        }

        public String getCity () {
            return city;
        }

        public void setCity (String city) {
            this.city = city;
        }

        public String getCityCN () {
            return cityCN;
        }

        public void setCityCN (String cityCN) {
            this.cityCN = cityCN;
        }

        public String getCountry () {
            return country;
        }

        public void setCountry (String country) {
            this.country = country;
        }

        public String getCountryCN () {
            return countryCN;
        }

        public void setCountryCN (String countryCN) {
            this.countryCN = countryCN;
        }

        public String getTimezone () {
            return timezone;
        }

        public void setTimezone (String timezone) {
            this.timezone = timezone;
        }
    }
}
