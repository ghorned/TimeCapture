package ghorned.timecapture.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmployeePunchAlreadyExistsException extends RuntimeException {

    public EmployeePunchAlreadyExistsException(LocalDateTime time, int id) {
        super(MessageFormat.format("Punch for time: {0} already exists for employee with id: {1}", time, id));
    }
}