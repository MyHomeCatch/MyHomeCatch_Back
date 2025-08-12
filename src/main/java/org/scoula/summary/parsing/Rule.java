package org.scoula.summary.parsing;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class Rule {
    private final List<String> section;
    private final List<String> cells;
    private final List<String> cues;

    // is_homeless 전용(선택)
    private final List<String> cuesRequired;
    private final List<String> cuesRelaxed;
    private final List<String> cuesNotRequired;

    // target_groups 전용(선택)
    private final List<String> tokens;

    public Rule(List<String> section,
                List<String> cells,
                List<String> cues,
                List<String> cuesRequired,
                List<String> cuesRelaxed,
                List<String> cuesNotRequired,
                List<String> tokens) {

        this.section       = section == null ? null : Collections.unmodifiableList(section);
        this.cells         = cells == null ? null : Collections.unmodifiableList(cells);
        this.cues          = cues == null ? null : Collections.unmodifiableList(cues);
        this.cuesRequired  = cuesRequired == null ? null : Collections.unmodifiableList(cuesRequired);
        this.cuesRelaxed   = cuesRelaxed == null ? null : Collections.unmodifiableList(cuesRelaxed);
        this.cuesNotRequired = cuesNotRequired == null ? null : Collections.unmodifiableList(cuesNotRequired);
        this.tokens        = tokens == null ? null : Collections.unmodifiableList(tokens);
    }
}



