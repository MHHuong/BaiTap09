package vn.host.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry reg) {
        // /user/**: chỉ cần đăng nhập (USER hoặc ADMIN đều vào được)
        reg.addInterceptor(new AuthInterceptor(null))
                .addPathPatterns("/user/**");
        // /admin/**: phải là ADMIN
        reg.addInterceptor(new AuthInterceptor("ADMIN"))
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // map sẵn các view nếu muốn
        registry.addViewController("/products").setViewName("products");
        registry.addViewController("/products-by-category").setViewName("productsByCategory");
    }
}