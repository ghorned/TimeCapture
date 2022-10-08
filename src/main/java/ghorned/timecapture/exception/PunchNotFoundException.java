package ghorned.timecapture.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PunchNotFoundException extends RuntimeException {

    public PunchNotFoundException(int id) {
        super(MessageFormat.format("Could not find punch with id: {0}", id));
    }
}