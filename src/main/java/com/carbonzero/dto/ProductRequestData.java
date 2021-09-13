package com.carbonzero.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.github.dozermapper.core.Mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRequestData {
    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("brand")
    private String brand;

    @NotNull
    @Mapping("price")
    private Long price;

    @Mapping("description")
    private String description;

    @Mapping("imageLink")
    private List<String> imageLink;

    @Mapping("categoryId")
    private Long categoryId;

    @Mapping("isEcoFriendly")
    private Boolean isEcoFriendly;

    @Mapping("carbonEmissions")
    private Integer carbonEmissions;
}
