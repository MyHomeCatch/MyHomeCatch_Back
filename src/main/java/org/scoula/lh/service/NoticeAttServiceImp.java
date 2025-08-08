package org.scoula.lh.service;

import lombok.RequiredArgsConstructor;
import org.scoula.lh.danzi.domain.NoticeAttVO;
import org.scoula.lh.dto.NoticeAttDTO;
import org.scoula.lh.mapper.NoticeAttMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeAttServiceImp implements NoticeAttService {

    private final NoticeAttMapper noticeAttMapper;
    Pattern pattern = Pattern.compile(".*공고문\\(PDF\\).*");


    @Override
    public int create(NoticeAttVO noticeAttVO) {
        String name = noticeAttVO.getSlPanAhflDsCdNm();
        if (pattern.matcher(name).matches()) {
            return noticeAttMapper.create(noticeAttVO);
        }
        return 0;
    }

    @Override
    public int createAll(List<NoticeAttVO> noticeAttVOList) {
        List<NoticeAttVO> filteredList = noticeAttVOList.stream()
                .filter(item -> {
                    String name = item.getSlPanAhflDsCdNm();
                    return name != null && pattern.matcher(name).matches();
                })
                .collect(Collectors.toList());
        return noticeAttMapper.createAll(filteredList);
    }

    @Override
    public List<NoticeAttDTO> getNoticeAttByPanId(String panId) {
        return null;
//        return noticeAttMapper.getNoticeAttByPanId(panId).stream().map(NoticeAttDTO::of).toList();
    }
}
