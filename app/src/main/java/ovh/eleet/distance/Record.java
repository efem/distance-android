package ovh.eleet.distance;

import java.util.Date;

/**
 * Created by Marek on 11.05.2017.
 */

public class Record {
    private Date date;
    private double distance;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Record{" +
                "date=" + date +
                ", distance=" + distance +
                '}';
    }
}
