package org.scoula.bookmark.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.bookmark.domain.BookmarkVo;
import org.scoula.bookmark.dto.BookmarkDto;
import org.scoula.bookmark.dto.BookmarkEmailInfo;

import java.time.LocalDate;
import java.util.List;

public interface BookmarkMapper {
    List<BookmarkEmailInfo> findAlertsForToday(@Param("today") LocalDate today);

    public int createBookmark(BookmarkDto bookmarkDto);

    public List<BookmarkVo> readBookmark(BookmarkDto bookmarkDto);

    public int deleteBookmark(BookmarkDto bookmarkDto);

}
