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
public class AmountCriteriaDTO extends CriteriaDTO {

    @NotNull
    @JsonProperty("comparableValue")
    private final Integer comparableValue;

    @Builder
    public AmountCriteriaDTO(Long id, String comparingCondition, Integer comparableValue) {
        super(id, CriteriaType.Amount, comparingCondition);
        this.comparableValue = comparableValue;
    }
}
