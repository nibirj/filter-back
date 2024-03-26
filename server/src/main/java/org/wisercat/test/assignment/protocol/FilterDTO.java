package org.wisercat.test.assignment.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.wisercat.test.assignment.data.Filter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FilterDTO {
    @JsonProperty("id")
    private final Long id;
    @NotNull
    @JsonProperty("name")
    private final String name;
    public static FilterDTO toProtocolObject(Filter filter) {
        return FilterDTO.builder()
                .id(filter.getId())
                .name(filter.getName())
                .build();
    }
}
