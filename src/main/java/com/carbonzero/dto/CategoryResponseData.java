package com.carbonzero.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.carbonzero.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CategoryResponseData {

    private Long id;
    private String name;
    private String codeName;
    private Boolean isActive = true;
    private List<CategoryResponseData> subCategoryList;

    public static CategoryResponseData convertToCategoryResponseData(Category category){
        return CategoryResponseData.builder()
                .id(category.getId())
                .name(category.getName())
                .codeName(category.getName())
                .isActive(category.getIsActive())
                .subCategoryList(category.getSubCategoryList().stream()
                        .map(CategoryResponseData::convertToCategoryResponseData)
                        .collect(Collectors.toList()))
                .build();
    }
}
