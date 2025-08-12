package com.vn.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "is_gallery", nullable = false)
    private Boolean isGallery;

    @Column(name = "label", length = 255)
    private String label;

    @Column(name = "large_url")
    private String largeUrl;

    @Column(name = "medium_url")
    private String mediumUrl;

    @Column(name = "small_url")
    private String smallUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "alt_text", length = 255)
    private String altText;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
