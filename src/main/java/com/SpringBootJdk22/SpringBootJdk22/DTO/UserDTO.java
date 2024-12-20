package com.SpringBootJdk22.SpringBootJdk22.DTO;


import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    @Size(min = 1, max = 50, message = "Email must be between 1 and 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String provider;
    private boolean locked;
    private Set<Long> roles;

    private String password;
}
