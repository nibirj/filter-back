package org.wisercat.test.assignment.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AmountCriteriaDTO.class, name = "Amount"),
        @JsonSubTypes.Type(value = TitleCriteriaDTO.class, name = "Title"),
        @JsonSubTypes.Type(value = DateCriteriaDTO.class, name = "Date")
})
public abstract class CriteriaDTO {
    @JsonProperty("id")
    private final Long id;
    @NotNull
    @JsonProperty("type")
    private final CriteriaType type;
    @NotNull
    @JsonProperty("comparingCondition")
    private final String comparingCondition;
}
