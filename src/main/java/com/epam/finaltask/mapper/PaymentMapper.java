package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.order.payment.PaymentRequestDto;
import com.epam.finaltask.dto.order.payment.PaymentResponseDto;
import com.epam.finaltask.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto toPaymentResponseDto(Payment payment);
    void updateFromPaymentDto(PaymentRequestDto paymentRequestDto, @MappingTarget Payment payment);
}
