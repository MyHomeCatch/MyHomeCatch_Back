package org.scoula.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    String articleId;
    String commentId;
    String author;
    String content;
    Date createdAt;
}
