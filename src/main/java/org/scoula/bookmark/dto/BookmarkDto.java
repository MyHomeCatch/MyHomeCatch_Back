package org.scoula.bookmark.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDto {
    private int userId;
    private int danziId;
}
