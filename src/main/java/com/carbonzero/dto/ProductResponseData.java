package com.carbonzero.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.carbonzero.domain.Product;
import com.github.dozermapper.core.Mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Builder @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductResponseData {

    @Mapping("id")
    private Long id;

    @Mapping("createAt")
    private LocalDateTime createAt;

    @Mapping("updateAt")
    private LocalDateTime updateAt;

    @Mapping("isActive")
    private boolean isActive;

    @Mapping("name")
    private String name;

    @Mapping("brand")
    private String brand;

    @Mapping("price")
    private Long price;

    @Mapping("description")
    private String description;

    @Mapping("imageLink")
    private List<String> imageLink;

    @Mapping("category")
    private String category;

    @Mapping("isEcoFriendly")
    private Boolean isEcoFriendly;

    @Mapping("carbonEmissions")
    private Integer carbonEmissions;

    public static ProductResponseData convertToProductResponseData(Product product){
        return ProductResponseData.builder()
                .id(product.getId())
                .createAt(product.getCreateAt())
                .updateAt(product.getUpdateAt())
                .name(product.getName())
                .brand(product.getBrand())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageLink(product.getImageLink())
                .category(product.getCategory().getName())
                .isEcoFriendly(product.getIsEcoFriendly())
                .carbonEmissions(product.getCarbonEmissions())
                .build();
    }
}
