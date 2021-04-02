package com.ivione93.myworkout;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public static String calculatePartial(String time, String distance) {
        String sRitmo;
        BigDecimal bRitmo;
        float fTime = Float.parseFloat(time);
        float fDistance = Float.parseFloat(distance);
        float fRitmo = fTime / fDistance;
        bRitmo = new BigDecimal(fRitmo).setScale(2, RoundingMode.DOWN);
        int iRitmo = (int) fRitmo;

        String str = String.valueOf(bRitmo);
        int decNumberInt = Integer.parseInt(str.substring(str.indexOf('.') + 1));
        int sec = (60 * decNumberInt) / 100;

        sRitmo = iRitmo + "." + sec;

        return sRitmo;

    }
}
