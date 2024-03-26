package org.wisercat.test.assignment.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wisercat.test.assignment.exceptions.WrongDateFormatException;
import org.wisercat.test.assignment.protocol.AmountCriteriaDTO;
import org.wisercat.test.assignment.protocol.CriteriaDTO;
import org.wisercat.test.assignment.protocol.DateCriteriaDTO;
import org.wisercat.test.assignment.protocol.FilterWithCriteriaDTO;
import org.wisercat.test.assignment.protocol.TitleCriteriaDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Filter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "filter", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<AmountCriteria> amountCriteriaMapping = new ArrayList<>();
    @OneToMany(mappedBy = "filter", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<TitleCriteria> titleCriteriaMapping = new ArrayList<>();
    @OneToMany(mappedBy = "filter", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<DateCriteria> dateCriteriaMapping = new ArrayList<>();

    public static Filter fromDTOObject(FilterWithCriteriaDTO filterWithCriteriaDTO) throws WrongDateFormatException, ParseException {
        Filter filter = Filter.builder()
                .id(filterWithCriteriaDTO.getId())
                .name(filterWithCriteriaDTO.getName()).build();
        List<AmountCriteria> amountCriteria = new ArrayList<>();
        List<TitleCriteria> titleCriteria = new ArrayList<>();
        List<DateCriteria> dateCriteria = new ArrayList<>();

        for (CriteriaDTO criteriaDTO: filterWithCriteriaDTO.getCriteriaDTOList()) {
            if (criteriaDTO instanceof AmountCriteriaDTO) {
                amountCriteria.add(AmountCriteria.fromProtocolObject((AmountCriteriaDTO) criteriaDTO, filter));
            } else if (criteriaDTO instanceof TitleCriteriaDTO) {
                titleCriteria.add(TitleCriteria.fromProtocolObject((TitleCriteriaDTO) criteriaDTO, filter));
            } else {
                dateCriteria.add(DateCriteria.fromProtocolObject((DateCriteriaDTO) criteriaDTO, filter));
            }
        }
        filter.setAmountCriteriaMapping(amountCriteria);
        filter.setTitleCriteriaMapping(titleCriteria);
        filter.setDateCriteriaMapping(dateCriteria);
        return filter;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amountCriteriaMapping=" + amountCriteriaMapping +
                ", titleCriteriaMapping=" + titleCriteriaMapping +
                ", dateCriteriaMapping=" + dateCriteriaMapping +
                '}';
    }
}
