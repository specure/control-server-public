package com.specure.controller.mobile;

import com.specure.constant.URIConstants;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.response.mobile.MeasurementHistoryLoopUuidResponse;
import com.specure.response.mobile.MeasurementHistoryMobileResponse;
import com.specure.service.mobile.MeasurementHistoryMobileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MobileHistoryController {

    private final MeasurementHistoryMobileService measurementHistoryMobileService;

    @PostMapping(URIConstants.MOBILE + URIConstants.HISTORY)
    public MeasurementHistoryMobileResponse processResult(@PageableDefault Pageable pageable, @RequestBody MeasurementHistoryMobileRequest measurementHistoryMobileRequest) {
        return measurementHistoryMobileService.getMeasurementHistoryMobileResponse(pageable, measurementHistoryMobileRequest);
    }

    @PostMapping(URIConstants.V2 + URIConstants.MOBILE + URIConstants.HISTORY)
    public MeasurementHistoryLoopUuidResponse getMobileHistoryLoopUuidAggregation(@PageableDefault Pageable pageable, @RequestBody MeasurementHistoryMobileRequest measurementHistoryMobileRequest) {
        return measurementHistoryMobileService.getMeasurementHistoryMobileResponseLoopUuidAggregation(pageable, measurementHistoryMobileRequest);
    }

    @GetMapping(URIConstants.MOBILE + URIConstants.HISTORY_BY_UUID)
    public BasicTestHistoryMobileResponse getBasicTestHistoryResponseByUuid(@PathVariable String uuid) {
        return measurementHistoryMobileService.getMeasurementHistoryMobileResponseByUuid(uuid);
    }
}
