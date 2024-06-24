package com.cloud.cm_server.dto;

import lombok.Data;

@Data
public class CommentDto
{
    private Long boardId;
    private String content;
    private String nickname;
    private long userId;
}
