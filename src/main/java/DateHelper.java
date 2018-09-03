package main.java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    String dateFormat = "yyyy-MM-dd HH:mm:ss";
    public static void main(String[] args) {
        DateHelper dateHelper = new DateHelper();
        Date fromDate = dateHelper.parseStringToDate("2009-09-22 16:47:08", dateHelper.dateFormat);
        Date toDate = dateHelper.parseStringToDate("2009-09-22 16:47:20", dateHelper.dateFormat);
        dateHelper.getSecondBetweenTwoDate(fromDate, toDate);
    }

    public void getSecondBetweenTwoDate(Date fromDate, Date toDate) {
        long seconds = (toDate.getTime() - fromDate.getTime())/1000;
        System.out.println("Seconds : " + seconds);
    }

    public Date parseStringToDate(String strDate, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            Date date = formatter.parse(strDate);
            System.out.println(date);
            System.out.println(formatter.format(date));
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void compareDate(String fromDate, String toDate) {
        if (fromDate.compareToIgnoreCase(toDate) < 0) {
            System.out.println("Result : " + fromDate.compareToIgnoreCase(toDate));
        } else if (fromDate.compareToIgnoreCase(fromDate) == 0) {
            System.out.println("Result : " + fromDate.compareToIgnoreCase(toDate));
        } else {
            System.out.println("Result : " + fromDate.compareToIgnoreCase(toDate));
        }
    }
}
