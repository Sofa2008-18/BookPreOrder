
package com.bookstore.bookpreorder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Введите корректный email")
    private String email;

    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;
}
