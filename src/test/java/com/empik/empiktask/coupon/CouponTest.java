package com.empik.empiktask.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.error.TaskAppException;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CouponTest {

    @Test
    void createNewCoupon_should_create_new_coupon() {
        //given
        String couponCode = "WIOSNA-20";
        int maxUsage = 5;
        CountryCode countryCode = CountryCode.PL;
        //when
        Coupon result = Coupon.createNewCoupon(couponCode, maxUsage, countryCode);
        //then
        assertNotNull(result);
        assertNotNull(result.getCouponId());
        assertEquals(couponCode, result.getCode().code());
        assertEquals(LocalDate.now(), result.getCreatedAt());
        assertEquals(0, result.getActualUsage());
        assertEquals(5, result.getMaxUsage());
        assertEquals(CountryCode.PL, result.getCountry());
    }

    @Test
    void registerCouponUsage_should_register_usage() {
        //given
        Coupon result = Coupon.createNewCoupon("TEST-1", 5, CountryCode.BR);
        //when
        result.registerCouponUsage(CountryCode.BR);
        //then
        assertEquals(1, result.getActualUsage());
    }

    @Test
    void registerCouponUsage_should_throw_on_usage_exceeded() {
        //given
        Coupon result = Coupon.createNewCoupon("TEST-1", 5, CountryCode.BR);
        result.registerCouponUsage(CountryCode.BR);
        result.registerCouponUsage(CountryCode.BR);
        result.registerCouponUsage(CountryCode.BR);
        result.registerCouponUsage(CountryCode.BR);
        result.registerCouponUsage(CountryCode.BR);
        //when
        TaskAppException exception = assertThrows(TaskAppException.class,
            () -> result.registerCouponUsage(CountryCode.BR));
        //then
        assertEquals("Cannot use coupon, limit reached", exception.getMessage());
    }

    static Stream<Arguments> registerCouponUsageInvalidCountryData() {
        return Stream.of(
            Arguments.of(CountryCode.PL, ""),
            Arguments.of(null, ""),
            Arguments.of(CountryCode.CN, "")
        );
    }
    @ParameterizedTest
    @MethodSource("registerCouponUsageInvalidCountryData")
    void registerCouponUsage_should_throw_on_different_country(CountryCode countryCode) {
        //given
        Coupon result = Coupon.createNewCoupon("TEST-1", 5, CountryCode.BR);
        //when
        TaskAppException exception = assertThrows(TaskAppException.class,
            () -> result.registerCouponUsage(countryCode));
        //then
        assertEquals("Cannot use coupon, coupon for different country", exception.getMessage());
    }
}