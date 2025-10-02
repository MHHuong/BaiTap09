package vn.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;   // <-- nhớ import
import vn.host.entity.Product;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByPriceAsc();

    @Query("""
                select p
                from Product p
                join p.user u
                join u.categories c
                where c.id = :categoryId
            """)
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
}