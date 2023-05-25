package com.specure.repository.sah;

import com.specure.dto.sah.NationalTableParams;
import com.specure.model.dto.NationalTable;

public interface StatsElasticsearchRepository {
    NationalTable getNationalTable(NationalTableParams nationalTableParams, String areaName);
}
