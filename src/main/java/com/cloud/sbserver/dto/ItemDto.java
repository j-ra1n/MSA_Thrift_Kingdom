package com.cloud.sbserver.dto;

import lombok.Data;

@Data
public class ItemDto
{
    private int content_id;
    private String productName;
    private String price;
    private String url;
    private String nickname;
    private long user_id;
}
