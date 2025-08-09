package com.vn.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;


    @Lob // Large Object
    @Column(name = "thumbnail_url", columnDefinition = "LONGBLOB")
    private byte[] thumbnailUrl;    // lob lưu ảnh
}
