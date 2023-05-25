package com.specure.service.sah.impl;

import com.specure.mapper.sah.RadioSignalMapper;
import com.specure.common.model.jpa.Measurement;
import com.specure.model.jpa.RadioSignal;
import com.specure.repository.sah.RadioSignalRepository;
import com.specure.request.core.measurement.request.RadioSignalRequest;
import com.specure.response.sah.RadioSignalResponse;
import com.specure.service.sah.RadioSignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RadioSignalServiceImpl implements RadioSignalService {

    private final RadioSignalMapper radioSignalMapper;
    private final RadioSignalRepository radioSignalRepository;

    @Override
    public void processRadioSignal(Collection<RadioSignalRequest> signals, Measurement measurement) {
        int minSignalStrength = Integer.MAX_VALUE;
        int minLteRsrp = Integer.MAX_VALUE;
        int minLteRsrq = Integer.MAX_VALUE;
        List<RadioSignal> radioSignals = new ArrayList<>();


        for (RadioSignalRequest signalRequest : signals) {
            RadioSignal newSignal = radioSignalMapper.radioSignalRequestToRadioSignal(signalRequest, measurement);
            radioSignals.add(newSignal);

            if (Objects.nonNull(signalRequest.getSignalStrength()) && signalRequest.getSignalStrength() < minSignalStrength) {
                minSignalStrength = signalRequest.getSignalStrength();
            }
            if (Objects.nonNull(signalRequest.getLteRSRP()) && signalRequest.getLteRSRP() < minLteRsrp) {
                minLteRsrp = signalRequest.getLteRSRP();
            }

            if (Objects.nonNull(signalRequest.getLteRSRQ()) && signalRequest.getLteRSRQ() < minLteRsrq) {
                minLteRsrq = signalRequest.getLteRSRQ();
            }
        }

        if (minSignalStrength < 0) {
            measurement.setSignalStrength(minSignalStrength);
        }
        if (minLteRsrp < 0) {
            measurement.setLte_rsrp(minLteRsrp);
        }
        if (minLteRsrp < 0) {
            measurement.setLte_rsrq(minLteRsrq);
        }

        radioSignalRepository.saveAll(radioSignals);
    }

    @Override
    public List<RadioSignalResponse> getRadioSignalsByOpenTestUuid(String openTestUuid) {
        return radioSignalRepository.findAllByOpenTestUuid(openTestUuid).stream()
                .map(radioSignalMapper::radioSignalToRadioSignalResponse)
                .collect(Collectors.toList());
    }
}
