package vn.host.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer quantity;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user; // owner/seller
}