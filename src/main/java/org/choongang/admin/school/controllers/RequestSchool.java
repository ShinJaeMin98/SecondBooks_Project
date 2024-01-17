package org.choongang.admin.school.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.constants.Location;
import org.choongang.school.entities.School;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RequestSchool {
    private String mode = "add";
    private Long num = -1L;
    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String domain;
    private String comment;

    private Location menuLocation = Location.LEFT;

    //private List<String> chk = new ArrayList<>();

    public RequestSchool (School school){
        this.domain = school.getDomain();
        this.gid = school.getGid();;
        this.menuLocation = school.getMenuLocation();
        this.num = school.getNum();;
    }


}