package com.empik.empiktask.geo;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import com.empik.empiktask.common.error.TaskAppException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class GeoServiceImpl implements GeoService {

    private final GeoApiClient client;

    private final Map<IpAddress, CountryCode> localCache = new ConcurrentHashMap<>();
    private static final int maxCacheSize = 50_000;

    @Override
    public CountryCode getCountryCodeByIp(IpAddress ip) {
        log.info("Resolve country code by ip: {}", ip.address());
        clearCacheIfNeeded();
        return localCache.computeIfAbsent(ip, ipAdr -> {
            CountryCode countryCode = client.getCountryCodeFromIp(ipAdr);
            if (countryCode == null) {
                throw new TaskAppException("Country code not found for IP " + ipAdr.address());
            }
            return countryCode;
        });
    }

    private void clearCacheIfNeeded() {
        if (localCache.size() > maxCacheSize) {
            synchronized (this) {
                if (localCache.size() > maxCacheSize) {
                    localCache.clear();
                }
            }
        }
    }
}
