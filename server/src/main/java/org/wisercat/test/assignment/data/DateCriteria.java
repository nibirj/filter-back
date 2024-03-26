package org.wisercat.test.assignment.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wisercat.test.assignment.protocol.DateCriteriaDTO;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateCriteria {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comparingCondition;
    private Date comparableDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filter_id")
    @JsonBackReference
    private Filter filter;

    public static DateCriteria fromProtocolObject(DateCriteriaDTO dateCriteriaDTO, Filter filter) throws ParseException {
        return DateCriteria.builder()
                .id(dateCriteriaDTO.getId())
                .comparingCondition(dateCriteriaDTO.getComparingCondition())
                .comparableDate(dateFormat.parse(dateCriteriaDTO.getComparableDate()))
                .filter(filter)
                .build();
    }

    @Override
    public String toString() {
        return "DateCriteria{" +
                "id=" + id +
                ", comparingCondition='" + comparingCondition + '\'' +
                ", comparableDate=" + comparableDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateCriteria that = (DateCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(comparingCondition, that.comparingCondition) && Objects.equals(comparableDate, that.comparableDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comparingCondition, comparableDate);
    }
}
