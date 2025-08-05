package org.scoula.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkEmailInfo {

    private String email;
    private String title;
    private String type;
}
