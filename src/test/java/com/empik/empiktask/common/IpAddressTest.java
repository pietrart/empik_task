package com.empik.empiktask.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empik.empiktask.common.error.TaskAppException;
import org.junit.jupiter.api.Test;

class IpAddressTest {

    @Test
    void from_should_return_ip() {
        String ipStr = "127.0.0.1";
        IpAddress ip = IpAddress.from(ipStr);
        assertEquals(ipStr, ip.address());
    }

    @Test
    void from_should_throw_on_null() {
        TaskAppException ex = assertThrows(TaskAppException.class, () -> IpAddress.from(null));
        assertTrue(ex.getMessage().contains("Invalid IP address"));
    }

    @Test
    void from_should_throw_on_empty() {
        TaskAppException ex = assertThrows(TaskAppException.class, () -> IpAddress.from("   "));
        assertTrue(ex.getMessage().contains("Invalid IP address"));
    }

    @Test
    void from_should_throw_on_invalid_format() {
        String invalidIp = "999.999.999.999";
        TaskAppException ex = assertThrows(TaskAppException.class, () -> IpAddress.from(invalidIp));
        assertTrue(ex.getMessage().contains("Invalid IP address"));
    }

    @Test
    void from_should_throw_on_invalid_format_partially() {
        String invalidIp = "192.168.0.";
        TaskAppException ex = assertThrows(TaskAppException.class, () -> {
            IpAddress.from(invalidIp);
        });
        assertTrue(ex.getMessage().contains("Invalid IP address"));
    }
}