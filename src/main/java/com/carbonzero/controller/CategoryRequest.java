package com.carbonzero.controller;

import com.sun.istack.Nullable;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    @Nullable
    private Long parentId;
}
