package com.empik.empiktask.geo;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;

public interface GeoService {

    CountryCode getCountryCodeByIp(IpAddress address);
}
