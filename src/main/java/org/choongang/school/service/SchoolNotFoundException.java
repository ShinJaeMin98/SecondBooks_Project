package org.choongang.school.service;

import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class SchoolNotFoundException extends AlertBackException {
    public SchoolNotFoundException(){
        super(Utils.getMessage("NotFound.school", "errors"), HttpStatus.NOT_FOUND);
    }
}
