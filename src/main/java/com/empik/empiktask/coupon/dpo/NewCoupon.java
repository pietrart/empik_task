package com.empik.empiktask.coupon.dpo;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.validators.CountryCodeValid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCoupon(@NotBlank(message = "Invalid code for new coupon") String code,
                        @NotNull(message = "Invalid max usage for new coupon") @Min(value = 1, message = "Max usage, invalid value") Integer maxUsage,
                        @JsonIgnore CountryCode countryCode,
                        @CountryCodeValid String country) {

    @Override
    public CountryCode countryCode() {
        return CountryCode.valueOf(country.toUpperCase());
    }
}
