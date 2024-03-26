package org.wisercat.test.assignment.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wisercat.test.assignment.exceptions.WrongDateFormatException;
import org.wisercat.test.assignment.protocol.FilterDTO;
import org.wisercat.test.assignment.protocol.FilterWithCriteriaDTO;
import org.wisercat.test.assignment.service.FilterServiceImpl;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/filter")
public class FilterController {

    @Autowired
    FilterServiceImpl filterService;

    @PostMapping()
    @ApiResponses(
            value = {
                    @ApiResponse(description = "New filter created", responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FilterWithCriteriaDTO.class))}),
                    @ApiResponse(description = "Filter is not valid.", responseCode = "422", content = @Content),
                    @ApiResponse(description = "We're sorry, but an unexpected error occurred while processing" +
                            " your request. Please try again later.", responseCode = "500", content = @Content)
            })
    public ResponseEntity<FilterWithCriteriaDTO> createFilter(@RequestBody FilterWithCriteriaDTO newFilter) {
        if (newFilter.getCriteriaDTOList().isEmpty() || newFilter.getName().isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        try {
            return ResponseEntity.ok(this.filterService.createFilter(newFilter));
        } catch (WrongDateFormatException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/filters")
    @ApiResponses(
            value = {
                    @ApiResponse(description = "Retrieved all filters", responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FilterDTO.class))) }),
                    @ApiResponse(description = "We're sorry, but an unexpected error occurred while processing" +
                            " your request. Please try again later.", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<FilterDTO>> getAll() {
        return ResponseEntity.ok(this.filterService.listAllFilters());
    }

    @GetMapping("/{filterId}")
    @ApiResponses(
            value = {
                    @ApiResponse(description = "Get filter defined by given filterId.", responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FilterWithCriteriaDTO.class))}),
                    @ApiResponse(description = "Filter not found.", responseCode = "404", content = @Content),
                    @ApiResponse(description = "We're sorry, but an unexpected error occurred while processing" +
                            " your request. Please try again later.", responseCode = "500", content = @Content)
            })
    public ResponseEntity<FilterWithCriteriaDTO> getFilterById(@PathVariable Long filterId) {
        Optional<FilterWithCriteriaDTO> filter = this.filterService.getFilter(filterId);
        return filter.map(value -> ResponseEntity.ok(filter.get()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping()
    @ApiResponses(
            value = {
                    @ApiResponse(description = "Filter defined by given filterId got updated", responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FilterWithCriteriaDTO.class))}),
                    @ApiResponse(description = "Filter not found.", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Filter is not valid.", responseCode = "422", content = @Content),
                    @ApiResponse(description = "We're sorry, but an unexpected error occurred while processing" +
                            " your request. Please try again later.", responseCode = "500", content = @Content)
            })
    public ResponseEntity<FilterWithCriteriaDTO> updateFilter(@RequestBody FilterWithCriteriaDTO newFilter) {
        if (newFilter.getCriteriaDTOList().isEmpty() || newFilter.getName().isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        try {
            return this.filterService.updateFilter(newFilter.getId(), newFilter)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (WrongDateFormatException | ParseException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{filterId}")
    @ApiResponses(
            value = {
                    @ApiResponse(description = "Filter defined by given filterId got deleted", responseCode = "200", content = @Content),
                    @ApiResponse(description = "We're sorry, but an unexpected error occurred while processing" +
                            " your request. Please try again later.", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Void> deleteFilter(@PathVariable Long filterId) {
        this.filterService.deleteFilter(filterId);
        return ResponseEntity.ok().build();
    }
}
