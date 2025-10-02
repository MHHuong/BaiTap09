package vn.host.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryInput {
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be <= 100 chars")
    private String name;

    @Pattern(regexp = "^(https?://.*)?$", message = "Images must be a URL (http/https) or left blank")
    private String images;
}