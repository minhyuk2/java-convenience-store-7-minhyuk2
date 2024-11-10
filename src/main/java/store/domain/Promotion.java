package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static camp.nextstep.edu.missionutils.DateTimes.now;


public class Promotion {
    private String name;
    private int buy;
    private int get;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean nowOk = false;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nowOk = checkIfNowInRange();
    }

    private boolean checkIfNowInRange() {
        LocalDateTime currentNow = now();
        LocalDate currentDate = currentNow.toLocalDate();
        return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }

    public String getName() {
        return name;
    }
    public boolean getIsNowOk() {
        return nowOk;
    }
    public int getBuy() {
        return buy;
    }
    public int getGet() {
        return get;
    }

}
