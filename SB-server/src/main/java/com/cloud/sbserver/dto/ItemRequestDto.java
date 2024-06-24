package com.cloud.sbserver.dto;


import lombok.Data;

@Data
public class ItemRequestDto {
    private String productName;
    private String price;
    private String url;
    private String nickname;
    private long user_id;
}
