package com.carbonzero.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carbonzero.domain.Product;
import com.carbonzero.dto.ProductResponseData;
import com.carbonzero.dto.ProductSearchRequest;
import com.carbonzero.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductRepository productRepository;
    @PersistenceContext EntityManager entityManager;


    /**
     * 검색엔진
     * @param productSearchRequest
     * @return
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseData> search(ProductSearchRequest productSearchRequest) {

        // Criteria Builder 사용
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        // where 절

        // 통합 검색(상품 이름, 브랜드, 설명, 카테고리)
        if(productSearchRequest.getSearchKeyword() != null && !Strings.isBlank(productSearchRequest.getSearchKeyword())) {
            predicates.add(
                    builder.or(builder.like(builder.lower(root.get("name")),"%" + productSearchRequest.getSearchKeyword().toLowerCase(Locale.ROOT) + "%"),
                            builder.like(builder.lower(root.get("brand")),"%" + productSearchRequest.getSearchKeyword().toLowerCase(Locale.ROOT) + "%"),
                            builder.like(builder.lower(root.get("description")),"%" + productSearchRequest.getSearchKeyword().toLowerCase(Locale.ROOT) + "%")
//                            , builder.equal(root.get("category_id").as(Long.class),productSearchRequest.getCategoryId())
            ));
        }

        // 상품명 검색
        if(productSearchRequest.getProductName() != null && !Strings.isBlank(productSearchRequest.getProductName())) {
            predicates.add(builder.like(builder.lower(root.get("name")),"%" + productSearchRequest.getProductName().toLowerCase(Locale.ROOT) + "%"));
        }

        // 브랜드 검색
        if(productSearchRequest.getBrand() != null && !Strings.isBlank(productSearchRequest.getBrand())) {
            predicates.add(builder.like(builder.lower(root.get("brand")),"%" + productSearchRequest.getBrand().toLowerCase(Locale.ROOT) + "%"));
        }

        // 설명 검색
        if(productSearchRequest.getDescription() != null && !Strings.isBlank(productSearchRequest.getDescription())) {
            predicates.add(builder.like(builder.lower(root.get("description")),"%" + productSearchRequest.getDescription().toLowerCase(Locale.ROOT) + "%"));
        }

        // 카테고리 검색(추후에 enum으로 관리)
//        if(productSearchRequest.getCategoryId() != null) {
//            predicates.add(builder.equal(root.get("category_id").as(Long.class),productSearchRequest.getCategoryId()));
//        }

        // 가격 검색(하한, 상한 설정)
        if(productSearchRequest.getPriceTo() != null || productSearchRequest.getPriceFrom() != null) {
            predicates.add(
                    builder.and(builder.greaterThanOrEqualTo(root.get("price").as(Long.class), productSearchRequest.getPriceFrom())
                            ,builder.lessThanOrEqualTo(root.get("price").as(Long.class), productSearchRequest.getPriceTo())));
        }

        query.where(predicates.toArray(new Predicate[predicates.size()]));

        // 정렬
        String sortField = productSearchRequest.getSort().split(",")[0];
        String sortDirectionStr = productSearchRequest.getSort().split(",")[1];
        Sort.Direction sortDirection = Sort.Direction.DESC;
        if(sortDirectionStr.toLowerCase(Locale.ROOT).equals("asc")){
            sortDirection = Sort.Direction.ASC;
        }

        // order by
        if(sortDirection.isAscending()){
            query.orderBy(builder.asc(root.get(sortField)));
        } else {
            query.orderBy(builder.desc(root.get(sortField)));
        }

        // 리스트 결과
        TypedQuery<Product> searchedProducts = entityManager.createQuery(query).setFirstResult(productSearchRequest.getPageNumber()).setMaxResults(productSearchRequest.getPageSize());
        List<Product> resultList = searchedProducts.getResultList();

        // 페이징을 위한 카운트 쿼리 추출
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Product> productRootCount = countQuery.from(Product.class);
        countQuery.select(builder.count(productRootCount)).where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        // 페이징 콘텐츠 dto로 변환
        return new PageImpl<>(resultList.stream().map(ProductResponseData::convertToProductResponseData).collect(Collectors.toList())
                ,PageRequest.of(productSearchRequest.getPageNumber(), productSearchRequest.getPageSize(), sortDirection,sortField),count);
    }

}
