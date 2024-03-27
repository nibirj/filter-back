package org.wisercat.test.assignment.data;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wisercat.test.assignment.protocol.TitleCriteriaDTO;

import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TitleCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comparingCondition;
    private String comparableName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filter_id")
    @JsonBackReference
    private Filter filter;

    public static TitleCriteria fromProtocolObject(TitleCriteriaDTO titleCriteriaDTO, Filter filter) {
        return TitleCriteria.builder()
                .id(titleCriteriaDTO.getId())
                .comparingCondition(titleCriteriaDTO.getComparingCondition())
                .comparableName(titleCriteriaDTO.getComparableName())
                .filter(filter)
                .build();
    }

    @Override
    public String toString() {
        return "TitleCriteria{" +
                "id=" + id +
                ", comparingCondition='" + comparingCondition + '\'' +
                ", comparableName='" + comparableName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleCriteria that = (TitleCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(comparingCondition, that.comparingCondition) && Objects.equals(comparableName, that.comparableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comparingCondition, comparableName);
    }
}
