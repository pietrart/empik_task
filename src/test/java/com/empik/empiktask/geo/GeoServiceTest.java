package com.empik.empiktask.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import com.empik.empiktask.common.error.TaskAppException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoServiceTest {

    @Mock
    private GeoApiClient client;

    @InjectMocks
    private GeoServiceImpl service;

    @Test
    void getCountryCodeByIp_should_throw_exception_on_code_not_found() {
        //given
        IpAddress testIp = IpAddress.from("127.0.0.1");
        when(client.getCountryCodeFromIp(testIp)).thenReturn(null);
        //when
        TaskAppException exception = assertThrows(TaskAppException.class, () -> service.getCountryCodeByIp(testIp));
        //then
        assertEquals("Country code not found for IP " + testIp.address(), exception.getMessage());
    }

    @Test
    void getCountryCodeByIp_should_work_with_multiple_threads() throws ExecutionException, InterruptedException {
        //given
        IpAddress ip = IpAddress.from("127.0.0.1");
        CountryCode expectedCode = CountryCode.US;

        //and
        when(client.getCountryCodeFromIp(ip)).thenAnswer(invocation -> {
            Thread.sleep(100);
            return expectedCode;
        });
        //and
        int threadCount = 20;
        //when
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<CountryCode>> futures = IntStream.range(0, threadCount)
            .mapToObj(i -> executor.submit(() -> service.getCountryCodeByIp(ip)))
            .toList();

        //then
        Set<CountryCode> resultSet = new CopyOnWriteArraySet<>();
        for (Future<CountryCode> future : futures) {
            resultSet.add(future.get());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        //and
        assertEquals(1, resultSet.size());
        assertTrue(resultSet.contains(expectedCode));

        verify(client, times(1)).getCountryCodeFromIp(ip);
    }
}