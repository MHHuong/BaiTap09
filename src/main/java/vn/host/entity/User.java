package vn.host.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    public enum Role { USER, ADMIN }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String fullname;

    @Column(unique = true, nullable = false, length=100)
    private String email;

    @Column(nullable = false, length=100)
    private String password;

    @Column(length=20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=10)
    private Role role; // USER | ADMIN

    @ManyToMany
    @JoinTable(
            name = "user_category",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
}