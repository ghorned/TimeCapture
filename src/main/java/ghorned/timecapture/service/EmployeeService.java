package ghorned.timecapture.service;

import ghorned.timecapture.entity.Employee;
import ghorned.timecapture.entity.Punch;
import ghorned.timecapture.exception.EmployeeNotFoundException;
import ghorned.timecapture.repository.EmployeeRepository;
import ghorned.timecapture.response.Report;
import ghorned.timecapture.utility.HoursWorked;
import ghorned.timecapture.utility.PunchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Employee> getAll() {
        return (List<Employee>) employeeRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee getById(int id) {
        return employeeRepository.findById(id).orElseThrow(() ->
                new EmployeeNotFoundException(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee getByUsername(String username) {
        for (Employee employee : employeeRepository.findAll()) {
            if (employee.getUsername().equalsIgnoreCase(username)) {
                return employee;
            }
        }
        throw new EmployeeNotFoundException(username);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee add(Employee addedEmployee) {
        Employee employee = new Employee();
        employee.setUsername(addedEmployee.getUsername());
        employee.setPassword(new BCryptPasswordEncoder().encode(addedEmployee.getPassword()));
        employee.setFirstName(addedEmployee.getFirstName());
        employee.setLastName(addedEmployee.getLastName());
        employee.setWeekHrsMin(addedEmployee.getWeekHrsMin());
        employee.setWeekHrsMax(addedEmployee.getWeekHrsMax());
        employee.setAccessAuthority(addedEmployee.getAccessAuthority());
        employeeRepository.save(employee);
        return employee;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee update(int id, Employee updatedEmployee) {
        Employee employee = getById(id);
        employee.setUsername(updatedEmployee.getUsername());
        employee.setPassword(new BCryptPasswordEncoder().encode(updatedEmployee.getPassword()));
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setWeekHrsMin(updatedEmployee.getWeekHrsMin());
        employee.setWeekHrsMax(updatedEmployee.getWeekHrsMax());
        employee.setAccessAuthority(updatedEmployee.getAccessAuthority());
        employeeRepository.save(employee);
        return employee;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee delete(int id) {
        Employee employee = getById(id);
        employeeRepository.delete(employee);
        return employee;
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public Employee updatePassword(int id, Employee updatedEmployee) {
        Employee employee = getById(id);
        employee.setPassword(new BCryptPasswordEncoder().encode(updatedEmployee.getPassword()));
        employeeRepository.save(employee);
        return employee;
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    @Enumerated(EnumType.STRING)
    public PunchStatus getPunchStatus(int id) {
        Employee employee = getById(id);
        if (employee.getPunches().size() % 2 == 0) {
            return PunchStatus.OUT;
        } else {
            return PunchStatus.IN;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public Punch getLastPunch(int id) {
        Employee employee = getById(id);
        if (employee.getPunches().isEmpty()) {
            return null;
        } else {
            return employee.getPunches().get(employee.getPunches().size() - 1);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public List<Punch> getTodayPunches(int id) {
        Employee employee = getById(id);
        if (employee.getPunches().isEmpty()) {
            return null;
        } else {
            List<Punch> todayPunches = new ArrayList<>();
            for (Punch punch : employee.getPunches()) {
                if (punch.getTime().getYear() == LocalDateTime.now().getYear() &&
                        punch.getTime().getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
                    todayPunches.add(punch);
                }
            }
            return todayPunches;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public List<Punch> getMonthPunches(int id) {
        Employee employee = getById(id);
        if (employee.getPunches().isEmpty()) {
            return null;
        } else {
            List<Punch> monthPunches = new ArrayList<>();
            for (Punch punch : employee.getPunches()) {
                if (punch.getTime().getYear() == LocalDateTime.now().getYear() &&
                        punch.getTime().getMonth() == LocalDateTime.now().getMonth()) {
                    monthPunches.add(punch);
                }
            }
            return monthPunches;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public List<Punch> getYearPunches(int id) {
        Employee employee = getById(id);
        if (employee.getPunches().isEmpty()) {
            return null;
        } else {
            List<Punch> yearPunches = new ArrayList<>();
            for (Punch punch : employee.getPunches()) {
                if (punch.getTime().getYear() == LocalDateTime.now().getYear()) {
                    yearPunches.add(punch);
                }
            }
            return yearPunches;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public List<Punch> getAllPunches(int id) {
        Employee employee = getById(id);
        return employee.getPunches();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public List<Punch> getPastWeekPunches(int id) {
        Employee employee = getById(id);
        if (employee.getPunches().isEmpty()) {
            return null;
        } else {
            List<Punch> weekPunches = new ArrayList<>();
            for (Punch punch : employee.getPunches()) {
                if (punch.getTime().getYear() == LocalDateTime.now().getYear() &&
                        punch.getTime().getDayOfYear() <= LocalDateTime.now().getDayOfYear() &&
                        punch.getTime().getDayOfYear() > LocalDateTime.now().minusWeeks(1).getDayOfYear()) {
                    weekPunches.add(punch);
                }
            }
            return weekPunches;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #id")
    public List<Punch> getPastTwoWeekPunches(int id) {
        Employee employee = getById(id);
        if (employee.getPunches().isEmpty()) {
            return null;
        } else {
            List<Punch> pastTwoWeekPunches = new ArrayList<>();
            for (Punch punch : employee.getPunches()) {
                if (punch.getTime().getYear() == LocalDateTime.now().getYear() &&
                        punch.getTime().getDayOfYear() <= LocalDateTime.now().getDayOfYear() &&
                        punch.getTime().getDayOfYear() > LocalDateTime.now().minusWeeks(2).getDayOfYear()) {
                    pastTwoWeekPunches.add(punch);
                }
            }
            return pastTwoWeekPunches;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Report getPayPeriodReport(int id) {
        Employee employee = getById(id);

        double weekOneTotalHrs = HoursWorked.getWeekHrs(
                employee, LocalDateTime.now().minusWeeks(2), LocalDateTime.now().minusWeeks(1));
        double weekTwoTotalHrs = HoursWorked.getWeekHrs(
                employee, LocalDateTime.now().minusWeeks(1), LocalDateTime.now());

        double weekOneRegHrs = weekOneTotalHrs > 40 ? 40 : weekOneTotalHrs;
        double weekTwoRegHrs = weekTwoTotalHrs > 40 ? 40 : weekTwoTotalHrs;

        double weekOneOverHrs = weekOneTotalHrs - weekOneRegHrs;
        double weekTwoOverHrs = weekTwoTotalHrs - weekTwoRegHrs;

        String flag = null;
        if (weekOneTotalHrs > employee.getWeekHrsMax() ||
                weekOneTotalHrs < employee.getWeekHrsMin() ||
                weekTwoTotalHrs > employee.getWeekHrsMax() ||
                weekTwoTotalHrs < employee.getWeekHrsMin()) {
            flag = "Hours out of bounds";
        }

        return new Report(employee, weekOneRegHrs, weekOneOverHrs, weekTwoRegHrs, weekTwoOverHrs, flag);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Report getPastPayPeriodReport(int id) {
        Employee employee = getById(id);

        double weekOneTotalHrs = HoursWorked.getWeekHrs(
                employee, LocalDateTime.now().minusWeeks(4), LocalDateTime.now().minusWeeks(3));
        double weekTwoTotalHrs = HoursWorked.getWeekHrs(
                employee, LocalDateTime.now().minusWeeks(3), LocalDateTime.now().minusWeeks(2));

        double weekOneRegHrs = weekOneTotalHrs > 40 ? 40 : weekOneTotalHrs;
        double weekTwoRegHrs = weekTwoTotalHrs > 40 ? 40 : weekTwoTotalHrs;

        double weekOneOverHrs = weekOneTotalHrs - weekOneRegHrs;
        double weekTwoOverHrs = weekTwoTotalHrs - weekTwoRegHrs;

        String flag = null;
        if (weekOneTotalHrs > employee.getWeekHrsMax() ||
                weekOneTotalHrs < employee.getWeekHrsMin() ||
                weekTwoTotalHrs > employee.getWeekHrsMax() ||
                weekTwoTotalHrs < employee.getWeekHrsMin()) {
            flag = "Hours out of bounds";
        }

        return new Report(employee, weekOneRegHrs, weekOneOverHrs, weekTwoRegHrs, weekTwoOverHrs, flag);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }
}
