package org.scoula.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.auth.mapper.AuthMapper;
import org.scoula.bookmark.domain.BookmarkVo;
import org.scoula.bookmark.dto.BookmarkDto;
import org.scoula.bookmark.dto.BookmarkListDto;
import org.scoula.bookmark.mapper.BookmarkMapper;
import org.scoula.member.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BookmarkServiceImpl implements BookmarkService {

    @Autowired
    private BookmarkMapper bookmarkMapper;

    @Autowired
    private AuthMapper authMapper;


    @Override
    public int createBookmark(BookmarkDto bookmarkDto, String email) {
        bookmarkDto.setUserId(getUserIdByEmail(email));
        return bookmarkMapper.createBookmark(bookmarkDto);
    }

    @Override
    public BookmarkListDto readBookmark(String email) {
        BookmarkDto bookmarkDto = BookmarkDto.builder()
                .userId(getUserIdByEmail(email))
                .build();

        List<BookmarkVo> bookmarks = bookmarkMapper.readBookmark(bookmarkDto);

        List<BookmarkDto> bookmarkDtoList = bookmarks.stream()
                .map(vo -> new BookmarkDto(vo.getUserId(), vo.getDanziId()))
                .collect(Collectors.toList());

        return BookmarkListDto.builder()
                .bookmarks(bookmarkDtoList)
                .build();
    }

    @Override
    public int deleteBookmark(BookmarkDto bookmarkDto, String email) {
        bookmarkDto.setUserId(getUserIdByEmail(email));
        return bookmarkMapper.deleteBookmark(bookmarkDto);
    }

    // 이메일로 유저 아이디 가져오기
    public int getUserIdByEmail(String email){
        return authMapper.findByEmail(email).getUserId();
    }

    @Override
    public int getBookmarksByHouseId(Integer houseId) {
        return bookmarkMapper.countByHouseId(houseId);
    }
}
