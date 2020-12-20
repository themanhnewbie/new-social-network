package com.cost.free.Something.Utils;

import java.text.SimpleDateFormat;

public class FormatDateUtil {
    private static String dateFormat = "yyyy-MM-dd'T'HH:mm";
    public static SimpleDateFormat getDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat;
    }
}
