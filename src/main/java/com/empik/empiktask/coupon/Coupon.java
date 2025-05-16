package com.empik.empiktask.coupon;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.TaskAppException;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
class Coupon {

    private final String couponId;
    private final CouponCode code;
    private final LocalDate createdAt;
    private final int maxUsage;
    private int actualUsage;
    private final CountryCode country;

    static Coupon createNewCoupon(String code, int maxUsage, CountryCode country) {
        CouponCode newCouponCode = new CouponCode(code);
        newCouponCode.isValid();
        return Coupon.builder()
            .code(newCouponCode)
            .maxUsage(maxUsage)
            .country(country)
            .createdAt(LocalDate.now())
            .actualUsage(0)
            .couponId(UUID.randomUUID().toString())
            .build();
    }

    void registerCouponUsage(CountryCode country) {
        canUseCoupon(country);
        this.actualUsage += 1;
    }

    private void canUseCoupon(CountryCode country) {
        if (usageExceeded()) {
            throw new TaskAppException("Cannot use coupon, limit reached");
        }
        if (!couponForCountry(country)) {
            throw new TaskAppException("Cannot use coupon, coupon for different country");
        }
    }

    private boolean usageExceeded() {
        return actualUsage >= maxUsage;
    }

    private boolean couponForCountry(CountryCode country) {
        return this.country.equals(country);
    }

}
