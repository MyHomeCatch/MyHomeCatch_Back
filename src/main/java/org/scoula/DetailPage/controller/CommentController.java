package org.scoula.DetailPage.controller;


import lombok.RequiredArgsConstructor;
import org.scoula.DetailPage.Service.CommentService;
import org.scoula.DetailPage.dto.CommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/detail/comments")
public class CommentController {
    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping("/{danziId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer danziId) {
        List<CommentDTO> comments = commentService.getCommentsByDanziId(danziId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity<Integer> addComment(@RequestBody CommentDTO comment) {
        int result = commentService.addComment(comment);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
