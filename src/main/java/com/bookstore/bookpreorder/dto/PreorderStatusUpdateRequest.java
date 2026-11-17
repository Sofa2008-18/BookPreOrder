
package com.bookstore.bookpreorder.dto;

import com.bookstore.bookpreorder.model.PreorderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreorderStatusUpdateRequest {

    @NotNull(message = "Необходимо указать новый статус предзаказа")
    private PreorderStatus status;
}
