package org.choongang.commons;

import lombok.Data;

@Data
public class RequestPaging {
    protected int page  = 1;
    protected int limit = 20;
}