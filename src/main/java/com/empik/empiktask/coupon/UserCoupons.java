package com.empik.empiktask.coupon;

import java.util.Set;

record UserCoupons(Set<Coupon> coupons, String userId) {

}
