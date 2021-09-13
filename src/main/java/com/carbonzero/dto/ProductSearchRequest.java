package com.carbonzero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {

    private String searchKeyword;
    private Long categoryId;
    private String productName;
    private Long priceFrom = 0L;
    private Long priceTo = Long.MAX_VALUE;
    private String brand;
    private String description;
    private String sort = "id,desc";
    private int pageSize = 20;
    private int pageNumber = 0;

}
