package com.empik.empiktask.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empik.empiktask.BaseTestIT;
import com.empik.empiktask.common.CountryCode;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CouponNormalizingRepositoryTest extends BaseTestIT {

    @Test
    void couponWithCodeExists_should_return_true() {
        //given
        repository.save(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //when
        boolean result = repository.couponWithCodeExists("TEST-1");
        //then
        assertTrue(result);
    }

    @Test
    void couponWithCodeExists_should_return_false() {
        //given
        repository.save(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //when
        boolean result = repository.couponWithCodeExists("TEST-2");
        //then
        assertFalse(result);
    }

    @Test
    void save() {
        //given
        //when
        Coupon result = repository.save(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //then
        assertNotNull(result);
        assertNotNull(result.getCouponId());
        assertEquals("TEST-1", result.getCode().code());
        assertEquals(LocalDate.now(), result.getCreatedAt());
        assertEquals(5, result.getMaxUsage());
        assertEquals(0, result.getActualUsage());
        assertEquals(CountryCode.PL, result.getCountry());
    }

    @Test
    void findByCode_should_return_empty() {
        //given
        //when
        Optional<Coupon> result = repository.findByCode("TEST-1");
        //then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByCode() {
        //given
        repository.save(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //when
        Optional<Coupon> result = repository.findByCode("TEST-1");
        //then
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCouponId());
        assertEquals("TEST-1", result.get().getCode().code());
        assertEquals(LocalDate.now(), result.get().getCreatedAt());
        assertEquals(5, result.get().getMaxUsage());
        assertEquals(0, result.get().getActualUsage());
        assertEquals(CountryCode.PL, result.get().getCountry());
    }

    @Test
    void couponCodeUsedByUser_should_return_true() {
        //given
        repository.registerUserUsage("TEST-1", "user-1");
        repository.registerUserUsage("TEST-2", "user-1");
        repository.registerUserUsage("TEST-1", "user-2");
        //when
        boolean result = repository.couponCodeUsedByUser("TEST-1", "user-1");
        //then
        assertTrue(result);
    }

    @Test
    void couponCodeUsedByUser_should_return_false() {
        //given
        repository.registerUserUsage("TEST-1", "user-1");
        repository.registerUserUsage("TEST-2", "user-1");
        repository.registerUserUsage("TEST-1", "user-2");
        //when
        boolean result = repository.couponCodeUsedByUser("TEST-1", "user-3");
        //then
        assertFalse(result);
    }

    @Test
    void registerUserUsage() {
        //when
        repository.registerUserUsage("TEST-1", "user-1");
        //then
        assertTrue(repository.couponCodeUsedByUser("TEST-1", "user-1"));
    }
}