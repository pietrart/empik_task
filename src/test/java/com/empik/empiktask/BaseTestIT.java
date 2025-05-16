package com.empik.empiktask;

import com.empik.empiktask.coupon.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTestIT {

    @Autowired
    protected CouponRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }
}
