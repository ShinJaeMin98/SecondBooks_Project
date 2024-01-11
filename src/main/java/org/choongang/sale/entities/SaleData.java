package org.choongang.sale.entities;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SaleData {

    private String userId;  //작성자명
    private String title;   //글제목
    private String details; //상세설명
    private String saleType;    //거래 방식(판매or나눔)
    private int price;  //가격
    private String postingType;    //판매or구매
    private String gid;
    private String location;
    private String imageOnly;
    private MultipartFile file;


}
