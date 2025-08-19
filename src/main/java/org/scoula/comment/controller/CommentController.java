package org.scoula.comment.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.scoula.comment.Service.CommentService;
import org.scoula.comment.dto.CommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/detail/comments")
@Api(tags = "Comment API")
public class CommentController {
    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping("/{danziId}")
    @ApiOperation(value = "Get Comments", notes = "Get a list of comments for a specific danzi.")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer danziId) {
        List<CommentDTO> comments = commentService.getCommentsByDanziId(danziId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 등록
    @PostMapping
    @ApiOperation(value = "Add Comment", notes = "Add a new comment.")
    public ResponseEntity<Integer> addComment(@RequestBody CommentDTO comment) {
        int result = commentService.addComment(comment);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "Delete Comment", notes = "Delete a comment by its ID.")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
