package ch.fhnw.cere.orchestrator.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Operation not allowed. There may be dependencies to the resource.")
public class ConflictException extends RuntimeException {
}