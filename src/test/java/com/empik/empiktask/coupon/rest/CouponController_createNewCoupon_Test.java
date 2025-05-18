package com.empik.empiktask.coupon.rest;

import static com.empik.empiktask.common.AppMappings.COUPON_BASE_REST_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.empik.empiktask.BaseTestIT;
import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.coupon.CouponService;
import com.empik.empiktask.coupon.dpo.NewCoupon;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

class CouponController_createNewCoupon_Test extends BaseTestIT {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CouponService service;

    static Stream<Arguments> createNewCouponInvalidBody() {
        return Stream.of(
            Arguments.of("""
                {
                  "country": "PL",
                  "maxUsage": 5
                }
                """, "Invalid code for new coupon"),
            Arguments.of("""
                {
                  "code": null,
                  "country": "PL",
                  "maxUsage": 5
                }
                """, "Invalid code for new coupon"),
            Arguments.of("""
                {
                  "code": "",
                  "country": "PL",
                  "maxUsage": 5
                }
                """, "Invalid code for new coupon"),
            Arguments.of("""
                {
                  "code": "TEST-1",
                  "maxUsage": 5
                }
                """, "Invalid country code"),
            Arguments.of("""
                {
                  "country": null,
                  "code": "TEST-1",
                  "maxUsage": 5
                }
                """, "Invalid country code"),
            Arguments.of("""
                {
                  "country": "DE",
                  "code": "TEST-1",
                  "maxUsage": 0
                }
                """, "Max usage, invalid value"),
            Arguments.of("""
                {
                  "country": "DE",
                  "code": "TEST-1",
                  "maxUsage": null
                }
                """, "Invalid max usage for new coupon"),
            Arguments.of("""
                {
                  "country": "DE",
                  "code": "TEST-1"
                }
                """, "Invalid max usage for new coupon")
        );
    }

    @ParameterizedTest
    @MethodSource("createNewCouponInvalidBody")
    void createNewCoupon_should_fail_on_invalid_body(String content, String expectedMessage) throws Exception {
        //when
        mvc.perform(post(COUPON_BASE_REST_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(expectedMessage))
        ;

    }

    @Test
    void createNewCoupon_should_return_201() throws Exception {
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
            .andExpect(status().isCreated())
        ;
        //then
        ArgumentCaptor<NewCoupon> captor = ArgumentCaptor.forClass(NewCoupon.class);
        verify(service, times(1)).createCoupon(captor.capture());
        assertEquals("TEST-1", captor.getValue().code());
        assertEquals(CountryCode.PL, captor.getValue().countryCode());
        assertEquals(5, captor.getValue().maxUsage());
    }
}