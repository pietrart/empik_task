package com.empik.empiktask.coupon.dpo;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCoupon(@NotBlank(message = "Invalid code for new coupon") String code,
                 @NotNull(message = "Invalid max usage for new coupon") Integer maxUsage,
                 @ValidEnum(enumClass = CountryCode.class, message = "Invalid country code") CountryCode country) {

}
