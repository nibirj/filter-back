package service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.wisercat.test.assignment.Main;
import org.wisercat.test.assignment.database.repository.FilterRepository;
import org.wisercat.test.assignment.protocol.AmountCriteriaDTO;
import org.wisercat.test.assignment.protocol.DateCriteriaDTO;
import org.wisercat.test.assignment.protocol.FilterWithCriteriaDTO;
import org.wisercat.test.assignment.protocol.TitleCriteriaDTO;
import org.wisercat.test.assignment.service.FilterServiceImpl;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtest.properties")
public class FilterServiceImplTest {



    @Autowired
    FilterRepository filterRepository;

    @Autowired
    private FilterServiceImpl filterService;

    @Test
    public void testCreateFilter() {
        FilterWithCriteriaDTO createdFilter = filterService.createFilter(FilterWithCriteriaDTO.builder().name("Test1")
                .criteriaDTOList(List.of())
                .build());
        assertEquals(3, filterService.listAllFilters().size());
        filterService.deleteFilter(createdFilter.getId());
    }

    @Test
    public void testDeleteFilter() {
        FilterWithCriteriaDTO createdFilter = filterService.createFilter(FilterWithCriteriaDTO.builder().name("Test1")
                .criteriaDTOList(List.of())
                .build());
        filterService.deleteFilter(createdFilter.getId());
        assertEquals(2, filterService.listAllFilters().size());
    }

    @Test
    public void testGetFilterById() {
        FilterWithCriteriaDTO createdFilter = filterService.createFilter(FilterWithCriteriaDTO.builder().name("Test1")
                .criteriaDTOList(List.of())
                .build());
        FilterWithCriteriaDTO expected = FilterWithCriteriaDTO.builder().id(createdFilter.getId())
                .name("Test1")
                .criteriaDTOList(List.of()).build();
        assertEquals(expected, filterService.getFilter(createdFilter.getId()).get());
        filterService.deleteFilter(createdFilter.getId());
    }

    @Test
    @Transactional
    public void testUpdateFilter() throws ParseException {
        FilterWithCriteriaDTO createdFilter = filterService.createFilter(FilterWithCriteriaDTO.builder().name("Test1")
                        .criteriaDTOList(List.of(
                                AmountCriteriaDTO.builder()
                                    .comparableValue(4)
                                    .comparingCondition("More").build(),
                                DateCriteriaDTO
                                        .builder()
                                        .comparableDate("2024-03-19")
                                        .comparingCondition("From").build(),
                                TitleCriteriaDTO
                                        .builder()
                                        .comparableName("NewName")
                                        .comparingCondition("Contains").build()
                                )).build());
        Long filterId = createdFilter.getId();
        FilterWithCriteriaDTO newlyCreatedFilter = filterService.getFilter(filterId).get();
        Optional<FilterWithCriteriaDTO> newFilter = filterService.updateFilter(filterId,
                FilterWithCriteriaDTO.builder()
                        .id(filterId)
                        .name("Test1")
                        .criteriaDTOList(
                List.of(AmountCriteriaDTO.builder()
                        .comparableValue(4)
                        .comparingCondition("More").build(),
                AmountCriteriaDTO.builder()
                        .id(newlyCreatedFilter.getCriteriaDTOList().stream().filter(criteriaDTO ->
                                criteriaDTO instanceof AmountCriteriaDTO).findFirst().get().getId())
                        .comparableValue(3)
                        .comparingCondition("More").build(),
                TitleCriteriaDTO
                        .builder()
                        .comparableName("NewName2")
                        .comparingCondition("Contains").build(),
                TitleCriteriaDTO
                        .builder()
                        .id(newlyCreatedFilter.getCriteriaDTOList().stream().filter(criteriaDTO ->
                                criteriaDTO instanceof TitleCriteriaDTO).findFirst().get().getId())
                        .comparableName("NewName")
                        .comparingCondition("Contains").build(),
                DateCriteriaDTO
                        .builder()
                        .comparableDate("9999-03-19")
                        .comparingCondition("From").build(),
                DateCriteriaDTO
                        .builder()
                        .id(newlyCreatedFilter.getCriteriaDTOList().stream().filter(criteriaDTO ->
                                criteriaDTO instanceof DateCriteriaDTO).findFirst().get().getId())
                        .comparableDate("2024-03-19")
                        .comparingCondition("From").build())).build());
        assertEquals(newFilter.get().getCriteriaDTOList().size(), 6);
        filterRepository.deleteById(filterId);
    }
}
