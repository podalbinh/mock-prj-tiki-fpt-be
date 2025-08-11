package com.vn.backend.services;

import com.vn.backend.dto.request.CreateProductRequest;
import com.vn.backend.dto.response.AuthorResponse;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.ProductImageResponse;
import com.vn.backend.dto.response.ProductResponse;
import com.vn.backend.entities.Product;
import com.vn.backend.repositories.ProductImageRepository;
import com.vn.backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ProductImageRepository productImageRepository;

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .listPrice(request.getListPrice())
                .originalPrice(request.getOriginalPrice())
                .quantitySold(request.getQuantitySold())
                .ratingAverage(request.getRatingAverage())
                .publisherVn(request.getPublisherVn())
                .publicationDate(request.getPublicationDate())
                .dimensions(request.getDimensions())
                .bookCover(request.getBookCover())
                .numberOfPage(request.getNumberOfPage())
                .stockQuantity(request.getStockQuantity())
                .isActive(request.getIsActive())
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public List<ProductResponse> getAllProducts(Sort sort, Integer limit) {
        List<Product> entities = productRepository.findAll(sort);

        Stream<Product> stream = entities.stream();
        if (limit != null && limit > 0) {
            stream = stream.limit(limit);
        }
        return stream
                .map(this::mapToResponse)
                .toList();
    }


    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductResponse updateProduct(Long id, CreateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setListPrice(request.getListPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setQuantitySold(request.getQuantitySold());
        product.setRatingAverage(request.getRatingAverage());
        product.setPublisherVn(request.getPublisherVn());
        product.setPublicationDate(request.getPublicationDate());
        product.setDimensions(request.getDimensions());
        product.setBookCover(request.getBookCover());
        product.setNumberOfPage(request.getNumberOfPage());
        product.setStockQuantity(request.getStockQuantity());
        product.setIsActive(request.getIsActive());

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productImageRepository.deleteByProduct_Id(id);
        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .listPrice(product.getListPrice())
                .originalPrice(product.getOriginalPrice())
                .quantitySold(product.getQuantitySold())
                .ratingAverage(product.getRatingAverage())
                .publisherVn(product.getPublisherVn())
                .publicationDate(product.getPublicationDate())
                .dimensions(product.getDimensions())
                .bookCover(product.getBookCover())
                .numberOfPage(product.getNumberOfPage())
                .stockQuantity(product.getStockQuantity())
                .isActive(product.getIsActive())

                .authors(product.getAuthors().stream()
                        .map(a -> new AuthorResponse(a.getId(), a.getName(), a.getSlug()))
                        .collect(Collectors.toList()))
                .categories(
                        product.getCategory() != null
                                ? new CategoryResponse(product.getCategory().getId(), product.getCategory().getName())
                                : null
                )
                .images(product.getImages().stream()
                        .map(i -> new ProductImageResponse(
                                i.getId(),
                                i.getBaseUrl(),
                                i.getLabel(),
                                i.getLargeUrl(),
                                i.getMediumUrl(),
                                i.getSmallUrl(),
                                i.getThumbnailUrl(),
                                i.getAltText()
                        ))
                        .collect(Collectors.toList()))
                .build();
    }
}
