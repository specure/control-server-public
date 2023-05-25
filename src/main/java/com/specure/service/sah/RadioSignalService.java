package com.specure.service.sah;

import com.specure.common.model.jpa.Measurement;
import com.specure.request.core.measurement.request.RadioSignalRequest;
import com.specure.response.sah.RadioSignalResponse;

import java.util.Collection;
import java.util.List;

public interface RadioSignalService {

    void processRadioSignal(Collection<RadioSignalRequest> signals, Measurement measurement);

    List<RadioSignalResponse> getRadioSignalsByOpenTestUuid(String openTestUuid);
}
