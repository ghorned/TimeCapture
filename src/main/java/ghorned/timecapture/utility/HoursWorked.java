package ghorned.timecapture.utility;

import ghorned.timecapture.entity.Employee;
import ghorned.timecapture.entity.Punch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoursWorked {

    public static Double getWeekHrs(Employee employee, LocalDateTime start, LocalDateTime end) {
        List<Punch> excludedPunches = new ArrayList<>();
        List<Punch> includedPunches = new ArrayList<>();

        for (Punch punch : employee.getPunches()) {
            if (punch.getTime().isBefore(start)) {
                excludedPunches.add(punch);
            }
            if ((punch.getTime().isEqual(start) || punch.getTime().isAfter(start)) &&
                    (punch.getTime().isEqual(end) || punch.getTime().isBefore(end))) {
                includedPunches.add(punch);
            }
        }

        PunchStatus startPunchStatus = excludedPunches.size() % 2 == 0 ? PunchStatus.OUT : PunchStatus.IN;
        PunchStatus endPunchStatus = (excludedPunches.size() + includedPunches.size()) % 2 == 0 ? PunchStatus.OUT : PunchStatus.IN;

        double hrs = 0.0;
        if (startPunchStatus == PunchStatus.OUT) {
            if (endPunchStatus == PunchStatus.OUT) {
                for (int i = 0; i < includedPunches.size(); i += 2) {
                    hrs += Duration.between(includedPunches.get(i).getTime(), includedPunches.get(i+1).getTime()).toMinutes() / 60.0;
                }
            } else {
                for (int i = 0; i < includedPunches.size() - 1; i += 2) {
                    hrs += Duration.between(includedPunches.get(i).getTime(), includedPunches.get(i+1).getTime()).toMinutes() / 60.0;
                }
                hrs += Duration.between(includedPunches.get(includedPunches.size() - 1).getTime(), end).toMinutes() / 60.0;
            }
        } else {
            if (endPunchStatus == PunchStatus.IN) {
                hrs += Duration.between(start, includedPunches.get(0).getTime()).toMinutes() / 60.0;
                for (int i = 1; i < includedPunches.size() - 1; i += 2) {
                    hrs += Duration.between(includedPunches.get(i).getTime(), includedPunches.get(i+1).getTime()).toMinutes() / 60.0;
                }
                hrs += Duration.between(includedPunches.get(includedPunches.size() - 1).getTime(), end).toMinutes() / 60.0;
            } else {
                hrs += Duration.between(start, includedPunches.get(0).getTime()).toMinutes() / 60.0;
                for (int i = 1; i < includedPunches.size(); i += 2) {
                    hrs += Duration.between(includedPunches.get(i).getTime(), includedPunches.get(i+1).getTime()).toMinutes() / 60.0;
                }
            }
        }
        return new BigDecimal(hrs).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
