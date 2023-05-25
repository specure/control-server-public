package com.specure.utils.sah.region;

import java.util.List;

public interface RegionConverter {
    String getTenantPurpose();
    String getNameByCode(String code);
    List<NameCodeRelation> getAllNameCodeRelations();
}
