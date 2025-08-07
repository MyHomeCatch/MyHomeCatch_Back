package org.scoula.bookmark.service;

import org.scoula.bookmark.domain.BookmarkVo;
import org.scoula.bookmark.dto.BookmarkDto;
import org.scoula.bookmark.dto.BookmarkListDto;

import java.util.List;

public interface BookmarkService {

    public int createBookmark(BookmarkDto bookmarkDto, String email);

    public BookmarkListDto readBookmark(String email);

    public int deleteBookmark(BookmarkDto bookmarkDto, String email);

    public int getBookmarksByHouseId(Integer houseId);
}
