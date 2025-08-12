package org.scoula.lh.danzi.dto.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanziRequestDTO {
    private Integer UserId;
    private List<String> selfCheckResult;
}
