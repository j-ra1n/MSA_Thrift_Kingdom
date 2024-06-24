package com.cloud.cm_server.dto;

import lombok.Data;

@Data
public class CommentRequestDto {

    private Long boardId;
    private String content;
    private String nickname;
    private long userId;
}