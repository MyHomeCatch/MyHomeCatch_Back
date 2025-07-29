package org.scoula.house.controller;

import lombok.RequiredArgsConstructor;
//import net.coobird.thumbnailator.Thumbnailator;
import org.scoula.house.service.ThumbService;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
public class ThumbnailController {

    private final ThumbService service;


    // 메인 공고 썸네일 반환 -

    // 공고 상세 전체 썸네일
//    @PostMapping("/api/thumbnails")
//    @ResponseBody
//    public ResponseEntity<List<String>> createThumbFromUrl (@RequestBody String panId){
//        List<String> createdThumbnailPaths = new ArrayList<>();
//
//        for (String url : imgUrls) {
//        String imgPath = service.createImgPathFromUrl(url);
//        service.saveImgPath(imgPath);
//        createdThumbnailNames.add(imgPath);
//        service.ThumbnailFromImgPath(imgPath);
//        }
//    }


}
