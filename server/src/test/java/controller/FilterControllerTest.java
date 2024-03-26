package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.wisercat.test.assignment.Main;
import org.wisercat.test.assignment.exceptions.WrongDateFormatException;
import org.wisercat.test.assignment.protocol.AmountCriteriaDTO;
import org.wisercat.test.assignment.protocol.DateCriteriaDTO;
import org.wisercat.test.assignment.protocol.FilterDTO;
import org.wisercat.test.assignment.protocol.FilterWithCriteriaDTO;
import org.wisercat.test.assignment.protocol.TitleCriteriaDTO;
import org.wisercat.test.assignment.service.FilterServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtest.properties")
public class FilterControllerTest {

    private final String dateString = "19-03-2024";
    private final String MORE = "More";
    private final String FROM = "From";
    private final String EQUAL_TO = "Equals to";
    private final String TITLE_COMPARABLE_VALUE = "Hello world!";
    private final long ID_1 = 1;
    private  ObjectWriter objectWriter;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FilterServiceImpl service;

    private final String url = "http://localhost:8080/";

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        this.objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }


    @Test
    public void testGetAllFiltersReturnOk() throws Exception {
        when(service.listAllFilters()).thenReturn(List.of(createFilterDTOData()));
        mvc.perform(get(this.url + "filter/filters")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test1"));
    }

    @Test
    public void testGetAllFiltersReturnEmptyListOk() throws Exception {
        when(service.listAllFilters()).thenReturn(List.of());
        mvc.perform(get(this.url + "filter/filters")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testGetFilterByIdReturnOk() throws Exception {
        when(service.getFilter((long) 1)).thenReturn(Optional.of(createFilterWithCriteriaDTOData()));
        mvc.perform(get(this.url + "filter/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test1"))
                .andExpect(jsonPath("$.criterias.[0].size()").value("4"));
    }

    @Test
    public void testGetFilterByIdReturnNotFound() throws Exception {
        when(service.getFilter((long) 2)).thenReturn(Optional.empty());
        final MvcResult mvcResult = mvc.perform(get(this.url + "filter/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(mvcResult.getResponse().getStatus(), 404);
    }

    @Test
    public void testCreateFilterOk() throws Exception {
        FilterWithCriteriaDTO createFilterData = createFilterWithCriteriaDTOData();
        String requestJson = this.objectWriter.writeValueAsString(newFilterObject());
        when(service.createFilter(createFilterData)).thenReturn(createFilterData);
        mvc.perform(post(this.url + "filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateFilterIsNotValidEmptyCriterias() throws Exception {
        String requestJson = objectWriter.writeValueAsString(createFilterWithCriteriaDTODataInvalid());
        mvc.perform(post(this.url + "filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateFilterInvalidDate() throws Exception {
        String requestJson = objectWriter.writeValueAsString(createFilterWithCriteriaDTOInvalidDate());
        when(this.service.createFilter(createFilterWithCriteriaDTOInvalidDate())).thenThrow(WrongDateFormatException.class);
        mvc.perform(post(this.url + "filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateFilterOk() throws Exception {
        FilterWithCriteriaDTO updateFilterData = updateFilterObject();
        String requestJson = this.objectWriter.writeValueAsString(updateFilterWithCriteriaDTOObject());
        when(service.updateFilter(ID_1, updateFilterObject()))
                .thenReturn(Optional.of(updateFilterData));
        mvc.perform(put(this.url + "filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateFilterNotFound() throws Exception {
        String requestJson=this.objectWriter.writeValueAsString(newFilterObject());
        when(service.updateFilter(ID_1,  newFilterObject())).thenReturn(Optional.empty());
        mvc.perform(put(this.url + "filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateFilterIsNotValidEmptyCriterias() throws Exception {
        String requestJson = objectWriter.writeValueAsString(createFilterWithCriteriaDTODataInvalid());
        mvc.perform(put(this.url + "filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateFilterInvalidDate() throws Exception {
        String requestJson = objectWriter.writeValueAsString(createFilterWithCriteriaDTOInvalidDate());
        mvc.perform(put(this.url + "filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteFilter() throws Exception {
        mvc.perform(delete(this.url + "filter/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private FilterWithCriteriaDTO newFilterObject() {
        return new FilterWithCriteriaDTO(null, "Test1",
                List.of(new AmountCriteriaDTO(null, MORE, 4),
                        new AmountCriteriaDTO(null, EQUAL_TO, 3),
                new TitleCriteriaDTO(null,EQUAL_TO, TITLE_COMPARABLE_VALUE),
                new DateCriteriaDTO(null, FROM, dateString)));
    }

    private FilterWithCriteriaDTO createFilterWithCriteriaDTODataInvalid() {
        return new FilterWithCriteriaDTO(null, "Test1",
                List.of());
    }

    private FilterWithCriteriaDTO createFilterWithCriteriaDTOInvalidDate() {
        return new FilterWithCriteriaDTO(null, "Test1",
                List.of(new DateCriteriaDTO(null, FROM, "asd")));
    }

    private FilterWithCriteriaDTO updateFilterWithCriteriaDTOObject() {
        return new FilterWithCriteriaDTO(ID_1, "Test1",
                List.of(new DateCriteriaDTO(ID_1, FROM, dateString)));
    }

    private FilterWithCriteriaDTO updateFilterObject() {
        return FilterWithCriteriaDTO.builder()
                .id(ID_1)
                .name("Test1")
                .criteriaDTOList(List.of(DateCriteriaDTO.builder()
                        .id(ID_1)
                        .comparingCondition(FROM).comparableDate(dateString).build())).build();
    }

    private FilterWithCriteriaDTO createFilterWithCriteriaDTOData() {
        return FilterWithCriteriaDTO.builder()
                .name("Test1")
                .criteriaDTOList(List.of(
                        AmountCriteriaDTO.builder()
                                .comparingCondition(MORE).comparableValue(4)
                                .build(),
                        AmountCriteriaDTO.builder()
                                .comparingCondition(EQUAL_TO).comparableValue(3)
                                .build(),
                        TitleCriteriaDTO.builder()
                                .comparingCondition(EQUAL_TO).comparableName(TITLE_COMPARABLE_VALUE)
                                .build(),
                        DateCriteriaDTO.builder()
                                .comparingCondition(FROM).comparableDate(dateString)
                                .build())
                ).build();
    }

    private FilterDTO createFilterDTOData() {
        return FilterDTO.builder()
                .id(ID_1)
                .name("Test1")
                .build();
    }
}
