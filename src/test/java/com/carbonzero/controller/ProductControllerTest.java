package com.carbonzero.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.carbonzero.domain.Category;
import com.carbonzero.domain.Product;
import com.carbonzero.dto.ProductRequestData;
import com.carbonzero.dto.ProductResponseData;
import com.carbonzero.dto.ProductSearchRequest;
import com.carbonzero.error.ProductNotFoundException;
import com.carbonzero.service.CategoryService;
import com.carbonzero.service.ProductSearchService;
import com.carbonzero.service.ProductServiceImpl;

@DisplayName("상품 컨트롤러 테스트")
@WebMvcTest(ProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductSearchService productSearchService;

    @BeforeEach
    void setUp() {

        Category category = Category.builder()
            .id(1L)
            .name("샴푸")
            .isActive(true)
            .parentCategory(null)
            .subCategoryList(null)
            .build();

        Product product = Product.builder()
            .id(1L)
            .name("헤드앤숄더 시트러스 레몬 샴푸")
            .brand("P&G")
            .category(category)
            .description("미세 클렌징 성분으로 과도한 두피 유분을 제거하는 샴푸입니다.")
            .price(17500L)
            .isActive(true)
            .isEcoFriendly(true)
            .carbonEmissions(100)
            .build();

        given(productService.createProduct(any(ProductRequestData.class))).willReturn(product);

        ProductResponseData responseData = ProductResponseData.convertToProductResponseData(product);
        given(productService.getProducts(any(Pageable.class))).willReturn(new PageImpl<>(List.of(responseData)));

        given(productService.getProduct(1L)).willReturn(product);

        given(productService.getProduct(1000L))
            .willThrow(new ProductNotFoundException(1000L));

        given(productSearchService.search(any(ProductSearchRequest.class)))
            .willReturn(new PageImpl<>(List.of(responseData)));

        given(productService.updateProduct(eq(1L), any(ProductRequestData.class)))
            .will(invocation -> {
                Long id = invocation.getArgument(0);
                ProductRequestData productRequestData = invocation.getArgument(1);
                return Product.builder()
                    .id(id)
                    .name(productRequestData.getName())
                    .brand(productRequestData.getBrand())
                    .price(productRequestData.getPrice())
                    .category(category)
                    .isActive(true)
                    .imageLink(productRequestData.getImageLink())
                    .description(productRequestData.getDescription())
                    .build();
            });

        given(productService.updateProduct(eq(1000L), any(ProductRequestData.class)))
            .willThrow(new ProductNotFoundException(1000L));

        given(productService.deleteProduct(1000L))
            .willThrow(new ProductNotFoundException(1000L));
    }

    @DisplayName("유효한 형식으로 상품 생성을 요청하면, 상태코드 201을 반환한다.")
    @Test
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(
            post("/products")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{"
                + "  \"brand\": \"P&G\",\n"
                + "  \"carbon_emissions\": 100,\n"
                + "  \"category\": 1,\n"
                + "  \"description\": \"미세 클렌징 성분으로 과도한 두피 유분을 제거하는 샴푸입니다.\",\n"
                + "  \"image_link\": [\n"
                + "    \"string\"\n"
                + "  ],\n"
                + "  \"is_eco_friendly\": true,\n"
                + "  \"name\": \"헤드앤숄더 시트러스 레몬 샴푸\",\n"
                + "  \"price\": 17500\n"
                + "}")
        )
            .andExpect(status().isCreated())
            .andExpect(content().string(
                containsString("\"brand\":\"P&G\"")
            ));
        verify(productService).createProduct(any(ProductRequestData.class));
    }

    @DisplayName("유효하지 않은 형식으로 상품 생성을 요청하면, 상태 코드 400을 반환한다.")
    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
            post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                    + "  \"brand\": \"P&G\",\n"
                    + "  \"carbon_emissions\": 100,\n"
                    + "  \"category\": 1,\n"
                    + "  \"description\": \"미세 클렌징 성분으로 과도한 두피 유분을 제거하는 샴푸입니다.\",\n"
                    + "  \"image_link\": [\n"
                    + "    \"string\"\n"
                    + "  ],\n"
                    + "  \"price\": 17500\n"
                    + "}")
        )
            .andExpect(status().isBadRequest());
    }

    @DisplayName("상품 리스트를 조회하였을 때, 상태 코드 200 및 상품 리스트를 반환한다.")
    @Test
    void list() throws Exception {
        mockMvc.perform(
            get("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().string(
                containsString("\"brand\":\"P&G\"")
            ));

        verify(productService).getProducts(any(Pageable.class));
    }

    @DisplayName("존재하는 상품에 대해서 상품 상세 조회를 하면 상태 코드 200을 반환한다.")
    @Test
    void detailWithExistedProduct() throws Exception {
        mockMvc.perform(
            get("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().string(
                containsString("\"brand\":\"P&G\"")
            ));

        verify(productService).getProduct(any(Long.class));
    }

    @DisplayName("존재하지 않은 상품을 조회하면, 상태 코드 404를 반환한다.")
    @Test
    void detailWithNotExistedProduct() throws Exception {
        mockMvc.perform(get("/products/1000"))
            .andExpect(status().isNotFound());
    }

    @DisplayName("특정 상품을 검색하였을 때, 상태코드 200을 반환한다.")
    @Test
    void searchWithExistedProduct() throws Exception {
        mockMvc.perform(
            post("/products/search")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                    + "  \"brand\": \"string\",\n"
                    + "  \"category\": \"string\",\n"
                    + "  \"description\": \"string\",\n"
                    + "  \"page_number\": 0,\n"
                    + "  \"page_size\": 3,\n"
                    + "  \"price_from\": 0,\n"
                    + "  \"price_to\": 0,\n"
                    + "  \"product_name\": \"string\",\n"
                    + "  \"search_keyword\": \"string\",\n"
                    + "  \"sort\": \"id,desc\"\n"
                    + "}")
        )
            .andExpect(status().isOk());

        verify(productSearchService).search(any(ProductSearchRequest.class));
    }

    @DisplayName("존재하는 상품에 대해서 수정 요청을 하면, 상태 코드 200을 반환한다.")
    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(
            patch("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                    + "  \"brand\": \"string\",\n"
                    + "  \"carbon_emissions\": 0,\n"
                    + "  \"category_id\": 1,\n"
                    + "  \"description\": \"string\",\n"
                    + "  \"image_link\": [\n"
                    + "    \"string\"\n"
                    + "  ],\n"
                    + "  \"is_eco_friendly\": true,\n"
                    + "  \"name\": \"string\",\n"
                    + "  \"price\": 100\n"
                    + "}")
        )
            .andExpect(status().isOk())
            .andExpect(content().string(
                containsString("\"price\":100")
            ));

        verify(productService).updateProduct(eq(1L), any(ProductRequestData.class));
    }

    @DisplayName("유효하지 않은 속성으로 상품 수정을 요청하면, 상태 코드 400을 반환한다.")
    @Test
    void updateWithInvalidAttributes() throws Exception {
        mockMvc.perform(
            patch("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                    + "  \"brand\": \"피존\",\n"
                    + "  \"carbon_emissions\": 100,\n"
                    + "  \"category\": \"string\",\n"
                    + "  \"description\": \"string\",\n"
                    + "  \"image_link\": [\n"
                    + "    \"string\"\n"
                    + "  ],\n"
                    + "  \"price\": 100\n"
                    + "}")
        )
            .andExpect(status().isBadRequest());
    }

    @DisplayName("존재하지 않는 상품에 대해서 수정 요청을 하면, 상태 코드 404을 반환한다.")
    @Test
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(
            patch("/products/1000")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                    + "  \"brand\": \"피존\",\n"
                    + "  \"carbon_emissions\": 100,\n"
                    + "  \"category\": \"string\",\n"
                    + "  \"description\": \"string\",\n"
                    + "  \"image_link\": [\n"
                    + "    \"string\"\n"
                    + "  ],\n"
                    + "  \"is_eco_friendly\": true,\n"
                    + "  \"name\": \"string\",\n"
                    + "  \"price\": 100\n"
                    + "}")
        )
            .andExpect(status().isNotFound());
    }

    @DisplayName("존재하는 상품을 삭제하였을 때 상태 코드 201을 반환한다.")
    @Test
    void deleteWithExistedProduct() throws Exception {
        mockMvc.perform(
            delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @DisplayName("존재하지 않는 상품을 삭제하였을 때 상태 코드 404를 반환한다.")
    @Test
    void deleteWithNotExistedProduct() throws Exception {
        mockMvc.perform(
            delete("/products/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1000L);
    }
}
