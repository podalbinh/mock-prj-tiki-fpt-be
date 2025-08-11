package com.vn.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Column(name = "quantity_sold", columnDefinition = "INT DEFAULT 0")
    private Integer quantitySold;

    @Column(name = "rating_average")
    private Float ratingAverage;

    @Column(name="publisher_vn", length = 255)
    private String publisherVn;

    @Column(name="publication_date", length = 255)
    private String publicationDate;

    @Column(name="dimensions", length = 255)
    private String dimensions;

    @Column(name="book_cover", length = 255)
    private String bookCover;

    @Column(name="number_of_page", length = 255)
    private String numberOfPage;

    @Column(name = "stock_quantity", columnDefinition = "INT DEFAULT 0")
    private Integer stockQuantity;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Author> authors = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;
}
