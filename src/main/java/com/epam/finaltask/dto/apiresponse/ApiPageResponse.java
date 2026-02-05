package com.epam.finaltask.dto.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiPageResponse<T> {

    private int statusCode;
    private String statusMessage;
    private List<T> results;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;

    private List<SortSpec> sort;
    public record SortSpec(String property, String direction) {}

    public static <T> ApiPageResponse<T> from(Page<T> pageData, int statusCode, String message) {
        List<SortSpec> sortSpecs = pageData.getSort().stream()
                .map(o -> new SortSpec(o.getProperty(), o.getDirection().name()))
                .toList();

        return ApiPageResponse.<T>builder()
                .statusCode(statusCode)
                .statusMessage(message)
                .results(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .numberOfElements(pageData.getNumberOfElements())
                .first(pageData.isFirst())
                .last(pageData.isLast())
                .hasNext(pageData.hasNext())
                .hasPrevious(pageData.hasPrevious())
                .sort(sortSpecs)
                .build();
    }


}
