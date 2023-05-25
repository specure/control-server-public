package com.specure.utils.core;

import com.specure.common.enums.Platform;
import com.specure.request.core.measurement.request.PingRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@UtilityClass
public class MeasurementCalculatorUtil {
    public String getUuid() {
        return UUID.randomUUID().toString();
    }

    public String getToken (String secretKey, String uuid, Integer slot) {
        String preTestToken = uuid + "_" + slot;
        return preTestToken + "_" + SecureAuthUtil.calculateHMAC(secretKey, preTestToken);
    }

    public long median(List<PingRequest> pingRequests, Long shortest) {
        if(pingRequests == null) {
            return shortest;
        }
        long[] arr =  pingRequests.stream()
                .mapToLong( p -> p.getValue() != 0 ? p.getValue(): p.getValueServer())
                .toArray();

        if (arr.length == 0) {
            return shortest;
        }
        Arrays.sort(arr);
        long median;
        if (arr.length % 2 == 0)
            median = (arr[arr.length/2] + arr[arr.length/2 - 1])/2;
        else
            median = arr[arr.length/2];
        return median;
    }

    public long getSpeedBitPerSec(final long bytes, final long nsec) {
        return Math.round((double) bytes / (double) nsec * 1e9 * 8.0);
    }

    public String calculateMeanJitterInMms(Long resultInMeanJitter, Long resultOutMeanJitter) {

        String meanJitterFormattedString = null;

        try {
            if (resultInMeanJitter != null && resultOutMeanJitter != null && resultInMeanJitter > 0L && resultOutMeanJitter > 0L) {
                double meanJitter = (resultInMeanJitter + resultOutMeanJitter) / 2.0;
                DecimalFormat df = new DecimalFormat("#,##0.00");
                meanJitterFormattedString = df.format(meanJitter / 1000000).replace(',', '.');
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return meanJitterFormattedString;

    }

    public String calculateMeanPacketLossInPercent(Long objectiveDelay, Long objectiveCallDuration, Long resultInNumPackets, Long resultOutNumPackets) {

        String packetLossStr = "-";

        try {
            int total = 0;
            if ((objectiveDelay != null) && (objectiveDelay != 0)) {
                total = (int) (objectiveCallDuration / objectiveDelay);
            }

            float packetLossDown = (100f * ((float) (total - resultInNumPackets) / (float) total));
            float packetLossUp = (100f * ((float) (total - resultOutNumPackets) / (float) total));
            if ((packetLossDown >= 0) && (packetLossUp >= 0)) {
                double meanPacketLoss = (packetLossDown + packetLossUp) / 2.0f;
                DecimalFormat df = new DecimalFormat("0.0");
                packetLossStr = df.format(meanPacketLoss).replace(',', '.');
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return packetLossStr;
    }

    public Platform getPlatform(String platform, String model) {
        if (platform != null && !platform.isEmpty()) {
            switch (platform.toUpperCase()) {
                case "ANDROID": {
                    return Platform.ANDROID;
                }
                case "APPLET": {
                    return Platform.APPLET;
                }
                case "CLI": {
                    return (model != null && !model.isEmpty() && model.compareTo("bhprobe") == 0) ? Platform.PROBE : Platform.CLI;
                }
                case "IOS": {
                    return Platform.IOS;
                }
                case "RMBTWS": {
                    return Platform.BROWSER;
                }
                case "NODEJS": {
                    return Platform.NODEJS;
                }
                case "DESKTOP": {
                    return Platform.DESKTOP;
                }
                default: {
                    log.warn("Unknown value {} in platform! Setting UNKNOWN..", platform);
                    return Platform.UNKNOWN;
                }
            }
        }
        log.warn("Platform is null or empty! Setting UNKNOWN..");
        return Platform.UNKNOWN;
    }
}
