package org.scoula.lh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/lh/notices")
@Api(tags = "LH Notice API")
public class LhNoticeController {

    @Autowired
    private LhNoticeService lhNoticeService;

    @GetMapping("/all")
    @ApiOperation(value = "Get All LH Notices", notes = "Get a list of all LH notices.")
    public ResponseEntity<List<LhNoticeVO>> getAllLhNotices() {
        List<LhNoticeVO> notices = lhNoticeService.getAllLhNoticesNew();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/{panId}")
    @ApiOperation(value = "Get LH Notice by PAN ID", notes = "Get an LH notice by its PAN ID.")
    public ResponseEntity<LhNoticeVO> getLhNoticeByPanId(@PathVariable String panId) {
        LhNoticeVO notice = lhNoticeService.getLhNoticeByPanIdNew(panId);
        if (notice != null) {
            return ResponseEntity.ok(notice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
