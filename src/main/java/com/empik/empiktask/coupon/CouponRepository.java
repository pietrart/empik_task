package com.empik.empiktask.coupon;

import java.util.Optional;

interface CouponRepository {

    boolean couponWithCodeExists(String code);

    Coupon save(Coupon newCoupon);

    Optional<Coupon> findByCode(String code);

    boolean couponCodeUsedByUser(String code, String userId);
}
