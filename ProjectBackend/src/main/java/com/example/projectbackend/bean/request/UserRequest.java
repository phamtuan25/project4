package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "First name must not be blank")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+( [a-zA-ZÀ-ÿ]+)*$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+( [a-zA-ZÀ-ÿ]+)*$", message = "Last name must contain only letters")
    private String lastName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Phone number must be between 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=.]).{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    private User.RoleType role;
}
