package org.choongang.member.controllers;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.choongang.school.entities.School;

import java.util.UUID;

@Data
public class RequestJoin {

    private String gid = UUID.randomUUID().toString();

   // @NotBlank @Email
    //private String email;

    @NotBlank
    private String domain;

    private School sNum;

    private String univId;

    @NotBlank
    @Size(min=6)
    private String userId;

    @NotBlank
    @Size(min=8)
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String name;

    @AssertTrue
    private boolean agree;

    public String getEmail() {
        return String.format("%s@%s", univId, domain);
    }


}
