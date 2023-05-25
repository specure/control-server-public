package com.specure.service.sah;

import com.specure.response.sah.stats.NationalTableResponse;

public interface StatsService {
    NationalTableResponse getNationalTable(String mobileTechnology, String municipalityCode);
}
