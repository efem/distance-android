package ovh.eleet.distance.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Marek on 21.05.2017.
 */

public class PrepareStatement {
    private static final String URL = "http://www.31337.ovh:8080/distance/";
    public static String prepareRangeStatement(String fromRange, String toRange) {
        return URL + "showRecordsByDistance?from=" + fromRange + "&to=" + toRange;
    }

    public static String prepareDateStatement(String fromDate, String toDate) {
        return URL + "showRecordsByDate?from=" + fromDate + "&to=" + toDate;
    }

    public static String prepareDateStatementMinusDay(int numberOfDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Calendar fromCalendar = Calendar.getInstance();
        Calendar toCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DAY_OF_MONTH, -numberOfDays);
        return URL + "showRecordsByDate?from=" + sdf.format(fromCalendar.getTime()) + "&to="
                + sdf.format(toCalendar.getTime());
    }


    public static String prepareBrowseAllStatement(String pageNo, String pageSize) {
        return URL + "getRecordsByPage?pageNumber=" + pageNo + "&pageSize=" + pageSize;
    }
}
