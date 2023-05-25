package com.specure.controller.sah;

import com.specure.response.sah.stats.NationalTableResponse;
import com.specure.service.sah.StatsService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.specure.constant.URIConstants.NATIONAL_TABLE;

@AllArgsConstructor
@RestController
public class StatsController {

    private final StatsService statsService;


    @ApiOperation("Get upload, download and latency per provider")
    @GetMapping(NATIONAL_TABLE)
    public NationalTableResponse getNationalTable(
            @RequestParam(name = "tech", required = false, defaultValue = "all") String mobileTechnology,
            @RequestParam(name = "code", required = false) String municipalityCode
    ) {
        return statsService.getNationalTable(mobileTechnology, municipalityCode);
    }
}
