package vn.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.host.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> { }