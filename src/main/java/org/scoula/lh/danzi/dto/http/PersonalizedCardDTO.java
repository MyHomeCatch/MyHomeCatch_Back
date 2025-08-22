package org.scoula.lh.danzi.dto.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.dto.EligibilityResultDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedCardDTO {
    private Integer userId;
    private Integer danziId;
    private EligibilityResultDTO eligibilityResultDTO;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
