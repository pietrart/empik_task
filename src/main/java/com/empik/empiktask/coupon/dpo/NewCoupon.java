package com.empik.empiktask.coupon.dpo;

import com.empik.empiktask.common.CountryCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCoupon(@NotBlank(message = "Invalid code for new coupon") String code,
                        @NotNull(message = "Invalid max usage for new coupon") @Min(value = 1, message = "Max usage, invalid value") Integer maxUsage,
                        @NotNull(message = "Invalid country code") CountryCode country) {

}
