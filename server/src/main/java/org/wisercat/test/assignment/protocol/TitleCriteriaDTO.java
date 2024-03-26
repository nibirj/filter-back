package org.wisercat.test.assignment.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class TitleCriteriaDTO extends CriteriaDTO {
    @NotNull
    @JsonProperty("comparableName")
    private final String comparableName;

    @Builder
    public TitleCriteriaDTO(Long id, String comparingCondition, String comparableName) {
        super(id, CriteriaType.Title, comparingCondition);
        this.comparableName = comparableName;
    }
}
