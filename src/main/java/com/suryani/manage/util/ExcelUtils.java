package com.suryani.manage.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

public class ExcelUtils {

    public static String getNumbericValue(HSSFCell cell, boolean bdouble) {
        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            DecimalFormat df = new DecimalFormat();
            if (bdouble) {
                df.applyPattern("#.##");
            } else {
                df.applyPattern("#");
            }
            return df.format(cell.getNumericCellValue());
        }
        return "0";
    }

    public static String getValue(HSSFCell cell, String def) {
        String rt = getValue(cell);
        if (rt.trim().equals("")) {
            return def;
        }
        return rt;
    }

    public static String getValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                    cellvalue = sdf.format(date);

                } else {
                    DecimalFormat df = new DecimalFormat("#");
                    cellvalue = df.format(cell.getNumericCellValue());
                }
                break;
            }
            case HSSFCell.CELL_TYPE_STRING:
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellvalue = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

}
