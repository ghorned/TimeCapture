package ghorned.timecapture.controller;

import ghorned.timecapture.entity.Employee;
import ghorned.timecapture.entity.Punch;
import ghorned.timecapture.response.Report;
import ghorned.timecapture.service.EmployeeService;
import ghorned.timecapture.utility.PunchStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Gets all employees. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> employees = employeeService.getAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Gets employee by id. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
            content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
            content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<Employee> getById(@PathVariable("id") int id) {
        Employee employee = employeeService.getById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Operation(summary = "Gets employee by username. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<Employee> getByUsername(@PathVariable("username") String username) {
        Employee employee = employeeService.getByUsername(username);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Operation(summary = "Adds new employee. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee added",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee could not be added",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<Employee> add(@Valid @RequestBody Employee addedEmployee) {
        Employee employee = employeeService.add(addedEmployee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Operation(summary = "Updates existing employee by id. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee could not be updated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> update(@PathVariable("id") int id, @Valid @RequestBody Employee updatedEmployee) {
        Employee employee = employeeService.update(id, updatedEmployee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Operation(summary = "Deletes existing employee by id. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee could not be deleted",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Employee> delete(@PathVariable("id") int id) {
        Employee employee = employeeService.delete(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Operation(summary = "Updates existing employee's password by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Password could not be updated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @PutMapping("/update/{id}/password")
    public ResponseEntity<Employee> updatePassword(@PathVariable("id") int id, @Valid @RequestBody Employee updatedEmployee) {
        Employee employee = employeeService.updatePassword(id, updatedEmployee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Operation(summary = "Gets current punch status for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/punchStatus")
    public ResponseEntity<PunchStatus> getPunchStatus(@PathVariable("id") int id) {
        PunchStatus punchStatus = employeeService.getPunchStatus(id);
        return new ResponseEntity<>(punchStatus, HttpStatus.OK);
    }

    @Operation(summary = "Gets most recent punch for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/lastPunch")
    public ResponseEntity<Punch> getLastPunch(@PathVariable("id") int id) {
        Punch punch = employeeService.getLastPunch(id);
        return new ResponseEntity<>(punch, HttpStatus.OK);
    }

    @Operation(summary = "Gets today's punches for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/todayPunches")
    public ResponseEntity<List<Punch>> getTodayPunches(@PathVariable("id") int id) {
        List<Punch> punches = employeeService.getTodayPunches(id);
        return new ResponseEntity<>(punches, HttpStatus.OK);
    }

    @Operation(summary = "Gets current month's punches for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/monthPunches")
    public ResponseEntity<List<Punch>> getMonthPunches(@PathVariable("id") int id) {
        List<Punch> punches = employeeService.getMonthPunches(id);
        return new ResponseEntity<>(punches, HttpStatus.OK);
    }

    @Operation(summary = "Gets current year's punches for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/yearPunches")
    public ResponseEntity<List<Punch>> getYearPunches(@PathVariable("id") int id) {
        List<Punch> punches = employeeService.getYearPunches(id);
        return new ResponseEntity<>(punches, HttpStatus.OK);
    }

    @Operation(summary = "Gets all punches for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/allPunches")
    public ResponseEntity<List<Punch>> getAllPunches(@PathVariable("id") int id) {
        List<Punch> punches = employeeService.getAllPunches(id);
        return new ResponseEntity<>(punches, HttpStatus.OK);
    }

    @Operation(summary = "Gets all punches from last week for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/pastWeekPunches")
    public ResponseEntity<List<Punch>> getPastWeekPunches(@PathVariable("id") int id) {
        List<Punch> punches = employeeService.getPastWeekPunches(id);
        return new ResponseEntity<>(punches, HttpStatus.OK);
    }

    @Operation(summary = "Gets all punches from last two weeks for employee by id. Requires ADMIN authority or principal user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("/get/{id}/pastTwoWeekPunches")
    public ResponseEntity<List<Punch>> getPastTwoWeekPunches(@PathVariable("id") int id) {
        List<Punch> punches = employeeService.getPastTwoWeekPunches(id);
        return new ResponseEntity<>(punches, HttpStatus.OK);
    }

    @Operation(summary = "Gets hours worked for past two weeks for employee by id. " +
            "Also returns a flag if employee worked more or less hours than expected. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Report.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("get/{id}/payPeriodReport")
    public ResponseEntity<Report> getPayPeriodReport(@PathVariable("id") int id) {
        Report report = employeeService.getPayPeriodReport(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @Operation(summary = "Gets hours worked for past 3-4 weeks for employee by id. " +
            "Also returns a flag if employee worked more or less hours than expected. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = Report.class))}),
            @ApiResponse(responseCode = "400", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content)
    })
    @GetMapping("get/{id}/pastPayPeriodReport")
    public ResponseEntity<Report> getPastPayPeriodReport(@PathVariable("id") int id) {
        Report report = employeeService.getPastPayPeriodReport(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
