package com.empik.empiktask.coupon;

import java.util.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Repository;

@Repository
class CouponNormalizingRepository implements CouponRepository {

    @Override
    public boolean couponWithCodeExists(String code) {
        return false;
    }

    @Override
    public Coupon save(Coupon newCoupon) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        throw new NotImplementedException();
    }

    @Override
    public boolean couponCodeUsedByUser(String code, String userId) {
        throw new NotImplementedException();
    }
}
