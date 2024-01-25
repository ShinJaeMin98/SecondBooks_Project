package org.choongang.myPage.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.choongang.file.entities.FileInfo;

@Data
public class RequestProfile {
    @NotBlank
    private String name;
    private String password;
    private String confirmPassword;

    private FileInfo profileImage;
}
