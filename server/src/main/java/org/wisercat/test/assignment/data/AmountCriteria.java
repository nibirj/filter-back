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
import org.wisercat.test.assignment.protocol.AmountCriteriaDTO;

import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmountCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comparingCondition;
    private Integer comparableValue;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filter_id")
    @JsonBackReference
    private Filter filter;

    public static AmountCriteria fromProtocolObject(AmountCriteriaDTO amountCriteriaDTO, Filter filter) {
        return AmountCriteria.builder()
                .id(amountCriteriaDTO.getId())
                .comparingCondition(amountCriteriaDTO.getComparingCondition())
                .comparableValue(amountCriteriaDTO.getComparableValue())
                .filter(filter)
                .build();
    }

    @Override
    public String toString() {
        return "AmountCriteria{" +
                "id=" + id +
                ", comparingCondition='" + comparingCondition + '\'' +
                ", comparableValue=" + comparableValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountCriteria that = (AmountCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(comparingCondition, that.comparingCondition) && Objects.equals(comparableValue, that.comparableValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comparingCondition, comparableValue);
    }
}
