package com.ivione93.mynavapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String toString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }

    public static Date toDate(String date)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date myDate = null;
        try {
            myDate = format.parse(date);
        }
        catch (ParseException ex)
        {
            System.out.println(ex);
        }
        return myDate;
    }
}
