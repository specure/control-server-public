package com.specure.service.core;

import com.specure.common.enums.MeasurementServerType;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.model.dto.internal.DataForMeasurementRegistration;
import com.specure.request.core.ClientLocationRequest;
import com.specure.request.core.MeasurementRegistrationForAdminRequest;
import com.specure.request.core.MeasurementRegistrationForProbeRequest;
import com.specure.request.core.MeasurementRegistrationForWebClientRequest;
import com.specure.response.core.MeasurementServerResponseForSettings;
import com.specure.response.core.NearestMeasurementServersResponse;

import java.util.List;

public interface MeasurementServerService {

    DataForMeasurementRegistration getDataFromProbeMeasurementRegistrationRequest(MeasurementRegistrationForProbeRequest measurementRegistrationForProbeRequest);

    DataForMeasurementRegistration getMeasurementServerForWebClient(MeasurementRegistrationForWebClientRequest measurementRegistrationForWebClientRequest);

    DataForMeasurementRegistration getMeasurementServerById(MeasurementRegistrationForAdminRequest measurementRegistrationForAdminRequest);

    NearestMeasurementServersResponse getNearestServers(ClientLocationRequest clientLocationRequest);

    MeasurementServer getMeasurementServerById(long id);

    List<MeasurementServerResponseForSettings> getServers(List<MeasurementServerType> serverTypes);

    void deleteByServerById(long id);

    MeasurementServer getMeasurementServerByIdOrGetDefault(Long preferredServer);
}
