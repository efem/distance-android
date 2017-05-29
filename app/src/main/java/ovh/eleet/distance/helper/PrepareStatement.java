package ovh.eleet.distance.helper;

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

    public static String prepareBrowseAllStatement(String pageNo, String pageSize) {
        return URL + "getRecordsByPage?pageNumber=" + pageNo + "&pageSize=" + pageSize;
    }
}
