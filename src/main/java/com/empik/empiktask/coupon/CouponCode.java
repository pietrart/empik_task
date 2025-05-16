package com.empik.empiktask.coupon;

import com.empik.empiktask.common.error.TaskAppException;
import org.apache.commons.lang3.StringUtils;

record CouponCode (String code) {

    public void isValid() {
        //other validations if needed
        if (StringUtils.isBlank(code)) {
            throw new TaskAppException("Invalid coupon code");
        }
    }

    public String formattedCode() {
        return code.toUpperCase().trim();
    }
}
