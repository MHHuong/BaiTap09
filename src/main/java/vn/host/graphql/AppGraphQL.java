package vn.host.graphql;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import vn.host.dto.CategoryInput;
import vn.host.dto.ProductInput;
import vn.host.dto.UserInput;
import vn.host.entity.Category;
import vn.host.entity.Product;
import vn.host.entity.User;
import vn.host.repository.CategoryRepo;
import vn.host.repository.ProductRepo;
import vn.host.repository.UserRepo;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Validated
public class AppGraphQL {

    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    // ======= Queries =======
    @QueryMapping
    public List<User> users() { return userRepo.findAll(); }

    @QueryMapping
    public User user(@Argument Long id) { return userRepo.findById(id).orElse(null); }

    @QueryMapping
    public List<Category> categories() { return categoryRepo.findAll(); }

    @QueryMapping
    public Category category(@Argument Long id) { return categoryRepo.findById(id).orElse(null); }

    @QueryMapping
    public List<Product> products() { return productRepo.findAll(); }

    @QueryMapping
    public Product product(@Argument Long id) { return productRepo.findById(id).orElse(null); }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        // Đổi theo quan hệ thực tế của bạn: categories_Id hoặc user_categories_Id...
        // Ví dụ nếu Product có ManyToMany< Category >:
        // return productRepo.findByCategories_Id(categoryId);

        // Mặc định tạm dùng theo repo mẫu ở trên:
        return productRepo.findByCategoryId(categoryId);
    }

    @QueryMapping
    public List<Product> productsByPriceAsc() {
        return productRepo.findAllByOrderByPriceAsc();
    }

    // ======= Mutations: USER =======
    @MutationMapping @Transactional
    public User createUser(@Argument @Valid UserInput in) {
        User u = User.builder()
                .fullname(in.getFullname())
                .email(in.getEmail())
                .password(in.getPassword())
                .phone(in.getPhone())
                .categories(resolveCategories(in.getCategoryIds()))
                .build();

        // mặc định role USER nếu chưa có
        if (u.getRole() == null) u.setRole(User.Role.USER);
        return userRepo.save(u);
    }

    @MutationMapping @Transactional
    public User updateUser(@Argument @Valid UserInput in) {
        User u = userRepo.findById(in.getId()).orElseThrow();
        if (in.getFullname() != null) u.setFullname(in.getFullname());
        if (in.getEmail() != null)    u.setEmail(in.getEmail());
        if (in.getPassword() != null) u.setPassword(in.getPassword());
        if (in.getPhone() != null)    u.setPhone(in.getPhone());
        if (in.getCategoryIds() != null) u.setCategories(resolveCategories(in.getCategoryIds()));
        if (u.getRole() == null) u.setRole(User.Role.USER);
        return userRepo.save(u);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        if (!userRepo.existsById(id)) return false;
        userRepo.deleteById(id);
        return true;
    }

    // ======= Mutations: CATEGORY =======
    @MutationMapping
    public Category createCategory(@Argument @Valid CategoryInput in) {
        return categoryRepo.save(Category.builder()
                .name(in.getName())
                .images(in.getImages())
                .build());
    }

    @MutationMapping
    public Category updateCategory(@Argument @Valid CategoryInput in) {
        Category c = categoryRepo.findById(in.getId()).orElseThrow();
        if (in.getName() != null)   c.setName(in.getName());
        if (in.getImages() != null) c.setImages(in.getImages());
        return categoryRepo.save(c);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (!categoryRepo.existsById(id)) return false;
        categoryRepo.deleteById(id);
        return true;
    }

    // ======= Mutations: PRODUCT =======
    @MutationMapping
    public Product createProduct(@Argument @Valid ProductInput in) {
        User owner = (in.getUserId() != null) ? userRepo.findById(in.getUserId()).orElse(null) : null;
        Product p = Product.builder()
                .title(in.getTitle())
                .quantity(in.getQuantity())
                .price(in.getPrice() != null ? BigDecimal.valueOf(in.getPrice()) : null)
                .user(owner)
                .build();

        // tránh field "desc" – dùng description
        if (in.getDescription() != null) {
            p.setDescription(in.getDescription());
        }
        return productRepo.save(p);
    }

    @MutationMapping
    public Product updateProduct(@Argument @Valid ProductInput in) {
        Product p = productRepo.findById(in.getId()).orElseThrow();
        if (in.getTitle() != null)       p.setTitle(in.getTitle());
        if (in.getQuantity() != null)    p.setQuantity(in.getQuantity());
        if (in.getPrice() != null)       p.setPrice(BigDecimal.valueOf(in.getPrice()));
        if (in.getDescription() != null) p.setDescription(in.getDescription());
        if (in.getUserId() != null)      p.setUser(userRepo.findById(in.getUserId()).orElse(null));
        return productRepo.save(p);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (!productRepo.existsById(id)) return false;
        productRepo.deleteById(id);
        return true;
    }

    // ======= Helpers =======
    private Set<Category> resolveCategories(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptySet();
        return new HashSet<>(categoryRepo.findAllById(ids));
    }
}