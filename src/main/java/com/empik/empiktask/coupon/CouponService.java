package com.empik.empiktask.coupon;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import com.empik.empiktask.common.TaskAppException;
import com.empik.empiktask.coupon.dpo.CouponUsed;
import com.empik.empiktask.coupon.dpo.NewCoupon;
import com.empik.empiktask.geoapi.GeoApi;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository repository;
    private final GeoApi geoApi;

    public UUID createCoupon(NewCoupon coupon) {
        log.info("Creating coupon {}", coupon);
        if (repository.couponWithCodeExists(coupon.code())) {
            throw new TaskAppException(String.format("Coupon with code %s already exists", coupon.code()));
        }
        Coupon newCoupon = Coupon.createNewCoupon(
            coupon.code(),
            coupon.maxUsage(),
            coupon.country()
        );
        log.info("New coupon prepared {}", newCoupon.getCouponId());
        repository.save(newCoupon);
        return newCoupon.getCouponId();
    }

    @Transactional
    public void registerCouponUsageByUser(CouponUsed couponUsed) {
        log.info("Try to use coupon {}", couponUsed);
        if (repository.couponCodeUsedByUser(couponUsed.code(), couponUsed.userId())) {
            log.error("User {} already used coupon {}", couponUsed.userId(), couponUsed.code());
            throw new TaskAppException("Coupon already used by user");
        }
        Coupon coupon = repository.findByCode(couponUsed.code())
            .orElseThrow(() -> new TaskAppException(String.format("Coupon with code %s not found", couponUsed.code())));
        CountryCode userCountryCodeBasedOnIp = geoApi.getCountryCodeByIp(IpAddress.from(couponUsed.userId()));
        coupon.registerCouponUsage(userCountryCodeBasedOnIp);
        repository.save(coupon);
    }
}
