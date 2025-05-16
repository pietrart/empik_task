package com.empik.empiktask.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empik.empiktask.BaseTestIT;
import com.empik.empiktask.common.CountryCode;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

class CouponNormalizingRepositoryTest extends BaseTestIT {

    @Test
    void couponWithCodeExists_should_return_true() {
        //given
        repository.createNew(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //when
        boolean result = repository.couponWithCodeExists("TEST-1");
        //then
        assertTrue(result);
    }

    @Test
    void couponWithCodeExists_should_return_false() {
        //given
        repository.createNew(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //when
        boolean result = repository.couponWithCodeExists("TEST-2");
        //then
        assertFalse(result);
    }

    @Test
    void createNew() {
        //given
        //when
        Coupon result = repository.createNew(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
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
        repository.createNew(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //when
        Optional<Coupon> result = repository.findByCode("TesT-1");
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
        boolean result = repository.couponCodeUsedByUser("TesT-1", "user-1");
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

    @Test
    void updateUsage() {
        //given
        Coupon existing = repository.createNew(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        //and
        Coupon updateData = Coupon.builder().version(0L).code(new CouponCode("TEST-1")).actualUsage(2).build();
        //when
        repository.updateUsage(updateData);
        //then values unchanged
        Optional<Coupon> result = repository.findByCode("TesT-1");
        assertTrue(result.isPresent());
        assertEquals(existing.getCouponId(), result.get().getCouponId());
        assertEquals(existing.getMaxUsage(), result.get().getMaxUsage());
        assertEquals(existing.getCountry(), result.get().getCountry());
        assertEquals(existing.getCode().code(), result.get().getCode().code());
        //and actual usage updated
        assertNotEquals(existing.getActualUsage(), result.get().getActualUsage());
        assertEquals(2, result.get().getActualUsage());
    }

    @Test
    void updateUsage_should_throw_on_optimistic_locking() {
        //given
        Coupon existing = repository.createNew(Coupon.createNewCoupon("TEST-1", 5, CountryCode.PL));
        assertEquals(0, existing.getVersion());
        assertEquals(0, existing.getActualUsage());
        //and update data with version different from version in DB
        Coupon updateData = Coupon.builder().version(1L).code(new CouponCode("TesT-1")).actualUsage(1).build();
        //when
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.updateUsage(updateData));
    }
}