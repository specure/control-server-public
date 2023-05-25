package com.specure.service.sah;

import java.util.List;
import java.util.Map;

public interface NationalOperatorService {

    List<String> getNationalOperatorNames(String currentTenant, boolean isMno);

    Map<String, List<String>> getNationalOperatorsAliases();
}
