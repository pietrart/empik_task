package com.empik.empiktask.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import com.empik.empiktask.common.error.TaskAppException;
import com.empik.empiktask.coupon.dpo.CouponUsed;
import com.empik.empiktask.coupon.dpo.NewCoupon;
import com.empik.empiktask.geo.GeoService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository repository;
    @Mock
    private GeoService geoService;

    @InjectMocks
    private CouponService service;

    @Test
    void createCoupon_should_throw_on_coupon_exists() {
        //given
        String testCouponCode = "TEST-1";
        NewCoupon newCoupon = new NewCoupon(testCouponCode, 5, CountryCode.PL, CountryCode.PL.name());
        //and
        when(repository.couponWithCodeExists(testCouponCode)).thenReturn(true);
        //when
        TaskAppException exception = assertThrows(TaskAppException.class, () -> service.createCoupon(newCoupon));
        //then
        assertEquals("Coupon with code TEST-1 already exists", exception.getMessage());
    }

    @Test
    void createCoupon_should_return_new_coupon_id() {
        //given
        String testCouponCode = "TEST-1";
        NewCoupon newCoupon = new NewCoupon(testCouponCode, 5, CountryCode.PL, CountryCode.PL.name());
        //and
        when(repository.couponWithCodeExists(testCouponCode)).thenReturn(false);
        //when
        UUID created = service.createCoupon(newCoupon);
        //then
        assertNotNull(created);
        //and
        verify(repository, times(1)).createNew(any(Coupon.class));
    }

    @Test
    void registerCouponUsageByUser_should_throw_on_coupon_used_by_user() {
        //given
        String testCouponCode = "TEST-1";
        String userId = UUID.randomUUID().toString();
        String testIp = "127.0.0.1";
        CouponUsed couponUsed = new CouponUsed(userId, testCouponCode, IpAddress.from(testIp));
        //and
        when(repository.couponCodeUsedByUser(testCouponCode, userId)).thenReturn(true);
        //when
        TaskAppException exception = assertThrows(TaskAppException.class,
            () -> service.registerCouponUsageByUser(couponUsed));
        //then
        assertEquals("Coupon already used by user", exception.getMessage());
    }

    @Test
    void registerCouponUsageByUser_should_throw_on_coupon_not_found() {
        //given
        String testCouponCode = "TEST-1";
        String userId = UUID.randomUUID().toString();
        String testIp = "127.0.0.1";
        CouponUsed couponUsed = new CouponUsed(userId, testCouponCode, IpAddress.from(testIp));
        //and
        when(repository.couponCodeUsedByUser(testCouponCode, userId)).thenReturn(false);
        //and
        when(repository.findByCode(testCouponCode)).thenReturn(Optional.empty());
        //when
        TaskAppException exception = assertThrows(TaskAppException.class,
            () -> service.registerCouponUsageByUser(couponUsed));
        //then
        assertEquals("Coupon with code TEST-1 not found", exception.getMessage());
    }

    @Test
    void registerCouponUsageByUser() {
        //given
        String testCouponCode = "TEST-1";
        String userId = UUID.randomUUID().toString();
        String testIp = "127.0.0.1";
        CouponUsed couponUsed = new CouponUsed(userId, testCouponCode, IpAddress.from(testIp));
        //and
        when(repository.couponCodeUsedByUser(testCouponCode, userId)).thenReturn(false);
        //and
        Coupon existingCoupon = Coupon.createNewCoupon(testCouponCode, 5, CountryCode.PL);
        assertEquals(0, existingCoupon.getActualUsage());
        when(repository.findByCode(testCouponCode)).thenReturn(Optional.of(existingCoupon));
        //and
        when(geoService.getCountryCodeByIp(any(IpAddress.class))).thenReturn(CountryCode.PL);
        //and
        when(repository.updateUsage(any(Coupon.class))).thenReturn(existingCoupon);
        //when
        service.registerCouponUsageByUser(couponUsed);
        //then
        ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);
        verify(repository, times(1)).updateUsage(couponCaptor.capture());
        assertEquals(1, couponCaptor.getValue().getActualUsage());
        //and
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> userIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(repository, times(1)).registerUserUsage(codeCaptor.capture(), userIdCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(testCouponCode, codeCaptor.getValue());
    }
}