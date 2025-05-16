package com.empik.empiktask.geoapi;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
class GeoApiService implements GeoApi {

    @Override
    public CountryCode getCountryCodeByIp(IpAddress address) {
        throw new NotImplementedException();
    }
}
