package org.scoula.bookmark.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkListDto {
    private List<BookmarkDto> bookmarks;
}
