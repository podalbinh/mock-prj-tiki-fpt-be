package com.vn.backend.services;

import com.vn.backend.dto.request.ProductSearchRequest;
import com.vn.backend.dto.response.PageResponse;
import com.vn.backend.dto.response.ProductHPResponse;
import com.vn.backend.repositories.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    @Autowired
    private final ProductSearchRepository productSearchRepository;

    public PageResponse<List<ProductHPResponse>> searchProducts(ProductSearchRequest request) {
        Pageable pageable = createPageable(request);
        Page<Object[]> productPage;

        switch (request.getSortBy()) {
            case "popular":
                productPage = productSearchRepository.findProductsByPopularityOnly(
                        request.getMinRating(), request.getCategoryId(), pageable);
                break;
            case "price_asc":
                productPage = productSearchRepository.findProductsWithRatingAndSold(
                        request.getMinRating(), request.getCategoryId(), pageable);
                break;
            case "price_desc":
                productPage = productSearchRepository.findProductsByPopularity(
                        request.getMinRating(), request.getCategoryId(), pageable);
                break;
            default:
                productPage = productSearchRepository.findProductsByPopularityOnly(
                        request.getMinRating(), request.getCategoryId(), pageable);
        }
        if(request.getKeyword() != null && !request.getKeyword().equals("")){
            productPage = productSearchRepository.searchProductsByKeyword(request.getKeyword(), pageable);
        }

        List<ProductHPResponse> products = productPage.getContent().stream()
                .map(this::mapToProductHPResponse)
                .collect(Collectors.toList());

        return PageResponse.<List<ProductHPResponse>>builder()
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalElements((int) productPage.getTotalElements())
                .last(productPage.isLast())
                .sortBy(request.getSortBy())
                .orderBy(getOrderBy(request.getSortBy()))
                .content(products)
                .build();
    }

    private Pageable createPageable(ProductSearchRequest request) {
        return PageRequest.of(request.getPage(), request.getSize());
    }

    private String getOrderBy(String sortBy) {
        switch (sortBy) {
            case "popular":
                return "Số lượng bán chạy nhất";
            case "price_asc":
                return "Giá từ thấp đến cao";
            case "price_desc":
                return "Giá từ cao đến thấp";
            default:
                return "Phổ biến";
        }
    }

    private ProductHPResponse mapToProductHPResponse(Object[] row) {
        ProductHPResponse product = new ProductHPResponse(
                ((Number) row[0]).longValue(), // id
                (String) row[1], // name -> title
                (String)row[6], // author
                ((Number) row[2]).doubleValue(), // price
                ((Number) row[7]).doubleValue(), // originalPrice
                null, // discount - sẽ được set bởi mockData()
                ((Number) row[4]).doubleValue(), // rating
                ((Number) row[5]).intValue(), // sold
                (String) row[3], // thumbnailUrl
                null, // hasAd - sẽ được set bởi mockData()
                null, // hasTikiNow - sẽ được set bởi mockData()
                null, // isTopDeal - sẽ được set bởi mockData()
                null, // isFreeshipXtra - sẽ được set bởi mockData()
                null // isAuthentic - sẽ được set bởi mockData()
        );

        // Gọi hàm mockData() để lấy các trường dữ liệu ngẫu nhiên
        product.mockData();

        return product;
    }
}
