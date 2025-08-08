package org.scoula.lh.controller;

import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.service.LhNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lh/notices")
public class LhNoticeController {

    @Autowired
    private LhNoticeService lhNoticeService;

    @GetMapping("/all")
    public ResponseEntity<List<LhNoticeVO>> getAllLhNotices() {
        List<LhNoticeVO> notices = lhNoticeService.getAllLhNoticesNew();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/{panId}")
    public ResponseEntity<LhNoticeVO> getLhNoticeByPanId(@PathVariable String panId) {
        LhNoticeVO notice = lhNoticeService.getLhNoticeByPanIdNew(panId);
        if (notice != null) {
            return ResponseEntity.ok(notice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
