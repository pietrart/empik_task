package com.empik.empiktask.geo;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import com.empik.empiktask.common.error.TaskAppException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class GeoServiceImpl implements GeoService {

    private final GeoApiClient client;
    private static final int maxCacheSize = 50_000;
    private final Cache<IpAddress, CountryCode> localCache = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.DAYS)
        .maximumSize(maxCacheSize)
        .build();

    @Override
    public CountryCode getCountryCodeByIp(IpAddress ip) {
        log.info("Resolve country code by ip: {}", ip.address());
        return localCache.get(ip, ipAdr -> {
            CountryCode countryCode = client.getCountryCodeFromIp(ipAdr);
            if (countryCode == null) {
                throw new TaskAppException("Country code not found for IP " + ipAdr.address());
            }
            return countryCode;
        });
    }
}
