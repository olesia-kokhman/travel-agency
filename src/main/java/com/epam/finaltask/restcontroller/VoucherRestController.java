package com.epam.finaltask.restcontroller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.service.VoucherService;
import com.epam.finaltask.service.VoucherServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherRestController {

    private VoucherService voucherService;

    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> getAllVouchers() {
        return ResponseEntity.ok(new ApiResponse<>("OK",
                "All vouchers are successfully found", voucherService.findAll()));
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> getAllVouchersByUserId(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(new ApiResponse<>("OK",
                "All vouchers of " + userId + " are successfully found",
                voucherService.findAllByUserId(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherDTO>> createVoucher(@RequestBody VoucherDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).
                contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponse<>("OK", "Voucher is successfully created",
                voucherService.create(dto)));
    }

    @PatchMapping("/{voucher_id}")
    public ResponseEntity<ApiResponse<VoucherDTO>> updateVoucher(@PathVariable("voucher_id") String voucherId,
                                                                 @RequestBody VoucherDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>("OK", "Voucher is successfully updated",
                voucherService.update(voucherId, dto)));
    }

    @DeleteMapping("/{voucher_id}")
    public ResponseEntity<ApiResponse<Void>> deleteVoucher(@PathVariable("voucher_id") String voucherId) {
        voucherService.delete(voucherId);
        return ResponseEntity.ok(new ApiResponse<>("OK",
                "Voucher with Id " + voucherId + " has been deleted", null));
    }

    @PatchMapping("/{voucher_id}/status")
    public ResponseEntity<ApiResponse<VoucherDTO>> changeVoucherStatus(@PathVariable("voucher_id") String voucherId,
                                                                       @RequestBody VoucherDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>("OK", "Voucher status is successfully changed",
                voucherService.changeHotStatus(voucherId, dto)));
    }

}
