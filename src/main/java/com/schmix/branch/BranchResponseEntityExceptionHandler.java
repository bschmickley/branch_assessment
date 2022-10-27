package com.schmix.branch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.schmix.branch.exceptions.BadUpstreamResponseException;
import com.schmix.branch.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class BranchResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Map<Class, HttpStatus> EXCEPTIONS_MAP = Map.ofEntries(
            Map.entry(IllegalArgumentException.class, HttpStatus.BAD_REQUEST),
            Map.entry(ResourceNotFoundException.class, HttpStatus.NOT_FOUND),
            Map.entry(BadUpstreamResponseException.class, HttpStatus.BAD_GATEWAY)
    );

    private ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleInternalException(Exception e, WebRequest request) throws Exception {
        HttpStatus status = EXCEPTIONS_MAP.get(e.getClass());

        if (null != status) {
            ObjectNode message = asJson(e.getMessage(), status.value());
            return new ResponseEntity<>(message, new HttpHeaders(), status);
        }

        return super.handleException(e, request);
    }

    private ObjectNode asJson(String message, int statusCode) {
        ObjectNode node = mapper.createObjectNode();
        node.put("message", message);
        node.put("status", statusCode);
        return node;
    }
}
