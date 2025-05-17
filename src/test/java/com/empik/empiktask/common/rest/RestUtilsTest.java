package com.empik.empiktask.common.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestUtilsTest {

    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    void extractIpFromRequest_should_extract_from_XForwardedFor() {
        //given
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.10");
        //when
        String ip = RestUtils.extractIpFromRequest(request);
        //then
        assertEquals("203.0.113.10", ip);
    }

    @Test
    void extractIpFromRequest_should_extract_first_ip_from_XForwardedFor() {
        //given
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.10, 198.51.100.5");
        //when
        String ip = RestUtils.extractIpFromRequest(request);
        //then
        assertEquals("203.0.113.10", ip);
    }

    @Test
    void extractIpFromRequest_should_extract_from_first_not_blanc() {
        //given
        when(request.getHeader("X-Forwarded-For")).thenReturn("");
        when(request.getHeader("Proxy-Client-IP")).thenReturn("198.51.100.5");
        //when
        String ip = RestUtils.extractIpFromRequest(request);
        //then
        assertEquals("198.51.100.5", ip);
    }

    @Test
    void extractIpFromRequest_should_extract_from_RemoteAddr() {
        //given
        when(request.getHeader(anyString())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        //when
        String ip = RestUtils.extractIpFromRequest(request);
        //then
        assertEquals("127.0.0.1", ip);
    }

    @Test
    void extractIpFromRequest_should_trim() {
        //given
        when(request.getHeader("X-Forwarded-For")).thenReturn("  192.168.1.1  ");
        //when
        String ip = RestUtils.extractIpFromRequest(request);
        //then
        assertEquals("192.168.1.1", ip);
    }
}