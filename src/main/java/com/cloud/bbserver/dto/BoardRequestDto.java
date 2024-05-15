package com.cloud.bbserver.dto;

import lombok.Data;

@Data
public class BoardRequestDto {
    private String title;
    private String content;
    private String nickname;
    private String company;
    private long user_id;
}