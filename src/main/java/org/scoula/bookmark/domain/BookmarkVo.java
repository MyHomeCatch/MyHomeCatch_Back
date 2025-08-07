package org.scoula.bookmark.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkVo {
    private int userId;
    private int danziId;
}
