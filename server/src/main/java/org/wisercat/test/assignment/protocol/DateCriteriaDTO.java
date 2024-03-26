package org.wisercat.test.assignment.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DateCriteriaDTO extends CriteriaDTO {
    @NotNull
    @JsonProperty("comparableDate")
    private final String comparableDate;
    @Builder
    public DateCriteriaDTO(Long id, String comparingCondition, String comparableDate) {
        super(id, CriteriaType.Date, comparingCondition);
        this.comparableDate = comparableDate;
    }
}
