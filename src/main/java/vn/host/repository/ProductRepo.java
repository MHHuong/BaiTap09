package vn.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.host.entity.Category;
import vn.host.entity.Product;
import vn.host.entity.User;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByPriceAsc();

    // Products của 1 category (thông qua User ∈ Category)
    @Query("""
     select p from Product p
     where p.user.id in (
       select u.id from User u join u.categories c
       where c.id = :categoryId
     )
  """)
    List<Product> findByCategoryId(Long categoryId);
}