package com.specure.mapper.sah;

import com.specure.common.enums.NetworkType;
import com.specure.common.model.jpa.Measurement;
import com.specure.model.jpa.RadioSignal;
import com.specure.request.core.measurement.request.RadioSignalRequest;
import com.specure.response.sah.RadioSignalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RadioSignalMapper {

    @Mapping(target = "openTestUuid", source = "measurement.openTestUuid")
    @Mapping(target = "time", expression = "java(calculateSignalTime(radioSignalRequest, measurement))")
    @Mapping(target = "signalStrength", source = "radioSignalRequest.signalStrength")
    RadioSignal radioSignalRequestToRadioSignal(RadioSignalRequest radioSignalRequest, Measurement measurement);

    @Mapping(target = "signal", expression = "java(getSignalFromRadioSignal(radioSignal))")
    @Mapping(target = "technology", expression = "java(getNetworkType(radioSignal.getNetworkTypeId()))")
    RadioSignalResponse radioSignalToRadioSignalResponse(RadioSignal radioSignal);

    @Mapping(target = "signal", expression = "java(getSignalFromRadioSignalNN(radioSignalRequest))")
    @Mapping(target = "technology", expression = "java(getNetworkType(radioSignalRequest.getNetworkTypeId()))")
    RadioSignalResponse radioSignalRequestToRadioSignalNN(RadioSignalRequest radioSignalRequest);

    default Timestamp calculateSignalTime(RadioSignalRequest radioSignalRequest, Measurement measurement) {
        if (Objects.nonNull(measurement.getTime()) && Objects.nonNull(radioSignalRequest.getTimeNs())) {
            return new Timestamp(measurement.getTime().toInstant().plusNanos(radioSignalRequest.getTimeNs()).toEpochMilli());
        }
        return null;
    }

    default String getNetworkType(Integer networkTypeId) {
        return NetworkType.fromStringValue(String.valueOf(networkTypeId)).getCategory();
    }

    default Integer getSignalFromRadioSignal(RadioSignal radioSignal) {
        return Objects.nonNull(radioSignal.getSignalStrength()) ? radioSignal.getSignalStrength() : radioSignal.getLteRSRP();
    }

    default Integer getSignalFromRadioSignalNN(RadioSignalRequest radioSignalRequest) {
        return Objects.nonNull(radioSignalRequest.getSignalStrength()) ? radioSignalRequest.getSignalStrength() : radioSignalRequest.getLteRSRP();
    }
}
