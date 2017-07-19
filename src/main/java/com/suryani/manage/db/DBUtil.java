package com.suryani.manage.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {

    /**
     * 将以下划线分隔的多个全部为大写字母的单词转换为JAVA BEAN的命名格式的单词
     * 
     * 如：TOWN_NAME将转换为townName
     * 
     * @param field
     *            大写的由多个单词组成的字段名
     * @return JAVA BEAN格式的单词
     */
    public static String toBeanField(String field) {
        // assertIt(field.trim().length() > 0);
        if (field == null) {
            return null;
        }
        field = field.toLowerCase();
        String[] words = field.split("_");
        String result = words[0];
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            result += word.substring(0, 1).toUpperCase();
            if (word.length() > 1) {
                result += word.substring(1, word.length());
            }
        }
        return result;
    }

    /**
     * 将以下划线分隔的多个全部为大写字母的单词转换为JAVA BEAN的命名格式的单词
     * 
     * 如：TOWN_NAME将转换为townName
     * 
     * @param field
     *            大写的由多个单词组成的字段名
     * @return JAVA BEAN格式的单词
     */
    public static String[] toBeanField(String[] fields) {
        String[] result = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            result[i] = toBeanField(fields[i]);
        }
        return result;
    }

    /**
     * 获取JDBC记录集的列名
     * 
     * @param rs
     *            JDBC记录集
     * @param useBeanName
     *            是否使用JAVA BEAN的命名格式返回字段名 JAVA BEAN的命名格式为：townName
     * @return 记录集中所有的字段名
     */
    public static String[] getColumnNames(ResultSet rs, boolean useBeanName) {
        try {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            String[] colNames = new String[colCount];

            for (int i = 0; i < colCount; i++) {
                String col = meta.getColumnLabel(i + 1);
                if (useBeanName) {
                    col = toBeanField(col);
                }
                colNames[i] = col;
            }

            return colNames;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从rs中读取记录
     * 
     * @param rs
     * @param useBeanField
     * @return
     */
    public static List<Map<String, String>> executeSQLQuery(ResultSet rs, boolean useBeanName) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        try {
            String[] colNames = getColumnNames(rs, useBeanName);
            while (rs.next()) {
                Map<String, String> record = new HashMap<String, String>();
                for (int i = 0; i < colNames.length; i++) {
                    String colName = colNames[i];
                    String colValue = rs.getString(i + 1);
                    record.put(colName, colValue);
                }
                result.add(record);
            }
        } catch (Exception e) {

        } finally {

        }
        return result;
    }

}
