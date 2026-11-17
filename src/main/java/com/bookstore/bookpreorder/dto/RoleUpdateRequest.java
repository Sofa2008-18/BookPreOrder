
package com.bookstore.bookpreorder.dto;

import com.bookstore.bookpreorder.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequest {

    @NotNull(message = "Роль не должна быть пустой")
    private Role role;
}
