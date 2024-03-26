package org.wisercat.test.assignment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wisercat.test.assignment.data.AmountCriteria;
import org.wisercat.test.assignment.data.DateCriteria;
import org.wisercat.test.assignment.data.Filter;
import org.wisercat.test.assignment.data.TitleCriteria;
import org.wisercat.test.assignment.database.repository.FilterRepository;
import org.wisercat.test.assignment.exceptions.WrongDateFormatException;
import org.wisercat.test.assignment.protocol.FilterDTO;
import org.wisercat.test.assignment.protocol.FilterWithCriteriaDTO;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilterServiceImpl implements FilterService {

    @Autowired
    private FilterRepository filterRepository;

    @Override
    public FilterWithCriteriaDTO createFilter(FilterWithCriteriaDTO filter) {
        try {
            return FilterWithCriteriaDTO.toDTOObject(this.filterRepository.save(Filter.fromDTOObject(filter)));
        } catch (ParseException e) {
            throw new WrongDateFormatException();
        }
    }

    @Override
    public List<FilterDTO> listAllFilters() {
        return this.filterRepository.findAll().stream().map(FilterDTO::toProtocolObject).toList();
    }

    @Override
    public Optional<FilterWithCriteriaDTO> getFilter(Long filterId) {
        Optional<Filter> updatedFilter = this.filterRepository.findById(filterId);
        return updatedFilter.map(FilterWithCriteriaDTO::toDTOObject);
    }

    @Override
    @Transactional
    public Optional<FilterWithCriteriaDTO> updateFilter(Long filterId, FilterWithCriteriaDTO updatedFilter) throws ParseException {
        Filter parentEntity = this.filterRepository.findById(filterId).orElseThrow(EntityNotFoundException::new);
        Filter updatedChildren = Filter.fromDTOObject(updatedFilter);
        parentEntity.setName(updatedChildren.getName());
        amountCriteriaChange(parentEntity, updatedChildren);
        titleCriteriaChange(parentEntity, updatedChildren);
        dateCriteriaChange(parentEntity, updatedChildren);
        filterRepository.save(parentEntity);
        return Optional.of(FilterWithCriteriaDTO.toDTOObject(this.filterRepository.save(parentEntity)));
    }

    private void amountCriteriaChange(Filter parentEntity, Filter updatedChildren) {
        List<AmountCriteria> existingChildren = parentEntity.getAmountCriteriaMapping();

        for (AmountCriteria updatedChild : updatedChildren.getAmountCriteriaMapping()) {
            if (updatedChild.getId() != null) {
                AmountCriteria existingChild = findExistingAmountChild(parentEntity.getAmountCriteriaMapping(), updatedChild.getId());
                if (existingChild != null) {
                    existingChild.setComparingCondition(updatedChild.getComparingCondition());
                    existingChild.setComparableValue(updatedChild.getComparableValue());
                }
            }
        }

        List<Long> updatedChildIds = updatedChildren.getAmountCriteriaMapping().stream().map(AmountCriteria::getId).toList();
        existingChildren.removeIf(child -> !updatedChildIds.contains(child.getId()));

        for (AmountCriteria updatedChild : updatedChildren.getAmountCriteriaMapping()) {
            if (updatedChild.getId() == null) {
                updatedChild.setFilter(parentEntity);
                existingChildren.add(updatedChild);
            }
        }
    }

    private AmountCriteria findExistingAmountChild(List<AmountCriteria> existingChildren, Long childId) {
        for (AmountCriteria childEntity : existingChildren) {
            if (childEntity.getId().equals(childId)) {
                return childEntity;
            }
        }
        return null;
    }

    private void titleCriteriaChange(Filter parentEntity, Filter updatedChildren) {
        List<TitleCriteria> existingChildren = parentEntity.getTitleCriteriaMapping();

        for (TitleCriteria updatedChild : updatedChildren.getTitleCriteriaMapping()) {
            if (updatedChild.getId() != null) {
                TitleCriteria existingChild = findExistingTitleChild(parentEntity.getTitleCriteriaMapping(), updatedChild.getId());
                if (existingChild != null) {
                    existingChild.setComparingCondition(updatedChild.getComparingCondition());
                    existingChild.setComparableName(updatedChild.getComparableName());
                }
            }
        }

        List<Long> updatedChildIds = updatedChildren.getTitleCriteriaMapping().stream().map(TitleCriteria::getId).toList();
        existingChildren.removeIf(child -> !updatedChildIds.contains(child.getId()));

        for (TitleCriteria updatedChild : updatedChildren.getTitleCriteriaMapping()) {
            if (updatedChild.getId() == null) {
                updatedChild.setFilter(parentEntity);
                existingChildren.add(updatedChild);
            }
        }
    }

    private TitleCriteria findExistingTitleChild(List<TitleCriteria> existingChildren, Long childId) {
        for (TitleCriteria childEntity : existingChildren) {
            if (childEntity.getId().equals(childId)) {
                return childEntity;
            }
        }
        return null;
    }

    private void dateCriteriaChange(Filter parentEntity, Filter updatedChildren)  {
        List<DateCriteria> existingChildren = parentEntity.getDateCriteriaMapping();

        for (DateCriteria updatedChild : updatedChildren.getDateCriteriaMapping()) {
            if (updatedChild.getId() != null) {
                DateCriteria existingChild = findExistingDateChild(parentEntity.getDateCriteriaMapping(), updatedChild.getId());
                if (existingChild != null) {
                    existingChild.setComparingCondition(updatedChild.getComparingCondition());
                    existingChild.setComparableDate(updatedChild.getComparableDate());
                }
            }
        }

        List<Long> updatedChildIds = updatedChildren.getDateCriteriaMapping().stream().map(DateCriteria::getId).toList();
        existingChildren.removeIf(child -> !updatedChildIds.contains(child.getId()));

        for (DateCriteria updatedChild : updatedChildren.getDateCriteriaMapping()) {
            if (updatedChild.getId() == null) {
                updatedChild.setFilter(parentEntity);
                existingChildren.add(updatedChild);
            }
        }
    }

    private DateCriteria findExistingDateChild(List<DateCriteria> existingChildren, Long childId) {
        for (DateCriteria childEntity : existingChildren) {
            if (childEntity.getId().equals(childId)) {
                return childEntity;
            }
        }
        return null;
    }

    @Override
    public void deleteFilter(Long filterId) {
        this.filterRepository.deleteById(filterId);
    }
}
