package vn.host.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductInput {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @Min(value = 0, message = "Quantity must be >= 0")
    private Integer quantity;

    // NOTE: tránh field/column name "desc" vì va chạm từ khóa SQL
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be >= 0")
    private Float price;

    private Long userId;
}