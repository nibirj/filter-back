package org.wisercat.test.assignment.service;

import org.wisercat.test.assignment.protocol.FilterDTO;
import org.wisercat.test.assignment.protocol.FilterWithCriteriaDTO;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface FilterService {
    FilterWithCriteriaDTO createFilter(FilterWithCriteriaDTO filter);
    List<FilterDTO> listAllFilters();
    Optional<FilterWithCriteriaDTO> getFilter(Long filterId);
    Optional<FilterWithCriteriaDTO>  updateFilter(Long filterId, FilterWithCriteriaDTO criteriaDTOList) throws ParseException;
    void deleteFilter(Long filterId);
}
