package com.empik.empiktask.coupon.rest;

import static com.empik.empiktask.common.rest.RestUtils.extractIpFromRequest;

import com.empik.empiktask.common.IpAddress;
import com.empik.empiktask.coupon.CouponService;
import com.empik.empiktask.coupon.dpo.CouponUsed;
import com.empik.empiktask.coupon.dpo.NewCoupon;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class CouponController implements CouponResource {

    private final CouponService couponService;

    @Override
    public UUID createNewCoupon(NewCoupon coupon) {
        return couponService.createCoupon(coupon);
    }

    @Override
    public void registerCouponUsageByUser(CouponUsed couponUsed, HttpServletRequest request) {
        IpAddress clientIp = IpAddress.from(extractIpFromRequest(request));
        couponUsed = couponUsed.merge(clientIp);
        couponService.registerCouponUsageByUser(couponUsed);
    }

}
