package org.choongang.admin.school.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.choongang.commons.constants.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class RequestSchool {
    private String mode = "add";
    private Long num = -1L;
    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String domain;
    private String comment;

    private Location menuLocation = Location.LEFT;

    //private List<String> chk = new ArrayList<>();

}