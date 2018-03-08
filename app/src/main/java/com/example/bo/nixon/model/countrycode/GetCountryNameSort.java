package com.example.bo.nixon.model.countrycode;

/**
 * Created by admin on 2016/7/22.
 */

import android.util.Log;
import com.example.bo.nixon.base.ChooseCityBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 取姓名首字母及模糊匹配查询
 * <p>
 * <p>
 * 类详细描述
 * </p>
 *
 * @author duanbokan
 */

public class GetCountryNameSort {

    CharacterParserUtil characterParser = CharacterParserUtil.getInstance ();

    String chReg = "[\\u4E00-\\u9FA5]+";// 中文字符串匹配

    /***
     * 将名字转化为拼音并获得首字母
     *
     * @param name
     * @return
     */
    public String getSortLetter (String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        // 汉字转换成拼音
        String pinyin = characterParser.getSelling (name);
        String sortString = pinyin.substring (0, 1).toUpperCase (Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches ("[A-Z]")) {
            letter = sortString.toUpperCase (Locale.CHINESE);
        }
        return letter;
    }

    /***
     * 取首字母
     *
     * @param sortKey
     * @return
     */
    public String getSortLetterBySortKey (String sortKey) {
        if (sortKey == null || "".equals (sortKey.trim ())) {
            return null;
        }
        String letter = "#";
        // 汉字转换成拼音
        String sortString = sortKey.trim ().substring (0, 1).toUpperCase (Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches ("[A-Z]")) {
            letter = sortString.toUpperCase (Locale.CHINESE);
        }
        return letter;
    }

    /***
     * 根据输入内容进行查询
     *
     * @param str
     *            输入内容
     * @param list
     *            需要查询的List
     * @return 查询结果 list
     */
    public List<ChooseCityBean.ObjectBean> search (String str, List<ChooseCityBean.ObjectBean> list) {
        List<ChooseCityBean.ObjectBean> filterList = new ArrayList<ChooseCityBean.ObjectBean> ();// 过滤后的list
        // if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
        if (str.matches ("[a-zA-Z]{1,100}")) {// 输入英文
            String simpleStr = str.toLowerCase ();
            for (ChooseCityBean.ObjectBean contact : list) {
                if (contact.getCity () != null && contact.getCountry () != null && contact.getShortName () != null) {
                    if (contact.getCity ().toLowerCase ().contains (simpleStr) || contact.getCountry ().toLowerCase ().contains (simpleStr) || contact
                        .getShortName ().toLowerCase ()
                        .contains (simpleStr)) {
                        if (!filterList.contains (contact)) {
                            filterList.add (contact);
                        }
                    }
                }
            }
        } else { //输入汉字
            for (ChooseCityBean.ObjectBean contact : list) {
                if (contact.getCity () != null) {
                    // 姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    if (contact.getCountryCN ().toLowerCase (Locale.CHINESE).contains (str.toLowerCase (Locale.CHINESE))
                        || contact.getCityCN ().toLowerCase (Locale.CHINESE).contains (str.toLowerCase (Locale.CHINESE))
                        || contact.getCityCN ().toLowerCase (Locale.CHINESE) .replace (" ", "").contains (str.toLowerCase (Locale.CHINESE))
                        || contact.getShortName ().toLowerCase (Locale.CHINESE).contains (str.toLowerCase (Locale.CHINESE))) {
                        if (!filterList.contains (contact)) {
                            filterList.add (contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }
}

