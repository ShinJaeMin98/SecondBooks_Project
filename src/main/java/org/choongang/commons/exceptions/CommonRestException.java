package org.choongang.commons.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.choongang.commons.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommonRestException extends CommonException {
    public CommonRestException(Errors errors, HttpStatus status) {
        super(toJSON(errors), status);
    }

    private static String toJSON(Errors errors) {
        List<String[]> items = errors.getFieldErrors()
                .stream()
                .map(e -> new String[] { e.getField(), Utils.getMessage(e.getCodes()[0]) })
                .toList();

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        try {
            return om.writeValueAsString(items);
        } catch (JsonProcessingException e) {}

        return null;
    }

    private static String getErrorMessage(String[] codes) {
        String message = Arrays.stream(codes)
                .map(c -> Utils.getMessage(c)).filter(s -> !s.isBlank())
                .collect(Collectors.joining("||"));

        return message;
    }
}
