package com.carbonzero.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.carbonzero.domain.Category;
import com.carbonzero.domain.Product;
import com.carbonzero.dto.ProductRequestData;
import com.carbonzero.error.ProductNotFoundException;
import com.carbonzero.repository.CategoryRepository;
import com.carbonzero.repository.ProductRepository;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

@DisplayName("상품 서비스 테스트")
class ProductServiceImplTest {

    private ProductServiceImpl productService;

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryService categoryService = mock(CategoryService.class);

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        productService = new ProductServiceImpl(mapper, productRepository, categoryRepository, categoryService);

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

        given(productRepository.findAll()).willReturn(List.of(product));

        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        given(productRepository.save(any(Product.class))).will(invocation -> {
            Product source = invocation.getArgument(0);
            return Product.builder()
                .id(1L)
                .name(source.getName())
                .brand(source.getBrand())
                .category(source.getCategory())
                .description(source.getDescription())
                .price(source.getPrice())
                .isActive(source.getIsActive())
                .isEcoFriendly(source.getIsEcoFriendly())
                .carbonEmissions(source.getCarbonEmissions())
                .build();
        });
    }

    @DisplayName("존재하는 상품 아이디로 상품을 조회하면 해당 상품이 반환된다.")
    @Test
    void getProductWithExistedId() {
        Product product = productService.getProduct(1L);

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("헤드앤숄더 시트러스 레몬 샴푸");
        assertThat(product.getBrand()).isEqualTo("P&G");
    }

    @DisplayName("존재하지 않는 상품 아이디로 상품을 조회하면 예외가 발생한다.")
    @Test
    void getProductWithNotExistedId() {
        assertThatThrownBy(() -> productService.getProduct(1000L))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @DisplayName("상품을 생성하면, 생성된 상품이 반환된다.")
    @Test
    void createProduct() {

        ProductRequestData productRequestData = ProductRequestData.builder()
            .name("헤드앤숄더 시트러스 레몬 샴푸")
            .brand("P&G")
            .description("미세 클렌징 성분으로 과도한 두피 유분을 제거하는 샴푸입니다.")
            .price(17500L)
            .isEcoFriendly(true)
            .carbonEmissions(100)
            .build();

        Product createdProduct = productService.createProduct(productRequestData);

        verify(productRepository).save(any(Product.class));

        assertThat(createdProduct.getId()).isEqualTo(1L);
        assertThat(createdProduct.getName()).isEqualTo("헤드앤숄더 시트러스 레몬 샴푸");
    }

    @DisplayName("존재하는 상품 아이디로, 업데이트를 하면 상품 정보가 갱신된다.")
    @Test
    void updateProductWithExistedId() {
        Category category = Category.builder()
            .id(1L)
            .name("샴푸")
            .isActive(true)
            .parentCategory(null)
            .subCategoryList(null)
            .build();

        ProductRequestData productData = ProductRequestData.builder()
            .name("케라시스 샴푸")
            .brand("AK")
            .description("좋은 샴푸입니다.")
            .categoryId(category.getId())
            .price(3000L)
            .isEcoFriendly(true)
            .carbonEmissions(150)
            .build();

        Product product = productService.updateProduct(1L, productData);

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getPrice()).isEqualTo(3000L);
        assertThat(product.getCarbonEmissions()).isEqualTo(150);
    }

    @DisplayName("존재하지 않는 상품 아이디로, 상품 정보를 갱신하면, 예외가 발생한다.")
    @Test
    void updateProductWithNotExistedId() {
        ProductRequestData productRequestData = ProductRequestData.builder()
            .name("케라시스 샴푸")
            .brand("AK")
            .categoryId(1L)
            .description("좋은 샴푸입니다.")
            .price(3000L)
            .isEcoFriendly(true)
            .carbonEmissions(150)
            .build();

        assertThatThrownBy(() -> productService.updateProduct(1000L, productRequestData))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @DisplayName("존재하는 상품을 삭제를 시도하면, 정상적으로 삭제가 된다.")
    @Test
    void deleteProductWithExistedId() {
        productService.deleteProduct(1L);

        verify(productRepository).delete(any(Product.class));
    }

    @DisplayName("존재하지 않는 상품 삭제를 시도하면 예외가 발생한다.")
    @Test
    void deleteProductWithNotExistedId() {
        assertThatThrownBy(() -> productService.deleteProduct(1000L))
            .isInstanceOf(ProductNotFoundException.class);
    }
}
