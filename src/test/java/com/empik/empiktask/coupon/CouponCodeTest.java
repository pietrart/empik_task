package com.empik.empiktask.coupon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.empik.empiktask.common.error.TaskAppException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CouponCodeTest {

    static Stream<Arguments> isValidShouldThrowData() {
        return Stream.of(
            Arguments.of("    ", ""),
            Arguments.of(null, ""),
            Arguments.of(" ", ""),
            Arguments.of("", "")
        );
    }

    @ParameterizedTest
    @MethodSource("isValidShouldThrowData")
    void isValid_should_throw(String code) {
        //given
        CouponCode couponCode = new CouponCode(code);
        //when
        TaskAppException exception = assertThrows(TaskAppException.class, couponCode::isValid);
        //then
        assertEquals("Invalid coupon code", exception.getMessage());
    }

    static Stream<Arguments> isValidShouldNotThrowData() {
        return Stream.of(
            Arguments.of("OK", ""),
            Arguments.of("wiosna", ""),
            Arguments.of("any string will work", "")
        );
    }

    @ParameterizedTest
    @MethodSource("isValidShouldNotThrowData")
    void isValid_should_not_throw(String code) {
        //given
        CouponCode couponCode = new CouponCode(code);
        //when/then
        assertDoesNotThrow(couponCode::isValid);
    }

}