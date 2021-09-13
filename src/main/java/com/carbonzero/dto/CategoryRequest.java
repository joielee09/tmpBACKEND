package com.carbonzero.dto;

import com.github.dozermapper.core.Mapping;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;

    @Mapping("parentId")
    private Long parentId;
}
