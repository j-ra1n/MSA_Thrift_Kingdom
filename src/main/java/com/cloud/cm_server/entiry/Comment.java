package com.cloud.cm_server.entiry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long boardId;
    private String content;
    private String nickname;
    private LocalDateTime createdTime;
    private long userId;

    // 빌더 패턴 사용 시 생성 시간 자동 설정을 위한 로직 포함
    public static class CommentBuilder {
        private LocalDateTime createdTime = LocalDateTime.now(); // 빌더가 호출될 때 초기값 설정

        public CommentBuilder createdTime(LocalDateTime createdTime) {
            this.createdTime = (createdTime == null) ? LocalDateTime.now() : createdTime;
            return this;
        }
    }
}
