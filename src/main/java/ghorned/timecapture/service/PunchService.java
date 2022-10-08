package ghorned.timecapture.service;

import ghorned.timecapture.entity.Employee;
import ghorned.timecapture.entity.Punch;
import ghorned.timecapture.exception.EmployeeNotFoundException;
import ghorned.timecapture.exception.EmployeePunchAlreadyExistsException;
import ghorned.timecapture.exception.PunchNotFoundException;
import ghorned.timecapture.repository.EmployeeRepository;
import ghorned.timecapture.repository.PunchRepository;
import ghorned.timecapture.utility.AccessAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@PreAuthorize("hasAuthority('ADMIN')")
public class PunchService {

    @Autowired
    private PunchRepository punchRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Punch> getAll() {
        return (List<Punch>) punchRepository.findAll();
    }

    public Punch getById(int id) {
        return punchRepository.findById(id).orElseThrow(() ->
                new PunchNotFoundException(id));
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.principal.id == #addedPunch.employee.id")
    public Punch add(Punch addedPunch) {

        // Assert that punch does not already exist
        for (Punch punch : punchRepository.findAll()) {
            if (punch.getEmployee().getId() == addedPunch.getEmployee().getId() &&
                    punch.getTime().isEqual(addedPunch.getTime())) {
                throw new EmployeePunchAlreadyExistsException(punch.getTime(), punch.getEmployee().getId());
            }
        }

        // Assert that associated employee does exist
        for (Employee employee : employeeRepository.findAll()) {
            if (employee.getId() == addedPunch.getEmployee().getId()) {
                Punch createdPunch = new Punch();
                createdPunch.setEmployee(addedPunch.getEmployee());

                // Check authority of caller
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth.getAuthorities().contains(AccessAuthority.ADMIN)) {
                    createdPunch.setTime(addedPunch.getTime()); // Admin can add punches for any time
                } else {
                    createdPunch.setTime(LocalDateTime.now()); // Users can only add punches for current time
                }
                punchRepository.save(createdPunch);
                return createdPunch;
            }
        }
        throw new EmployeeNotFoundException(addedPunch.getEmployee().getId());
    }

    public Punch update(int id, Punch updatedPunch) {

        // Assert that punch already exists
        Punch existingPunch = getById(id);

        // Assert that updated punch does not already exist
        for (Punch punch : punchRepository.findAll()) {
            if (punch.getEmployee().getId() == updatedPunch.getEmployee().getId() &&
                    punch.getTime().isEqual(updatedPunch.getTime())) {
                throw new EmployeePunchAlreadyExistsException(punch.getTime(), punch.getEmployee().getId());
            }
        }

        // Assert that associated employee does exist
        for (Employee employee : employeeRepository.findAll()) {
            if (employee.getId() == updatedPunch.getEmployee().getId()) {
                existingPunch.setEmployee(updatedPunch.getEmployee());
                existingPunch.setTime(updatedPunch.getTime());
                punchRepository.save(existingPunch);
                return existingPunch;
            }
        }
        throw new EmployeeNotFoundException(updatedPunch.getEmployee().getId());
    }

    public Punch delete(int id) {
        Punch punch = getById(id);
        punchRepository.delete(punch);
        return punch;
    }
}
