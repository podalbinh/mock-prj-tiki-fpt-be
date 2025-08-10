package com.vn.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name="short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name="list_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal listPrice;

    @Column(name="original_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    @Column(name = "rating_average")
    private Float ratingAverage;

    @Column(name="publisher_vn", nullable = false, length = 255)
    private String publisherVn;

    @Column(name="publication_date", nullable = false, length = 255)
    private String publicationDate;

    @Column(name="dimensions", nullable = false, length = 255)
    private String dimensions;

    @Column(name="book_cover", nullable = false, length = 255)
    private String bookCover;

    @Column(name="number_of_page", nullable = false, length = 255)
    private String numberOfPage;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Author> authors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductImage> images;
}
