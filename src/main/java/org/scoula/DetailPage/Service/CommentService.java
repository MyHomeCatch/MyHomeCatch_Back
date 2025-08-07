package org.scoula.DetailPage.Service;


import lombok.RequiredArgsConstructor;
import org.scoula.DetailPage.mapper.CommentMapper;
import org.scoula.DetailPage.dto.CommentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public List<CommentDTO> getCommentsByDanziId(Integer danziId) {
        return commentMapper.getCommentsByDanziId(danziId);
    }

    public int addComment(CommentDTO comment) {
        return commentMapper.insertComment(comment);
    }

    public void deleteComment(int commentId) {
        commentMapper.deleteComment(commentId);
    }
}
