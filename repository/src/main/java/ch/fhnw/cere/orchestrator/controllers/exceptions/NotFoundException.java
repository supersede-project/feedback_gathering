package ch.fhnw.cere.orchestrator.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Resource not found")
public class NotFoundException extends RuntimeException {
}