package com.empik.empiktask.coupon.dpo;

import jakarta.validation.constraints.NotBlank;

public record CouponUsed(@NotBlank(message = "Invalid user id") String userId,
                         @NotBlank(message = "Invalid coupon code") String code,
                         //todo asked about IP, dont know if I have to get it from headers on can be ensured by payload
                         @NotBlank(message = "Invalid IP") String ip) {

}
