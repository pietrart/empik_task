package com.empik.empiktask.coupon;

import static com.empik.empiktask.common.AppMappings.COUPON_BASE_REST_API;
import static com.empik.empiktask.common.AppMappings.REGISTER_COUPON_USED_BY_USER_REST_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.empik.empiktask.WiremockBase;
import com.empik.empiktask.common.CountryCode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CouponE2eTest extends WiremockBase {

    @Test
    void createNewCoupon() throws Exception {
        //given
        //when
        mvc.perform(post(COUPON_BASE_REST_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "code": "TEST-1",
                      "country": "PL",
                      "maxUsage": 5
                    }
                    """))
            .andExpect(status().isCreated());
        //then
        Optional<Coupon> coupon = repository.findByCode("TEST-1");
        assertTrue(coupon.isPresent());
        assertEquals(0, coupon.get().getActualUsage());
    }

    @Test
    void registerCouponUsageByUser() throws Exception {
        //given
        String testIp = "127.0.0.1";
        //and
        repository.createNew(new Coupon(
            UUID.randomUUID(),
            new CouponCode("TEST-1"),
            LocalDate.now(),
            1,
            0,
            CountryCode.PL,
            0L
        ));
        //and
        geoApiMockReturnCountryCodePL(testIp);
        //when
        mvc.perform(post(COUPON_BASE_REST_API + REGISTER_COUPON_USED_BY_USER_REST_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "userId": "user-1",
                      "code": "TEST-1",
                      "ip": "127.0.0.1"
                    }
                    """))
            .andExpect(status().isOk())
        ;
        //then
        Optional<Coupon> coupon = repository.findByCode("TEST-1");
        assertTrue(coupon.isPresent());
        assertEquals(1, coupon.get().getActualUsage());
        //and
        UserCoupons userCoupons = repository.getUserCoupons("user-1");
        assertEquals(1, userCoupons.coupons().size());
    }
}
