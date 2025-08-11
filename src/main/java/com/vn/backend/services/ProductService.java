package com.vn.backend.services;

import com.vn.backend.controllers.UserController;
import com.vn.backend.dto.request.CreateProductRequest;
import com.vn.backend.dto.response.AuthorResponse;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.ProductImageResponse;
import com.vn.backend.dto.response.ProductResponse;
import com.vn.backend.entities.Author;
import com.vn.backend.entities.Category;
import com.vn.backend.entities.Product;
import com.vn.backend.entities.ProductImage;
import com.vn.backend.repositories.CategoryRespository;
import com.vn.backend.repositories.ProductImageRepository;
import com.vn.backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
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

    @Autowired
    private final CategoryRespository categoryRespository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .listPrice(request.getListPrice())
                .originalPrice(request.getOriginalPrice())
                .publisherVn(request.getPublisherVn())
                .publicationDate(request.getPublicationDate())
                .dimensions(request.getDimensions())
                .bookCover(request.getBookCover())
                .numberOfPage(request.getNumberOfPage())
                .quantitySold(0)
                .stockQuantity(0)
                .isActive(true)
                .thumbnail(request.getThumbnail())
                .build();

        Category category = categoryRespository.findById(request.getCategoriesId()).get();
        logger.info("[INFO] POST /api/products - Category: {}", category);
        // Tạo mới danh sách Image
        List<ProductImage> images = request.getImages().stream()
                .map(img -> {
                    ProductImage productImage = ProductImage.builder()
                            .baseUrl(img.getBaseUrl())
                            .altText(img.getAltText())
                            .label(img.getLabel())
                            .isGallery(false)
                            .smallUrl(img.getSmallUrl())
                            .mediumUrl(img.getMediumUrl())
                            .largeUrl(img.getLargeUrl())
                            .thumbnailUrl(img.getThumbnailUrl())
                            .product(product)
                            .build()
                            ;
                    return productImage;
                })
                .collect(Collectors.toList());
        logger.info("[INFO] POST /api/products - images: {}", images);

        // 3. Tạo mới danh sách Author
        List<Author> authors = request.getAuthors().stream()
                .map(a -> {
                    Author author = Author.builder()
                            .name(a.getName())
                            .slug(a.getSlug())
                            .product(product)
                            .build();
                    return author;
                })
                .collect(Collectors.toList());
        logger.info("[INFO] POST /api/products - authors: {}", authors);

        product.setCategory(category);
        product.setAuthors(authors);
        product.setImages(images);
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

        // Xóa toàn bộ ảnh cũ
        product.getImages().clear();

        List<ProductImage> images = request.getImages().stream()
                .map(img -> {
                    ProductImage productImage = ProductImage.builder()
                            .baseUrl(img.getBaseUrl())
                            .altText(img.getAltText())
                            .label(img.getLabel())
                            .isGallery(false)
                            .smallUrl(img.getSmallUrl())
                            .mediumUrl(img.getMediumUrl())
                            .largeUrl(img.getLargeUrl())
                            .thumbnailUrl(img.getThumbnailUrl())
                            .product(product)
                            .build();
                    return productImage;
                })
                .collect(Collectors.toList());

        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setListPrice(request.getListPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.getImages().addAll(images);

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    @Transactional
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
                .categoriesId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
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
