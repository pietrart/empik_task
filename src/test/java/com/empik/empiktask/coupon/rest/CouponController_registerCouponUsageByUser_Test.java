package com.empik.empiktask.coupon.rest;

import static com.empik.empiktask.common.AppMappings.COUPON_BASE_REST_API;
import static com.empik.empiktask.common.AppMappings.REGISTER_COUPON_USED_BY_USER_REST_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.empik.empiktask.BaseTestIT;
import com.empik.empiktask.coupon.CouponService;
import com.empik.empiktask.coupon.dpo.CouponUsed;
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

class CouponController_registerCouponUsageByUser_Test extends BaseTestIT {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CouponService service;

    static Stream<Arguments> registerCouponUsageByUserInvalidBody() {
        return Stream.of(
            Arguments.of("""
                {
                  "userId": "",
                  "code": "TEST-1"
                }
                """, "Invalid user id")
            ,
            Arguments.of("""
                {
                  "userId": null,
                  "code": "TEST-1"
                }
                """, "Invalid user id"),
            Arguments.of("""
                {
                  "code": "TEST-1"
                }
                """, "Invalid user id"),
            Arguments.of("""
                {
                  "userId": "user-1",
                  "code": ""
                }
                """, "Invalid coupon code"),
            Arguments.of("""
                {
                  "userId": "user-1"
                }
                """, "Invalid coupon code"),
            Arguments.of("""
                {
                  "userId": "user-1",
                  "code": null
                }
                """, "Invalid coupon code")
        );
    }

    @ParameterizedTest
    @MethodSource("registerCouponUsageByUserInvalidBody")
    void registerCouponUsageByUser_should_fail_on_invalid_body(String content, String expectedMessage)
        throws Exception {
        //when
        mvc.perform(post(COUPON_BASE_REST_API + REGISTER_COUPON_USED_BY_USER_REST_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(expectedMessage))
        ;

    }

    @Test
    void registerCouponUsageByUser_should_return_201() throws Exception {
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
        ArgumentCaptor<CouponUsed> captor = ArgumentCaptor.forClass(CouponUsed.class);
        verify(service, times(1)).registerCouponUsageByUser(captor.capture());
        assertEquals("TEST-1", captor.getValue().code());
        assertEquals("user-1", captor.getValue().userId());
        assertEquals("127.0.0.1", captor.getValue().ip().address());
    }
}