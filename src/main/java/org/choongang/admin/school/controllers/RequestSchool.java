package org.choongang.admin.school.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.choongang.commons.constants.Location;

import java.util.UUID;

@Data
public class RequestSchool {
    private String mode = "add";

    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String domain;

    private Location menuLocation = Location.LEFT;

}