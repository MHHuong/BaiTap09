package vn.host.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserInput {
    private Long id;

    @NotBlank(message = "Fullname is required")
    @Size(max = 50, message = "Fullname must be <= 50 chars")
    private String fullname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @Size(min = 6, max = 100, message = "Password must be 6-100 chars")
    private String password;

    @Size(max = 20, message = "Phone must be <= 20 chars")
    private String phone;

    private List<Long> categoryIds;
}