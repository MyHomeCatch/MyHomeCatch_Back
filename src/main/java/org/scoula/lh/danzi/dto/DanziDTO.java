package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.DanziVO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DanziDTO {
    private Integer danziId;
    private String bzdtNm;
    private String lctAraAdr;
    private String lctAraDtlAdr;
    private String minMaxRsdnDdoAr;
    private Integer sumTotHshCnt;
    private String htnFmlaDeCoNm;
    private Date mvinXpcYm;

    public static DanziDTO from(DanziVO vo) {
        return DanziDTO.builder()
                .danziId(vo.getDanziId())
                .bzdtNm(vo.getBzdtNm())
                .lctAraAdr(vo.getLctAraAdr())
                .lctAraDtlAdr(vo.getLctAraDtlAdr())
                .minMaxRsdnDdoAr(vo.getMinMaxRsdnDdoAr())
                .sumTotHshCnt(vo.getSumTotHshCnt())
                .htnFmlaDeCoNm(vo.getHtnFmlaDeCoNm())
                .mvinXpcYm(DateParser.parseFullDate(vo.getMvinXpcYm()))
                .build();
    }

    public class DateParser {
        private static final DateTimeFormatter FULL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public static Date parseFullDate(String yyyymmdd) {
            if (yyyymmdd == null || yyyymmdd.isEmpty()) return null;
            try {
                LocalDate date = LocalDate.parse(yyyymmdd.substring(0, 10), FULL_DATE_FORMAT); // "2028-07-01"
                return java.sql.Date.valueOf(date);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
