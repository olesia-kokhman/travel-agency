package com.epam.finaltask.dto.order;

import com.epam.finaltask.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateDto {

    @NotNull(message = "Order status is required")
    private OrderStatus status;
}
