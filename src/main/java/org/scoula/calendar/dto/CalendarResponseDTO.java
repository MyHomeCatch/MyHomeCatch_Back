package org.scoula.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.calendar.domain.CalendarVO;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarResponseDTO {

    private int totalCount;
    private List<CalendarDTO> dataList;
}
