package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.entity.Tour;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Tour toVoucher(VoucherDTO voucherDTO);
    VoucherDTO toVoucherDTO(Tour tour);
}
