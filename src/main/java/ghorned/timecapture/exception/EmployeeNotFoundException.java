package ghorned.timecapture.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(int id) {
        super(MessageFormat.format("Could not find employee with id: {0}", id));
    }

    public EmployeeNotFoundException(String username) {
        super(MessageFormat.format("Could not find employee with username: {0}", username));
    }
}