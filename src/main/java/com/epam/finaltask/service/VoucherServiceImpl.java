package com.epam.finaltask.service;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {

    private VoucherRepository voucherRepository;
    private VoucherMapper voucherMapper;

    public VoucherServiceImpl(VoucherRepository voucherRepository, VoucherMapper voucherMapper) {
        this.voucherRepository = voucherRepository;
        this.voucherMapper = voucherMapper;
    }

    @Override
    public List<VoucherDTO> findAll() {
        System.out.println("IN SERVICE");
        System.out.println("find all: " + voucherRepository.findAll());
        return voucherRepository.findAll().stream().map(voucherMapper::toVoucherDTO).toList();
    }

    @Override
    public VoucherDTO create(VoucherDTO voucherDTO) {
        return null;
    }

    @Override
    public VoucherDTO order(String id, String userId) {
        return null;
    }

    @Override
    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
        return null;
    }

    @Override
    public void delete(String voucherId) {
    }

    @Override
    public VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO) {
        return null;
    }

    @Override
    public List<VoucherDTO> findAllByUserId(String userId) {
        return null;
    }

    @Override
    public List<VoucherDTO> findAllByTourType(TourType tourType) {
        return null;
    }

    @Override
    public List<VoucherDTO> findAllByTransferType(String transferType) {
        return null;
    }

    @Override
    public List<VoucherDTO> findAllByPrice(Double price) {
        return null;
    }

    @Override
    public List<VoucherDTO> findAllByHotelType(HotelType hotelType) {
        return null;
    }

}
