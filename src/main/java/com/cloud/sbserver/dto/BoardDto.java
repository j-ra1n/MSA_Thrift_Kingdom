package com.cloud.sbserver.dto;

import lombok.Data;

@Data
public class BoardDto
{
    private int content_id;
    private String title;
    private String content;
    private String nickname;
    private long user_id;
}
