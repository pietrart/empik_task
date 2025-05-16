package com.empik.empiktask.geoapi;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;

public interface GeoApi {

    CountryCode getCountryCodeByIp(IpAddress address);
}
