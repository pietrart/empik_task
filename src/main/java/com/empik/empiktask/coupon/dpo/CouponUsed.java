package com.empik.empiktask.coupon.dpo;

import com.empik.empiktask.common.IpAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

public record CouponUsed(@NotBlank(message = "Invalid user id") String userId,
                         @NotBlank(message = "Invalid coupon code") String code,
                         @JsonIgnore IpAddress ip) {

    public CouponUsed merge(IpAddress ip) {
        return new CouponUsed(this.userId, this.code, ip);
    }
}
