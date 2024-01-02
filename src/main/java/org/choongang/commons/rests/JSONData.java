package org.choongang.commons.rests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JSONData<T> {
    private HttpStatus status = HttpStatus.OK;
    private boolean success = true;
    private T data;
    private String message;
}
