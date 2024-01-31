package org.choongang.chatting.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestChatHistory {

    @NotBlank
    private String roomId;

    @NotBlank
    private String nickName;

    @NotBlank
    private String message;
}
