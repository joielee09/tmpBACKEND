package com.carbonzero.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carbonzero.domain.Product;
import com.carbonzero.dto.ProductRequestData;
import com.carbonzero.dto.ProductResponseData;

public interface ProductService {

    Page<ProductResponseData> getProducts(Pageable pageable);

    Product getProduct(Long id);

    Product createProduct(ProductRequestData productRequestData);

    Product updateProduct(Long id, ProductRequestData source);

    Product deleteProduct(Long id);
}
