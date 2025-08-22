package org.scoula.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Integer danziId;  // 관련 공고 id
    int commentId;  // 댓글 고유 id
    String content;  // 게시판 내용
    String nickname;
    private int userId;  // 유저 id
    private Boolean isDeleted;  // 삭제 여부
}
