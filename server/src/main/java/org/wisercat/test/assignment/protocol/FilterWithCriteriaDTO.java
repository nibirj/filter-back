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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Setter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class FilterWithCriteriaDTO {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @JsonProperty("id")
    private final Long id;
    @NotNull
    @JsonProperty("name")
    private final String name;
    @NotNull
    @JsonProperty("criterias")
    private final List<CriteriaDTO> criteriaDTOList;

    public static FilterWithCriteriaDTO toDTOObject(Filter filter) {
        List<CriteriaDTO> allCriterias = new ArrayList<>();
        filter.getAmountCriteriaMapping().forEach(entry ->
                allCriterias.add(AmountCriteriaDTO.builder()
                        .id(entry.getId())
                        .comparableValue(entry.getComparableValue())
                        .comparingCondition(entry.getComparingCondition())
                        .build()));
        filter.getTitleCriteriaMapping().forEach(entry ->
                allCriterias.add(TitleCriteriaDTO.builder()
                        .id(entry.getId())
                        .comparableName(entry.getComparableName())
                        .comparingCondition(entry.getComparingCondition())
                        .build()));
        filter.getDateCriteriaMapping().forEach(entry ->
                allCriterias.add(DateCriteriaDTO.builder()
                        .id(entry.getId())
                        .comparableDate(dateFormat.format(entry.getComparableDate()))
                        .comparingCondition(entry.getComparingCondition())
                        .build()));
        return FilterWithCriteriaDTO.builder()
                .id(filter.getId())
                .name(filter.getName())
                .criteriaDTOList(allCriterias)
                .build();
    }
}
