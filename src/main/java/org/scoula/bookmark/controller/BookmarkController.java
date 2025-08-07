package org.scoula.bookmark.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.service.AuthService;
import org.scoula.bookmark.dto.BookmarkDto;
import org.scoula.bookmark.service.BookmarkService;
import org.scoula.common.response.CommonResponse;
import org.scoula.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmark")
@Log4j2
@Api(tags = "즐겨찾기 API")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JwtUtil jwtUtil;

    @ApiOperation(value = "즐겨찾기 등록", notes = "즐겨찾기를 등록합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @PostMapping()
    public ResponseEntity<?> createBookmark(@RequestBody BookmarkDto bookmarkDto, HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);

        int result = bookmarkService.createBookmark(bookmarkDto, email);

        if (result == 1) {
            return ResponseEntity.ok(CommonResponse.response("즐겨찾기 등록 완료"));
        } else {
            return ResponseEntity.status(400).body(CommonResponse.response("즐겨찾기 등록 실패"));
        }
    }



    @ApiOperation(value = "즐겨찾기 조회", notes = "유저의 즐겨찾기 항목을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping()
    public ResponseEntity<?> readBookmark(HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok().body(bookmarkService.readBookmark(email));
    }

    @ApiOperation(value = "즐겨찾기 삭제", notes = "즐겨찾기를 해제합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @DeleteMapping()
    public ResponseEntity<?> deleteBookmark(@RequestBody BookmarkDto bookmarkDto, HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);

        int result = bookmarkService.deleteBookmark(bookmarkDto, email);

        if (result == 1) {
            return ResponseEntity.ok(CommonResponse.response("즐겨찾기 삭제 완료"));
        } else {
            return ResponseEntity.status(400).body(CommonResponse.response("즐겨찾기 삭제 실패"));
        }
    }

    @ApiOperation(value = "게시물 즐겨찾기수 조회 ",notes = "게시물 별 조회수 개수를 조회합니다." )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping("/{houseId}")
    public ResponseEntity<?> getBookmarksByHouseId(@PathVariable Integer houseId) {
        return ResponseEntity.ok().body(bookmarkService.getBookmarksByHouseId(houseId));
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
