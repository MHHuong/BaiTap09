package vn.host.graphql;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import vn.host.entity.Category;
import vn.host.entity.Product;

import vn.host.entity.User;
import vn.host.repository.CategoryRepo;
import vn.host.repository.ProductRepo;
import vn.host.repository.UserRepo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AppGraphQL {

    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

    @QueryMapping
    public List<Product> productsByPriceAsc() {
        try {
            return productRepo.findAllByOrderByPriceAsc();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("DB_ERROR: " + e.getMessage());
        }
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepo.findByCategoryId(categoryId);
    }

    @QueryMapping public List<User> users() { return userRepo.findAll(); }
    @QueryMapping public List<Category> categories() { return categoryRepo.findAll(); }
    @QueryMapping public List<Product> products() { return productRepo.findAll(); }
    @QueryMapping public User user(@Argument Long id) { return userRepo.findById(id).orElse(null); }
    @QueryMapping public Category category(@Argument Long id) { return categoryRepo.findById(id).orElse(null); }
    @QueryMapping public Product product(@Argument Long id) { return productRepo.findById(id).orElse(null); }

    public record UserInput(Long id, String fullname, String email, String password, String phone, List<Long> categoryIds) {}

    @MutationMapping
    @Transactional
    public User createUser(@Argument UserInput in) {
        User u = User.builder()
                .fullname(in.fullname()).email(in.email())
                .password(in.password()).phone(in.phone())
                .categories(resolveCategories(in.categoryIds()))
                .build();
        return userRepo.save(u);
    }

    @MutationMapping @Transactional
    public User updateUser(@Argument UserInput in) {
        User u = userRepo.findById(in.id()).orElseThrow();
        if (in.fullname()!=null) u.setFullname(in.fullname());
        if (in.email()!=null) u.setEmail(in.email());
        if (in.password()!=null) u.setPassword(in.password());
        if (in.phone()!=null) u.setPhone(in.phone());
        if (in.categoryIds()!=null) u.setCategories(resolveCategories(in.categoryIds()));
        return userRepo.save(u);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        if (!userRepo.existsById(id)) return false;
        userRepo.deleteById(id);
        return true;
    }

    public record CategoryInput(Long id, String name, String images) {}

    @MutationMapping
    public Category createCategory(@Argument CategoryInput in) {
        return categoryRepo.save(Category.builder().name(in.name()).images(in.images()).build());
    }

    @MutationMapping
    public Category updateCategory(@Argument CategoryInput in) {
        Category c = categoryRepo.findById(in.id()).orElseThrow();
        if (in.name()!=null) c.setName(in.name());
        if (in.images()!=null) c.setImages(in.images());
        return categoryRepo.save(c);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (!categoryRepo.existsById(id)) return false;
        categoryRepo.deleteById(id);
        return true;
    }

    public record ProductInput(Long id, String title, Integer quantity, String desc, Float price, Long userId) {}

    @MutationMapping
    public Product createProduct(@Argument ProductInput in) {
        User owner = (in.userId()!=null) ? userRepo.findById(in.userId()).orElse(null) : null;
        return productRepo.save(Product.builder()
                .title(in.title())
                .quantity(in.quantity())
                .desc(in.desc())
                .price(in.price()!=null ? BigDecimal.valueOf(in.price()) : null)
                .user(owner)
                .build());
    }

    @MutationMapping
    public Product updateProduct(@Argument ProductInput in) {
        Product p = productRepo.findById(in.id()).orElseThrow();
        if (in.title()!=null) p.setTitle(in.title());
        if (in.quantity()!=null) p.setQuantity(in.quantity());
        if (in.desc()!=null) p.setDesc(in.desc());
        if (in.price()!=null) p.setPrice(BigDecimal.valueOf(in.price()));
        if (in.userId()!=null) p.setUser(userRepo.findById(in.userId()).orElse(null));
        return productRepo.save(p);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (!productRepo.existsById(id)) return false;
        productRepo.deleteById(id);
        return true;
    }
    @SchemaMapping(typeName = "Product", field = "price")
    public Double mapPrice(vn.host.entity.Product p) {
        return p.getPrice() != null ? p.getPrice().doubleValue() : null;
    }
    private Set<Category> resolveCategories(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptySet();
        return new HashSet<>(categoryRepo.findAllById(ids));
    }
}