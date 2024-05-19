package com.cloud.bbserver.dto;

import lombok.Data;

@Data
public class BoardDto
{   // jenkinsTest
    private int content_id;
    private String title;
    private String content;
    private String nickname;
    private long user_id;
}