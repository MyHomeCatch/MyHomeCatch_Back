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

    public List<CommentDTO> getCommentsByPanId(String panId) {
        return commentMapper.findCommentsByPanId(panId);
    }

    public void addComment(CommentDTO comment) {
        commentMapper.insertComment(comment);
    }

    public void deleteComment(String commentId) {
        commentMapper.deleteComment(commentId);
    }
}
