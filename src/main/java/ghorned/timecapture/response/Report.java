package ghorned.timecapture.response;

import ghorned.timecapture.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private Employee employee;
    private double weekOneRegHrs;
    private double weekOneOverHrs;
    private double weekTwoRegHrs;
    private double weekTwoOverHrs;
    private String flag;
}
