package com.empik.empiktask.coupon.rest;

import static com.empik.empiktask.common.AppMappings.COUPON_BASE_REST_API;
import static com.empik.empiktask.common.AppMappings.REGISTER_COUPON_USED_BY_USER_REST_API;

import com.empik.empiktask.common.error.ApiErrorResponse;
import com.empik.empiktask.coupon.dpo.CouponUsed;
import com.empik.empiktask.coupon.dpo.NewCoupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Coupon api")
@RequestMapping(COUPON_BASE_REST_API)
@Validated
interface CouponResource {

    @Operation(summary = "Create new coupon")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UUID createNewCoupon(@Valid @RequestBody NewCoupon coupon);

    @Operation(summary = "Register coupon usage by user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
    })
    @PostMapping(REGISTER_COUPON_USED_BY_USER_REST_API)
    @ResponseStatus(HttpStatus.OK)
    void registerCouponUsageByUser(@Valid @RequestBody CouponUsed couponUsed, HttpServletRequest request);
}
