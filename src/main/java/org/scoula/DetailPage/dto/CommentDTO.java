package org.scoula.DetailPage.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private String panId;  // 관련 공고 id
    String commentId;  // 댓글 고유 id
    String content;  // 게시판 내용

    private int userId;  // 유저 id
    private String nickname;  // 유저 닉네임

    private Boolean isDeleted;  // 삭제 여부
}
