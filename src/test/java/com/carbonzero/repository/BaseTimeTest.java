package com.carbonzero.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.carbonzero.domain.Product;
import com.carbonzero.error.ProductNotFoundException;

@DataJpaTest
@TestPropertySource("classpath:application-test.yml")
public class BaseTimeTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 저장할 때, 생성 및 수정 시각이 기록된다.")
    @Test
    public void whenSavingUserCreateTimeAndUpdateTime() {
        // given
        LocalDateTime now = LocalDateTime.now();

        productRepository.save(
            Product.builder()
            .id(1L)
            .name("헤드앤숄더 시트러스 레몬 샴푸")
            .brand("P&G")
            .description("미세 클렌징 성분으로 과도한 두피 유분을 제거하는 샴푸입니다.")
            .price(17500L)
            .isActive(true)
            .isEcoFriendly(true)
            .carbonEmissions(100)
            .build());

        // when
        Product product = productRepository.findById(1L)
            .orElseThrow(() -> new ProductNotFoundException(1L));

        // then
        assertThat(product.getCreateAt()).isAfter(now);
        assertThat(product.getUpdateAt()).isAfter(now);
    }
}
