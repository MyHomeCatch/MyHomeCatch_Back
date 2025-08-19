package org.scoula.comment.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.comment.dto.CommentDTO;

import java.util.List;

public interface CommentMapper {
    // 특정 panId(공고ID)에 해당하는 댓글 목록 조회
    List<CommentDTO> getCommentsByDanziId(@Param("danziId") int danziId);

    // 댓글 등록
    int insertComment(CommentDTO comment);

    // 댓글 삭제 (soft delete)
    int deleteComment(@Param("commentId") int commentId);
}
